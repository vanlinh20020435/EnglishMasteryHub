<template>
  <v-app>
    <PublicNavbar />
    <v-main
      class="d-flex justify-center bg-linear__blue"
      style="width: 100vw; min-height: 100vh"
    >
      <v-container
        class="d-flex flex-column height-100 v-container__full profile-page"
      >
        <div style="padding-left: 30px">
          <HeaderTitle
            textBtn="Quay lại"
            :handleClickBtn="
              () => {
                this.$router.go(-1);
              }
            "
            title="Hồ sơ"
            leftButton
            :dataBtnLeft="dataBtnLeft"
            :handleClickBtnLeft="
              () => {
                this.$router.push(
                  `/${this.authentication?.user?.role}/account/edit`
                );
              }
            "
          />
        </div>
        <div
          style="max-height: 90%; overflow-y: auto"
          class="d-flex flex-column mt-10"
        >
          <div class="text-center bg-profile">
            <img src="@/assets/images/bg_hoso_detail.png" alt="Bg Profile" />
          </div>

          <div class="box-profile d-flex justify-center">
            <div class="box-profile_detail box-shadow">
              <div class="profile-avatar">
                <img :src="this.getAvtUser(dataProfile)" alt="Avt user" />
              </div>

              <h2 class="profile-name text-center font-bold">
                {{ dataProfile?.name || "..." }}
              </h2>

              <div class="profile-info pt-3">
                <div
                  v-for="(item, index) in dataRenderProfile"
                  :key="index"
                  class="d-flex align-center pt-1 pb-1"
                >
                  <v-icon
                    class="profile-info_icon"
                    size="large"
                    color="#808080"
                    >{{ item.icon }}</v-icon
                  >

                  <div class="profile-info_detail d-flex align-center">
                    {{ this.getDetailProfile(dataProfile, item.type) }}
                  </div>
                </div>
              </div>

              <v-row class="d-flex justify-end mt-10">
                <v-btn
                  @click="
                    () => {
                      this.$router.push(
                        `/${this.authentication?.user?.role}/account/update-password`
                      );
                    }
                  "
                  rounded
                  type="submit"
                  color="#00bd7e"
                  theme="dark"
                  >Đổi mật khẩu</v-btn
                >
              </v-row>
            </div>
          </div>
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
import { apiCallerGet } from "@/services/teacher";
import { getUserInfo } from "@/services";
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
      dataBtnLeft: {
        text: "Chỉnh sửa",
        preIcon: "",
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
    };
  },
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
    ...mapState(toastStore, ["updateToast"]),
  },
  mounted() {
    this.dataProfile = this.authentication.user;
    this.fetchDataProfile();
  },
  methods: {
    async fetchDataProfile() {
      const userInfoRes = await getUserInfo(
        JSON.parse(localStorage?.getItem("accessToken"))?.token
      );

      if (userInfoRes.success) {
        userInfoRes.data.role = userInfoRes.data.role.toLowerCase();
        let avatar = userInfoRes.data.avatar
          ? userInfoRes.data.avatar
          : this.authentication.user.avatar;

        userInfoRes.data.avatar = avatar;

        this.updateAuth({ user: userInfoRes.data });
        this.dataProfile = userInfoRes.data;
      }
    },
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
      if (type == "gender") {
        return data[type] ? "Nam" : "Nữ";
      } else {
        return data[type] || "...";
      }
    },
  },
};
</script>

<style>
@import "./Account.style.scss";
</style>
