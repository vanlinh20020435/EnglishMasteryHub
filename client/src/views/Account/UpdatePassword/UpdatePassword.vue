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
            title="Đổi mật khẩu"
          />
        </div>

        <v-form
          class="flex-1-1"
          v-model="validForm"
          @submit.prevent="handleSavePassword"
        >
          <div
            class="d-flex flex-column align-center justify-center edit-password-container"
          >
            <v-col cols="8" class="edit-info_item ml-13">
              <h3 class="edit-info_item-name font-semi-bold pl-5 pb-2">
                Mật khẩu mới
              </h3>
              <v-text-field
                rounded
                v-model="dataPassword.password"
                :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                :type="show1 ? 'text' : 'password'"
                variant="outlined"
                hide-no-data
                :rules="required"
                @click:append="show1 = !show1"
              />
            </v-col>

            <v-col cols="8" class="edit-info_item ml-13">
              <h3 class="edit-info_item-name font-semi-bold pl-5 pb-2">
                Xác nhận mật khẩu
              </h3>
              <v-text-field
                rounded
                :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                :type="show2 ? 'text' : 'password'"
                v-model="dataPassword.cfPassword"
                variant="outlined"
                hide-no-data
                :rules="cfPasswordRules"
                @click:append="show2 = !show2"
              />
            </v-col>

            <v-row class="d-flex justify-end mt-10">
              <v-btn
                class="pl-15 pr-15"
                rounded
                type="submit"
                color="#00bd7e"
                theme="dark"
                :disabled="!validForm"
                >Lưu</v-btn
              >
            </v-row>
          </div>
        </v-form>
      </v-container>
    </v-main>
    <!-- Popup -->
    <PopUpYesNo
      :msg="msgPopup"
      :visible="openPopup"
      :handleClickYes="
        () => {
          if (this.msgPopup?.includes('thành công')) {
            this.$router.go(-1);
          }
          this.openPopup = false;
        }
      "
      hideBtnNo
      btnYes="Đóng"
      @update:visible="handleVisible"
    />
  </v-app>
</template>

<script>
import PublicNavbar from "@/components/navbar/PublicNavbar.vue";
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
import { apiCallerPut } from "@/services/teacher";

export default {
  name: "UpdatePassword",
  components: {
    PublicNavbar,
    HeaderTitle,
    PopUpYesNo,
  },
  data: () => {
    return {
      dataPassword: {
        password: "",
        cfPassword: "",
      },
      validForm: false,
      required: [
        (v) => {
          if (v) return true;
          return "Trường nhập bắt buộc!";
        },
      ],

      cfPasswordRules: "",
      openPopup: false,
      msgPopup: "",
      show1: false,
      show2: false,
    };
  },
  mounted() {
    this.cfPasswordRules = [
      (v) => {
        if (v) {
          if (this.dataPassword.password && v != this.dataPassword.password) {
            return "Mật khẩu xác nhận không chính xác!";
          }
          return true;
        }
        return "Trường nhập bắt buộc!";
      },
    ];
  },

  methods: {
    async handleSavePassword() {
      if (this.validForm) {
        let urlAPI =
          "/api/user/update-password?password=" + this.dataPassword.password;

        const result = await apiCallerPut(urlAPI);

        if (result.success) {
          this.msgPopup = "Mật khẩu mới đã được cập nhật thành công";
        } else {
          this.msgPopup = "Thay đổi mật khẩu thất bại";
        }

        this.openPopup = true;
      }
    },
    handleVisible(newValue) {
      this.openPopup = newValue;
    },
  },
};
</script>

<style>
@import "./UpdatePassword.style.scss";
</style>
