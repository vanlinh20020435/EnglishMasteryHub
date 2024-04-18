<template>
  <div class="do-exam-container" v-if="test.testId">
    <v-card :elevation="8" style="margin-bottom: 16px">
      <v-card-title
        >{{ test.testName }} -
        <span style="color: #333; font-size: 14px"
          >Tổng số câu hỏi: {{ test.totalQuestions }}</span
        ></v-card-title
      >
      <v-card-text class="d-flex">
        <v-btn
          @click="back"
          color="#fde74c"
          icon="mdi-arrow-left"
          size="x-small">
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn @click="openSubmit" color="success" style="margin-right: 8px">
          Nộp bài
        </v-btn>
      </v-card-text>
    </v-card>
    <v-card :elevation="8">
      <v-form>
        <v-card-text>
          <v-row>
            <v-col
              md="12"
              v-for="(question, index) in test.questions"
              :key="question.questionId">
              <Question
                :question="{ ...question }"
                :questionResults="answersForm.questionResults"
                :indexQuestion="index" />
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
    </v-card>
    <div
      v-if="position[1] > 100"
      class="position-fixed"
      style="bottom: 16px; right: 16px">
      <v-btn @click="openSubmit" color="success" style="margin-right: 8px">
        Nộp bài
      </v-btn>
      <v-btn @click="scrollToTop" icon="mdi-menu-up" color="success"> </v-btn>
    </div>
    <div class="timer-tick">
      <TimerTick :seconds="test?.time * 60 || 30 * 60" :onEndTimerTick="onEndTimerTick"></TimerTick>
    </div>
    <v-dialog v-model="isOpenSubmit" max-width="500px">
      <v-card v-if="isLoadingSubmit">
        <v-card-text class="d-flex justify-center">
          <v-progress-circular
            :size="60"
            :width="6"
            color="success"
            indeterminate></v-progress-circular>
        </v-card-text>
      </v-card>
      <v-card v-else>
        <v-card-title>Xác nhận nộp bài</v-card-title>
        <v-card-text> Bạn có chắc chắn muốn nộp bài? </v-card-text>
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
  </div>
  <div v-else>no test</div>
</template>

<script>
import windowScroll from '@/utils/windowScroll';
import Question from '@/components/question/index.vue';
import {
  authenticationRole,
  studentStore,
  testStore,
  toastStore,
} from '@/stores';
import { submitExam } from '@/services';
import { mapState } from 'pinia';
import TimerTick from '@/components/timerTick/TimerTick.vue';

export default {
  mixins: [windowScroll('position')],
  components: {
    Question,
    TimerTick
  },
  data: () => ({
    answersForm: {
      score: 0,
      testDefaultScore: 0,
      time: 0,
      questionResults: [],
    },
    isOpenSubmit: false,
    isLoadingSubmit: false,
  }),
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(testStore, ['test', 'clearTest']),
    ...mapState(studentStore, ['student']),
    ...mapState(toastStore, ['updateToast']),
  },
  mounted() {
    window.addEventListener('beforeunload', this.confirmLeave);
    this.answersForm.classId = Number(this.student.class.classId);
    this.answersForm.testId = Number(this.$route.params.id);
    this.answersForm.studentId = Number(this.student.studentId);
  },
  methods: {
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
        this.updateToast('success', 'Nộp bài thành công!');
        this.$router.replace('/student/test');
      } else {
        this.updateToast('error', 'Nộp bài không thành công!');
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
        'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
      return 'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
    },
    onEndTimerTick() {
      this.confirmSubmit();
    }
  },
  beforeUnmount() {
    window.removeEventListener('DOMContentLoaded', this.confirmLeave);
    window.removeEventListener('beforeunload', this.confirmLeave);
  },
  beforeRouteLeave(to, from, next) {
    if (this.test.testId) {
      if (
        confirm('Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?') ==
        true
      ) {
        next();
      } else {
        next(false);
      }
    }
  },
  unmounted() {
    this.clearTest();
  },
};
</script>

<style>
@import "./DoExam.style.scss";
</style>
