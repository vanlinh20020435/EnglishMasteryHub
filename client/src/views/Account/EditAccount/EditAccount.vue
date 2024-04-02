<template>
  <v-app>
    <PublicNavbar />
    <v-main
      class="d-flex justify-center"
      style="width: 100vw; min-height: 100vh"
    >
      <v-container
        class="d-flex flex-column bg-linear__blue height-100 v-container__full edit-profile-page"
      >
        <div style="padding-left: 30px">
          <HeaderTitle
            textBtn="Quay lại"
            :handleClickBtn="
              () => {
                this.$router.go(-1);
              }
            "
            title="Chỉnh sửa thông tin"
          />
        </div>
        <div style="flex: 1" class="d-flex flex-column align-center mt-8">
          <v-form
            class="flex-1-1 w-75"
            v-model="validForm"
            @submit.prevent="handleEditProfile"
          >
            <div class="edit-container flex-1-1">
              <div class="edit-avt-box box-shadow">
                <img
                  :src="this.avatarPreview || this.dataProfile.avatar"
                  alt="Avt User"
                  class="edit-avt-preview"
                />
                <input
                  type="file"
                  name="avatar"
                  accept="image/*"
                  className="d-none"
                  :onChange="handleChangeFile"
                  id="avatar"
                  ref="fileInput"
                />
                <div @click="openSelectFile" className="img-edit-avt">
                  <img
                    src="@/assets/images/icon/ico_edit_avt.png"
                    alt="ico_edit_avt"
                  />
                </div>
              </div>

              <div
                class="edit-info pt-5 d-flex flex-wrap w-100 justify-space-between"
              >
                <v-col cols="6" class="edit-info_item">
                  <h3 class="edit-info_item-name font-semi-bold pl-5 pb-2">
                    Họ và tên
                  </h3>
                  <v-text-field
                    rounded
                    v-model="dataProfile.name"
                    variant="outlined"
                    hide-no-data
                    :rules="required"
                  />
                </v-col>
                <v-col cols="6" class="edit-info_item">
                  <h3 class="edit-info_item-name font-semi-bold pl-5 pb-2">
                    Giới tính
                  </h3>

                  <v-col>
                    <v-row class="d-flex align-center">
                      <div
                        @click="() => handleChangeGender(1)"
                        class="d-flex align-center mr-8 cursor-pointer"
                      >
                        <v-btn rounded class="mr-3 gender-btn">
                          <v-icon :color="checkGenderColor(1)" size="x-large"
                            >mdi-gender-male
                          </v-icon>
                        </v-btn>
                        <div>Nam</div>
                      </div>

                      <div
                        @click="() => handleChangeGender(0)"
                        class="d-flex align-center cursor-pointer"
                      >
                        <v-btn rounded class="mr-3 gender-btn">
                          <v-icon :color="checkGenderColor(0)" size="x-large"
                            >mdi-gender-female
                          </v-icon>
                        </v-btn>
                        <div>Nữ</div>
                      </div>
                    </v-row>
                  </v-col>
                </v-col>
                <v-col cols="6" class="edit-info_item">
                  <h3 class="edit-info_item-name font-semi-bold pl-5 pb-2">
                    Tên đăng nhập
                  </h3>
                  <v-text-field
                    rounded
                    v-model="dataProfile.username"
                    variant="outlined"
                    hide-no-data
                    :rules="required"
                  />
                </v-col>

                <v-col cols="6" class="edit-info_item">
                  <h3 class="edit-info_item-name font-semi-bold pl-5 pb-2">
                    Ngày sinh
                  </h3>
                  <v-dialog
                    ref="dialog"
                    v-model="openBirthdayPicker"
                    :return-value.sync="birthday"
                    persistent
                    auto
                    width="290px"
                  >
                    <template v-slot:activator="{ dateOfBirth }">
                      <v-text-field
                        v-model="dataProfile.birthday"
                        readonly
                        v-bind="dateOfBirth"
                        @click="() => (openBirthdayPicker = true)"
                        variant="outlined"
                        rounded
                        :rules="required"
                      ></v-text-field>
                    </template>
                    <v-date-picker
                      v-model="birthday"
                      year-icon="mdi-calendar-blank"
                      prev-icon="mdi-skip-previous"
                      next-icon="mdi-skip-next"
                      scrollable
                      @update:model-value="() => (openBirthdayPicker = false)"
                    ></v-date-picker>
                  </v-dialog>
                </v-col>

                <v-col cols="6" class="edit-info_item">
                  <h3 class="edit-info_item-name font-semi-bold pl-5 pb-2">
                    Email
                  </h3>
                  <v-text-field
                    rounded
                    v-model="dataProfile.email"
                    variant="outlined"
                    hide-no-data
                    :rules="required"
                  />
                </v-col>
              </div>
            </div>

            <v-row class="d-flex justify-end mt-10 mr-9">
              <v-btn
                class="pl-10 pr-10"
                rounded
                type="submit"
                color="#00bd7e"
                theme="dark"
                :disabled="!validForm"
                >Lưu</v-btn
              >
            </v-row>
          </v-form>
        </div>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import { mapState } from "pinia";
