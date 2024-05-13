<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        :title="classData?.className"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-col
        style="max-height: 90%; overflow-y: auto; margin: 0 auto; overflow-x: hidden;"
        class="d-flex flex-column class-list class-detail w-100"
      >
        <v-card
          class="pa-4"
          style="
            border-radius: 1rem;
            width: 85%;
            min-height: 252px;
            margin: 0 auto;
          "
        >
          <v-row>
            <div class="pa-3 class-detail_img d-flex align-center">
              <img :src="getClassAvatar(classData)" alt="Img Class" />
            </div>

            <v-col class="pl-6 pr-5 d-flex flex-column class-detail_infor">
              <v-row class="pt-3 pb-3 d-flex align-center">
                <img
                  src="@/assets/images/icon/ico_school.png"
                  alt="Icon School"
                />
                <div class="class-detail_text">English Mastery Hub</div>
              </v-row>
              <v-row class="pt-3 pb-3 d-flex align-center">
                <img
                  src="@/assets/images/icon/ico_desc.png"
                  alt="Icon School"
                />
                <div class="class-detail_text">
                  {{ classData?.description }}
                </div>
              </v-row>
              <v-row class="pt-3 pb-3 d-flex align-center">
                <img
                  src="@/assets/images/icon/ico_schedule.png"
                  alt="Icon School"
                />
                <div class="class-detail_text">
                  {{ classData?.startDate }} - {{ classData?.endDate }}
                </div>
              </v-row>
              <v-row
                class="pt-3 pb-3 d-flex align-center justify-space-between"
              >
                <div style="flex: 1" class="d-flex align-center flex-row">
                  <img
                    src="@/assets/images/icon/ico_users.png"
                    alt="Icon School"
                  />
                  <div class="class-detail_text">
                    Học sinh: <strong>{{ classData?.totalStudent }}</strong>
                  </div>
                </div>

                <!-- <div
                  @click="handleViewStudents"
                  class="cursor-pointer d-flex align-center justify-center"
                >
                  <img
                    src="@/assets/images/icon/ico_arrow_right.png"
                    alt="Icon Arrow Right"
                  />
                </div> -->
              </v-row>
            </v-col>
          </v-row>
        </v-card>

        <div class="d-flex flex-row pa-5" style="margin: 0 auto; overflow-x: auto; max-width: 100%;">
          <div
            class="mt-0 pa-6"
            v-for="(action, index) in dataAction"
            :key="index"
          >
            <v-card
              @click="() => handleNavigateAction(action)"
              hover
              class="d-flex flex-column align-center justify-center class-detail_action"
              ><div
                class="d-flex align-center justify-center class-detail_action_img"
              >
                <img :src="action.img" alt="Icon Action" />
              </div>

              <div>{{ action?.title }}</div>
            </v-card>
          </div>
        </div>
      </v-col>
    </v-container>
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet } from "@/services/teacher";
import classDefaultImage from "@/assets/images/class.png";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import ico_tailieu from "@/assets/images/icon/ico_tailieu.png";
import ico_chambai from "@/assets/images/icon/ico_chambai.png";
import ico_giaobai from "@/assets/images/icon/ico_giaobai.png";
import ico_hocsinh from "@/assets/images/icon/ico_hocsinh.png";

export default {
  name: "DetailClassTeacher",
  components: {
    HeaderTitle,
  },
  data() {
    return {
      classData: {
        className: "",
        description: "",
      },
      dataAction: [
        {
          id: 1,
          img: ico_hocsinh,
          title: "Danh sách học sinh",
          path: "students",
        },
        {
          id: 2,
          img: ico_tailieu,
          title: "Tài liệu học tập",
          path: "study-document",
        },
        {
          id: 3,
          img: ico_chambai,
          title: "Bài kiểm tra đã giao",
          path: "mark-exam",
        },
        {
          id: 4,
          img: ico_giaobai,
          title: "Giao bài",
          path: "assign-exam",
        },
        // {
        //   id: 4,
        //   img: ico_baocao,
        //   title: "Báo cáo học tập",
        //   path: "",
        // },
      ],
    };
  },
  computed: {
    ...mapState(authenticationRole, ["authentication"]),
  },
  mounted() {
    this.fetchDataDetailClass();
  },
  methods: {
    async fetchDataDetailClass() {
      let urlAPI = "/api/class/" + this.$route.params.id;

      const result = await apiCallerGet(urlAPI);

      if (result?.data) {
        this.classData = result.data;
      }
    },
    handleGoBack() {
      this.$router.go(-1);
    },
    getClassAvatar(classData) {
      return classData && classData.avatar
        ? classData.avatar
        : classDefaultImage; // Return the class avatar or the default image
    },
    handleViewStudents() {
      this.$router.push(
        `/${this.authentication?.user?.role}/class/${this.$route.params.id}/students`
      );
    },
    handleNavigateAction(action) {
      this.$router.push(
        `/${this.authentication?.user?.role}/class/${this.$route.params.id}/` +
          action?.path
      );
    },
  },
};
</script>

<style>
@import "../Class.style.scss";
@import "./DetailClassTeacher.style.scss";
</style>
