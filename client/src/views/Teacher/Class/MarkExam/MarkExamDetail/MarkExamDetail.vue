<template>
  <v-card class="height-100 class-container">
    <!-- <v-card v-if="isSubmitted">
      <v-card-title v-if="dataExamStudent?.requiresGrading">Bạn đã nộp bài thành công!</v-card-title>
      <v-card-title v-else>Điểm của bạn: {{ answersForm.score }}/{{ answersForm.testDefaultScore }}.</v-card-title>
      <v-card-subtitle>Tổng thời gian: {{ answersForm.time }} phút.</v-card-subtitle>
      <v-card-text class="d-flex"><v-spacer></v-spacer><v-btn @click="$router.push('/student/news')"
          color="success">Quay về trang
          chủ</v-btn></v-card-text>
    </v-card> -->
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        :title="
          'Học sinh: ' +
          (!!this.$route.query?.student ? this.$route.query?.student : '')
        "
        textBtn="Quay lại"
        :handleClickBtn="
          () => {
            this.$router.go(-1);
          }
        "
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>

      <v-col
        class="pt-0 class-list class-docs class-students align-center do-exam-container do-exam-container-mark"
      >
        <v-card :elevation="8" style="margin-bottom: 16px" class="pt-3" >
          <v-card-title
            >{{ dataTestInfor?.testName }} -
            <span style="color: #333; font-size: 14px"
              >Tổng số câu hỏi: {{ dataTestInfor?.totalQuestions }}</span
            >
          </v-card-title>
          <v-card-subtitle>
            Thời gian làm bài: {{ dataTestInfor?.time }} phút
          </v-card-subtitle>
          <v-card-text class="d-flex">
            <v-spacer></v-spacer>
            <v-btn
              v-if="dataExamStudent?.some((data) => data?.requiresGrading)"
              @click="openSubmit"
              color="success"
              style="margin-right: 8px"
            >
              Lưu bài
            </v-btn>
          </v-card-text>
        </v-card>
        <v-card class="do-exam-content" :elevation="8">
          <v-form>
            <v-card-text>
              <v-row>
                <v-col
                  md="12"
                  v-for="(question, index) in dataExamStudent?.[dataExamStudent?.length - 1]?.test.questions"
                  :key="question?.id"
                >
                  <Question
                    :question="{ ...question }"
                    :questionResults="answersForm.questionResults"
                    :indexQuestion="index"
                    reviewExam="teacherMark"
                  />
                </v-col>
              </v-row>
            </v-card-text>
          </v-form>
        </v-card>
        <div
          v-if="position?.[1] > 100"
          class="position-fixed"
          style="bottom: 16px; right: 16px"
        >
          <v-btn @click="openSubmit" color="success" style="margin-right: 8px">
            Lưu bài
          </v-btn>
          <v-btn @click="scrollToTop" icon="mdi-menu-up" color="success">
          </v-btn>
        </div>
        <!-- <div class="timer-tick timer-tick-mark">
          <TimerTick
            :seconds="test?.time * 60 || 30 * 60"
            :onEndTimerTick="onEndTimerTick"
          ></TimerTick>
        </div> -->
      </v-col>
      <v-dialog v-model="isOpenSubmit" max-width="500px">
        <v-card v-if="isLoadingSubmit">
          <v-card-text class="d-flex justify-center">
            <v-progress-circular
              :size="60"
              :width="6"
              color="success"
              indeterminate
            ></v-progress-circular>
          </v-card-text>
        </v-card>
        <v-card v-else>
          <v-card-title>Xác nhận lưu bài</v-card-title>
          <v-card-text> Bạn có chắc chắn muốn lưu bài? </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn variant="tonal" @click="() => (isOpenSubmit = false)">
              Hủy
            </v-btn>
            <v-btn color="success" variant="flat" @click="confirmSubmit">
              Đồng ý
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-container>
  </v-card>
</template>


<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet } from "@/services/teacher";
import windowScroll from "@/utils/windowScroll";
import Question from "@/components/question/index.vue";
import { authenticationRole, toastStore } from "@/stores";
import { submitExam } from "@/services";
import { mapState } from "pinia";
import TimerTick from "@/components/timerTick/TimerTick.vue";

export default {
  name: "MarkExamDetail",
  // mixins: [windowScroll("position")],
  components: {
    HeaderTitle,
    Question,
    TimerTick,
  },
  data() {
    return {
      dataExamStudent: null,
      answersForm: {
        score: 0,
        testDefaultScore: 0,
        time: 0,
        questionResults: [],
      },
      isOpenSubmit: false,
      isLoadingSubmit: false,
      isSubmitted: false,
      isLoading: false,
      classId: this.$route?.params?.id,
      examId: this.$route?.params?.examId,
      studentId: this.$route?.params?.studentId,
      dataTestInfor: {},
    };
  },
  computed: {
    ...mapState(authenticationRole, ["authentication"]),
    ...mapState(toastStore, ["updateToast"]),
  },
  mounted() {
    this.fetchDataExamStudent();
    // window.addEventListener("beforeunload", this.confirmLeave);
    this.answersForm.classId = Number(this.classId);
    this.answersForm.testId = Number(this.examId);
    this.answersForm.studentId = Number(this.studentId);
  },
  methods: {
    async fetchDataExamStudent() {
      this.isLoading = true;
      let urlAPI =
        "/api/test-result?classId=" +
        this.classId +
        "&testId=" +
        this.examId +
        "&studentId=" +
        this.studentId;

      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataExamStudent = result.data;
        this.dataTestInfor = result.data?.[0]?.test;

        console.log('dataExamStudent =====', this.dataExamStudent);
      }
      this.isLoading = false;
    },
    caculateScore() {
      this.answersForm.questionResults.forEach((qr) => {
        this.answersForm.testDefaultScore += qr.defaultScore;
        if (qr.rightAnswer) {
          this.answersForm.score += qr.defaultScore;
        }
      });
    },
    back() {
      this.$router.back();
    },
    async confirmSubmit() {
      this.isLoadingSubmit = true;
      this.caculateScore();
      this.answersForm.time = 10; // timer
      const res = await submitExam(
        this.authentication.accessToken.token,
        this.answersForm
      );
      if (res.success) {
        this.updateToast("success", "Nộp bài thành công!");
        this.isSubmitted = true;
      } else {
        this.updateToast("error", "Nộp bài không thành công!");
      }
      this.isLoadingSubmit = false;
      this.isOpenSubmit = false;
    },
    openSubmit() {
      this.isOpenSubmit = true;
    },
    createQuestionResult(data, defaultScore) {
      let qr = {};
      qr.questionId = data.questionId;
      qr.defaultScore = defaultScore || 1;
      return qr;
    },
    confirmLeave(event) {
      event.preventDefault();
      event.returnValue =
        "Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?";
      return "Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?";
    },
    onEndTimerTick() {
      this.confirmSubmit();
    },
  },
  // beforeRouteLeave(to, from, next) {
  //   if (this.dataExamStudent?.testId) {
  //     if (
  //       confirm("Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?") ==
  //       true
  //     ) {
  //       next();
  //     } else {
  //       next(false);
  //     }
  //   }
  // },
};
</script>

<style>
@import "@/views/Student/Test/TestSlug/DoExam/DoExam.style.scss";
@import "./MarkExamDetail.style.scss";
</style>