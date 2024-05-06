<template>
  <v-card class="height-100 class-container">
    <v-container
      class="d-flex flex-column height-100 v-container__full"
      style="height: 92vh"
    >
      <HeaderTitle
        :title="
          'Thống kê bài: ' +
          (!!this.$route.query?.exam ? this.$route.query?.exam : '')
        "
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-col
        class="pt-0 d-flex flex-column class-list class-docs class-students align-center"
      >
        <v-col
          class="d-flex flex-column align-center"
          v-if="dataDashboard?.completed || dataDashboard?.incomplete"
        >
          <v-col class="pt-0 pb-5">
            <div
              v-if="!!dataChart().datasets[0].data.length"
              class="d-flex justify-center align-end"
            >
              <div class="d-flex flex-column">
                <Pie :data="dataChart()" :options="chartConfigOptions" />
              </div>
              <div class="d-flex flex-column pl-3">
                <div>Học sinh làm bài / Tổng số học sinh</div>
                <div class="text-center" style="font-size: 2.75rem">
                  <span
                    class="font-bold"
                    style="color: #00beb4; font-size: 2.75rem"
                    >{{ dataDashboard?.completed || 0 }}</span
                  >
                  /
                  <span
                    class="font-bold"
                    style="color: #ff7a00; font-size: 2.75rem"
                    >{{
                      dataDashboard?.completed + dataDashboard?.incomplete || 0
                    }}</span
                  >
                </div>
              </div>
            </div>
          </v-col>
          <div
            v-if="dataDashboard?.statistic?.length > 0"
            style="width: 80%; min-width: 28rem; max-width: 70rem; flex: 1"
            class="d-flex flex-column"
          >
            <v-col class="pa-8 pt-3">
              <v-card
                style="overflow: visible"
                v-for="(studentDashboard, index) in dataDashboard?.statistic"
                :key="index"
                class="cursor-pointer pa-3 pb-7 mb-5 pl-12 d-flex student-item docs-item box-shadow"
                @click="
                  () => {
                    studentDashboard.result?.id
                      ? this.$router.push({
                          path:
                            `/${this.authentication?.user?.role}/class/${this.classId}/` +
                            'mark-exam/' +
                            this.examId +
                            '/review/' +
                            studentDashboard?.student?.studentId,
                          query: { student: studentDashboard?.student?.name },
                        })
                      : null;
                  }
                "
              >
                <v-row
                  class="pl-5 pr-5 pt-3 d-flex flex-row w-100 justify-space-between"
                >
                  <div class="d-flex flex-row align-center w-100">
                    <div class="d-flex align-center justify-center img_type">
                      <img
                        :src="
                          getAvtUserMethod(studentDashboard?.student, 'student')
                        "
                        alt="Avatar Student"
                        style="border-radius: 50%"
                      />
                    </div>

                    <div
                      class="d-flex justify-space-between w-100 align-center"
                    >
                      <div class="d-flex flex-column">
                        <div
                          class="font-semi-bold text-lg ml-5 mr-5 d-flex align-center"
                        >
                          {{ studentDashboard?.student?.name }}
                        </div>
                        <div
                          v-if="studentDashboard?.result?.created"
                          class="ml-5 mr-5 d-flex align-center"
                        >
                          Nộp bài lúc: {{ studentDashboard?.result?.created }}
                        </div>
                      </div>

                      <div class="d-flex align-center">
                        <v-btn
                          disabled
                          :class="
                            !studentDashboard?.result
                              ? 'btn-error'
                              : !!studentDashboard?.result?.requiresGrading
                              ? 'btn-warning'
                              : 'btn-delete-linear'
                          "
                          theme="dark"
                        >
                          <span class="color-white font-semi-bold">{{
                            textScore(studentDashboard?.result)
                          }}</span>
                        </v-btn>
                      </div>
                    </div>
                  </div>
                </v-row>
              </v-card>
            </v-col>
          </div>
        </v-col>

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
  </v-card>
</template>


<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet, apiCallerPost } from "@/services/teacher";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
import { getAvtUser } from "@/base/helper";

import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie } from "vue-chartjs";

ChartJS.register(ArcElement, Tooltip, Legend);

export default {
  name: "MarkExamDashboard",
  components: {
    HeaderTitle,
    PopUpYesNo,
    Pie,
  },
  data() {
    return {
      dataDashboard: {},
      isLoading: false,
      classId: this.$route.params?.id,
      examId: this.$route.params?.examId,
      chartConfigOptions: {
        responsive: true,
        maintainAspectRatio: false,
        aspectRatio: 1.5,
        plugins: {
        legend: {
          position: 'bottom'
          }
      }
      },
    };
  },
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
  },
  mounted() {
    this.fetchDataDashboardExam();
  },
  methods: {
    async fetchDataDashboardExam() {
      this.isLoading = true;
      let urlAPI =
        "/api/test-result/statistic?classId=" +
        this.classId +
        "&testId=" +
        this.examId;
      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataDashboard = result.data;
      }

      this.isLoading = false;
    },
    handleGoBack() {
      this.$router.go(-1);
    },
    dataChart() {
      return {
        labels: ["Học sinh làm bài", "Học sinh chưa làm bài"],
        datasets: [
          {
            backgroundColor: ["#00beb4", "#e77865"],
            data: [
              this?.dataDashboard?.completed,
              this?.dataDashboard?.incomplete,
            ],
          },
        ],
      };
    },
    getAvtUserMethod(item) {
      return getAvtUser(item, "student");
    },
    textScore(result) {
      return result == null
        ? "Chưa làm"
        : !!result?.requiresGrading
        ? "Chờ chấm"
        : result?.score?.toString() +
          "/" +
          result?.testDefaultScore?.toString() +
          " Điểm";
    },
  },
};
</script>


<style>
@import "@/views/Teacher/Class/StudyDocuments/StudyDocuments.style.scss";
</style>