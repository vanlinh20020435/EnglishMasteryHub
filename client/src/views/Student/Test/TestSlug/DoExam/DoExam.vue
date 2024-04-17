<template>
  <div class="do-exam-container" v-if="test.testId">
    <v-card :elevation="8" style="margin-bottom: 16px;">
      <v-card-title>{{ test.testName }} - <span style="color: #333; font-size: 14px;">Tổng số câu hỏi: {{
        test.totalQuestions }}</span></v-card-title>
      <v-card-text class="d-flex">
        <v-btn @click="openSubmit" color="#fde74c" icon="mdi-arrow-left" size="x-small">
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn @click="openSubmit" color="success" style="margin-right: 8px;">
          Nộp bài
        </v-btn>
      </v-card-text>
    </v-card>
    <v-card :elevation="8">
      <v-form>
        <v-card-text>
          <v-row>
            <v-col md="12" v-for="(question, index) in test.questions" :key="question.questionId">
              <Question :question="{ ...question }" :questionResults="answersForm.questionResults" :indexQuestion="index" />
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
    </v-card>
    <div v-if="position[1] > 100" class="position-fixed" style="bottom: 16px; right: 16px">
      <v-btn @click="openSubmit" color="success" style="margin-right: 8px;">
        Nộp bài
      </v-btn>
      <v-btn @click="scrollToTop" icon="mdi-menu-up" color="success">
      </v-btn>
    </div>
    <v-dialog v-model="isOpenSubmit" max-width="500px">
      <v-card>
        <v-card-title>Xác nhận nộp bài</v-card-title>
        <v-card-text>
          Bạn có chắc chắn muốn nộp bài?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="tonal" @click="() => (isOpenSubmit = false)">
            Hủy
          </v-btn>
          <v-btn color="success" variant="flat" @click="confirmSubmit">
            Đồng ý
          </v-btn>
        </v-card-actions>
      </v-card></v-dialog>
  </div>
  <div v-else>no test</div>
</template>

<script>
import windowScroll from '@/utils/windowScroll'
import Question from '@/components/question/index.vue'
import { authenticationRole, studentStore, testStore, toastStore } from '@/stores';
import { mapState } from 'pinia';
export default {
  mixins: [windowScroll('position')],
  components: {
    Question
  },
  data: () => ({
    answersForm: {
      score: 0,
      testDefaultScore: 0,
      time: 0,
      classId: 0,
      testId: 0,
      studentId: 0,
      questionResults: []
    },
    isOpenSubmit: false
  }),
  computed: {
    ...mapState(testStore, ['test', 'clearTest']),
  },
  mounted() {
    window.addEventListener('beforeunload', this.confirmLeave);
  },
  methods: {
    confirmSubmit() {
      console.log(this.answersForm.questionResults);
    },
    openSubmit() {
      this.isOpenSubmit = true
    },
    createQuestionResult(data, defaultScore) {
      let qr = {}
      qr.questionId = data.questionId
      qr.defaultScore = defaultScore || 1
      return qr
    },
    confirmLeave(event) {
      event.preventDefault();
      event.returnValue = 'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
      return 'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
    }
  },
  beforeUnmount() {
    window.removeEventListener('DOMContentLoaded', this.confirmLeave);
    window.removeEventListener('beforeunload', this.confirmLeave);
  },
  beforeRouteLeave(to, from, next) {
    if (this.test.testId) {
      if (confirm("Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?") == true) {
        next();
      } else {
        next(false);
      }
    }
  },
  caculateScore() {
    let score = 0
    this.questions.forEach(q => {
      if (q.type === 3 && q.skill === 'writting') {
        // writing 3
        score += q.score
      } else {
        // remain
        let total = 0
        q.subQuestions.forEach(sq => {
          total += sq.score
        });
        score += total
      }
    });
    return score;
  },
  unmounted() {
    this.clearTest()
  }
};
</script>

<!-- <style>
@import "./DoExam.style.scss";
</style> -->