import { authenticationRole, toastStore } from "@/stores";
import PublicNavbar from "@/components/navbar/PublicNavbar.vue";
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet, apiCallerPut, apiCallerPost } from "@/services/teacher";
import avtFemale from "@/assets/images/avatar_student_female.jpg";
import avtMale from "@/assets/images/avatar_student_male.jpg";

export default {
  components: {
    PublicNavbar,
    HeaderTitle,
  },
  data: () => {
    return {
      dataProfile: {
        name: "",
      },
      validForm: true,
      dataBtnLeft: {
        text: "Chỉnh sửa",
      },
      dataRenderProfile: [
        {
          id: 1,
          icon: "mdi-account-outline",
          type: "username",
        },
        {
          id: 2,
          icon: "mdi-gender-male-female",
          type: "gender",
        },
        {
          id: 3,
          icon: "mdi-cake-variant-outline",
          type: "birthday",
        },
        {
          id: 4,
          icon: "mdi-email-outline",
          type: "email",
        },
      ],
      avatarPreview: null,
      required: [
        (v) => {
          if (v) return true;
          return "Trường nhập bắt buộc!";
        },
      ],
      openBirthdayPicker: false,
      birthday: "",
      optionsFormatDate: { day: "2-digit", month: "2-digit", year: "numeric" },
      avatarPutUrl: "",
    };
  },
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
    ...mapState(toastStore, ["updateToast"]),
  },
  mounted() {
    this.dataProfile = this.authentication.user;

    this.birthday = this.convertToDate(this.dataProfile.birthday);
  },
  watch: {
    birthday(val) {
      this.dataProfile.birthday = val
        ? new Date(val)
            .toLocaleDateString("en-GB", this.optionsFormatDate)
            .substr(0, 10)
        : null;
    },
  },
  methods: {
    getAvtUser(data) {
      if (data?.avatar) {
        return data.avatar;
      } else {
        if (data?.gender) {
          return avtMale;
        } else {
          return avtFemale;
        }
      }
    },
    getDetailProfile(data, type) {
      return type == "gender" ? (data[type] ? "Nam" : "Nữ") : data[type];
    },
    openSelectFile(e) {
      e.preventDefault();
      this.$refs.fileInput.click();
    },
    handleChangeFile(event) {
      const input = event.target;
      if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = () => {
          this.avatarPreview = reader.result; // Update the avatar preview
        };
        reader.readAsDataURL(input.files[0]);
      }
      this.upploadFile(input.files[0]);
    },
    async upploadFile(file) {
      const formData = new FormData();

      formData.append("file", file);

      const result = await apiCallerPost(
        "/upload",
        JSON.parse(localStorage?.getItem("accessToken"))?.token,
        formData
      );

      if (result.success) {
        this.avatarPutUrl = result.data.url;
      }
    },
    convertToDate(dateString) {
      const [day, month, year] = dateString.split("/").map(Number);

      return new Date(year, month - 1, day);
    },
    formatStringDate(dateString) {
      const [day, month, year] = dateString.split("/").map(Number);
      return `${year}-${month}-${day}`;
    },
    checkGenderColor(genderNum) {
      if (this.dataProfile.gender == genderNum) {
        return this.dataProfile.gender ? "#64cad4" : "#f989c2";
      } else {
        return "#4d4d4d";
      }
    },
    handleChangeGender(genderNum) {
      this.dataProfile.gender = genderNum;
    },
    async handleEditProfile() {
      if (this.validForm) {
        let urlAPI = "/api/user/userinfo";

        let birthdayConvert = this.formatStringDate(this.dataProfile.birthday);

        let dataBody = {
          name: this.dataProfile.name,
          username: this.dataProfile.username,
          email: this.dataProfile.email,
          gender: this.dataProfile.gender,
          birthday: this.dataProfile.birthday,
        };

        this.avatarPutUrl ? (dataBody.avatar = this.avatarPutUrl) : null;

        const result = await apiCallerPut(urlAPI, dataBody);

        if (result.success) {
          this.$router.go(-1);
        }
      }
    },
  },
};
</script>

<style>
@import "./EditAccount.style.scss";
</style>
