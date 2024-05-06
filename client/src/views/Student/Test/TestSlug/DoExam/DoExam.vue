<template>
  <div class="do-exam-container" v-if="_test.testId">
    <v-card v-if="isSubmitted">
      <v-card-title v-if="_test.requiresGrading">Bạn đã nộp bài thành công!</v-card-title>
      <v-card-title v-else>Điểm của bạn: {{ answersForm.score }}/{{ answersForm.testDefaultScore }}.</v-card-title>
      <v-card-subtitle>Tổng thời gian: {{ answersForm.time }} phút.</v-card-subtitle>
      <v-card-text class="d-flex"><v-spacer></v-spacer><v-btn @click="$router.push('/student/news')"
          color="success">Quay về trang
          chủ</v-btn></v-card-text>
    </v-card>
    <div v-else>
      <v-card :elevation="8" style="margin-bottom: 16px">
        <v-card-title>{{ _test.testName }} -
          <span style="color: #333; font-size: 14px">Tổng số câu hỏi: {{ _test.totalQuestions
            }}</span></v-card-title>
        <v-card-text class="d-flex">
          <v-btn @click="back" color="#fde74c" icon="mdi-arrow-left" size="x-small">
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn v-if="!preview" @click="openSubmit" color="success" style="margin-right: 8px">
            Nộp bài
          </v-btn>
          <v-card-title v-else style="color: orange;">Preview</v-card-title>
        </v-card-text>
      </v-card>
      <v-card class="do-exam-content" :elevation="8" style="position: relative;">
        <v-form>
          <v-card-text>
            <v-row>
              <v-col md="12" v-for="(question, index) in _test.questions" :key="question.questionId">
                <Question :question="{ ...question }" :questionResults="answersForm.questionResults"
                  :indexQuestion="index" />
              </v-col>
            </v-row>
          </v-card-text>
        </v-form>
        <div v-if="preview" style="position: absolute; top: 0; left: 0; width: 100%; z-index: 1;
        height: 100%;"></div>
      </v-card>
      <div v-if="position[1] > 100 && !preview" class="position-fixed" style="bottom: 16px; right: 16px">
        <v-btn @click="openSubmit" color="success" style="margin-right: 8px">
          Nộp bài
        </v-btn>
        <v-btn @click="scrollToTop" icon="mdi-menu-up" color="success"> </v-btn>
      </div>
      <div class="timer-tick" v-if="!preview">
        <TimerTick :seconds="test?.time * 60 || 30 * 60" :onEndTimerTick="onEndTimerTick"></TimerTick>
      </div>
    </div>
    <v-dialog v-model="isOpenSubmit" max-width="500px">
      <v-card v-if="isLoadingSubmit">
        <v-card-text class="d-flex justify-center">
          <v-progress-circular :size="60" :width="6" color="success" indeterminate></v-progress-circular>
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
import { submitExam, getTest } from '@/services';
import { mapState } from 'pinia';
import TimerTick from '@/components/timerTick/TimerTick.vue';

export default {
  mixins: [windowScroll('position')],
  components: {
    Question,
    TimerTick
  },
  props: {
    preview: Boolean
  },
  data: () => ({
    answersForm: {
      score: 0,
      testDefaultScore: 0,
      time: 0,
      questionResults: [],
    },
    _test: {},
    isOpenSubmit: false,
    isLoadingSubmit: false,
    isSubmitted: false
  }),
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(testStore, ['test', 'clearTest']),
    ...mapState(studentStore, ['student']),
    ...mapState(toastStore, ['updateToast']),
  },
  async mounted() {
    if (!this.preview) window.addEventListener('beforeunload', this.confirmLeave);
    if (this.test.testId) {
      this._test = this.test
    }
    else {
      const res = await getTest(
        this.authentication.accessToken.token,
        this.$route.params.id,
      );
      if (res.success) {
        this._test = res.data
      }
    }
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
        this.isSubmitted = true
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
    if (!this.preview) {
      window.removeEventListener('DOMContentLoaded', this.confirmLeave);
      window.removeEventListener('beforeunload', this.confirmLeave);
    }
  },
  beforeRouteLeave(to, from, next) {
    if (this._test.testId && !this.preview) {
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
