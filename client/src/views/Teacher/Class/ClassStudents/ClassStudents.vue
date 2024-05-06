<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        title="Thành viên"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-col
        style="max-height: 90%; overflow-y: auto"
        class="d-flex flex-column class-list class-students align-center"
      >
        <div
          v-if="dataStudents.length > 0"
          style="width: 60%; min-width: 28rem; max-width: 45rem; flex: 1"
          class="d-flex flex-column"
        >
          <h3 class="font-bold">Danh sách lớp</h3>

          <v-col class="pa-8 student-list box-shadow mt-6">
            <v-col
              v-for="(student, index) in dataStudents"
              :key="index"
              class="pa-3 pt-5 pb-8 mb-3 d-flex student-item"
            >
              <v-row class="d-flex flex-row w-100 justify-space-between">
                <div class="d-flex flex-row align-center">
                  <div class="d-flex align-center justify-center font-bold">
                    {{ index + 1 }}
                  </div>

                  <div
                    class="ml-5 mr-5 d-flex align-center justify-center student-avt"
                  >
                    <img
                      :src="
                        student?.avatar
                          ? student?.avatar
                          : '/src/assets/images/avatar_student_male.jpg'
                      "
                      alt="Avatar Student"
                      class="cover_image"
                    />
                  </div>

                  <div class="student-name">
                    {{ student.name }}
                  </div>
                </div>

                <div
                  @click="() => handleOpenLockPopup(student)"
                  class="cursor-pointer student-lock d-flex align-center justify-center"
                >
                  <img
                    :src="`/src/assets/images/icon/${
                      student?.status == 1 ? 'ico_lock' : 'ico_unlock'
                    }.png`"
                    alt="Icon Lock"
                    class="contain_image"
                  />
                </div>
              </v-row>
            </v-col>
          </v-col>
        </div>
        <v-row v-else-if="!isLoading" style="height: 90%">
          <v-col>
            <div class="d-flex flex-column align-center class-empty w-100">
              <img
                src="@/assets/images/img_empty_students.jpg "
                alt="Img Empty Class"
              />
              <h3 class="d-flex d-flex flex-column align-center">
                Lớp chưa có thành viên nào.
                <br />
                <strong>Hãy liên hệ Admin để bắt đầu! </strong>
              </h3>
            </div>
          </v-col>
        </v-row>
      </v-col>
    </v-container>
    <PopUpYesNo
      :msg="`Bạn có chắc chắn muốn ${
        studentSelected.status ? 'khóa' : 'mở khóa'
      } học sinh '${studentSelected?.name}'?`"
      :visible="studentSelected?.studentId"
      :handleClickYes="handleLockStudent"
      :handleClickNo="() => (studentSelected = {})"
    />
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet, apiCallerPut } from "@/services/teacher";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";

export default {
  name: "ClassStudents",
  components: { HeaderTitle, PopUpYesNo },
  data() {
    return {
      dataStudents: [],
      isLoading: true,
      studentSelected: {},
    };
  },
  props: {},
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
  },
  mounted() {
    this.fetchDataStudents();
  },
  methods: {
    async fetchDataStudents() {
      let urlAPI = "/api/class/" + this.$route.params.id + "/students/get-all";

      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataStudents = result?.data;
      }
      this.isLoading = false;
    },
    handleGoBack() {
      this.$router.go(-1);
    },
    handleOpenLockPopup(student) {
      this.studentSelected = student;
    },
    async handleLockStudent() {
      const updateStatus = this.studentSelected?.status ? 0 : 1;
      let urlAPI =
        "/api/students/" +
        this.studentSelected?.studentId +
        "/update-status?status=" +
        updateStatus;
      const result = await apiCallerPut(urlAPI);

      if (result?.success) {
        this.dataStudents.forEach((student) => {
          if (student.studentId == this.studentSelected.studentId) {
            student.status = !this.studentSelected.status;
          }
        });
      }

      this.studentSelected = {};
    },
  },
};
</script>

<style>
@import "./ClassStudents.style.scss";
@import "../Class.style.scss";
</style>
