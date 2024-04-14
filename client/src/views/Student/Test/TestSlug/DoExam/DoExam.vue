<template>
  <div v-if="test.testId">
    <v-card :elevation="8" style="margin-bottom: 16px;">
      <v-card-title>{{ test.testName }}</v-card-title>
      <v-card-text>
        <v-row>
          <v-col md="6">Số câu hỏi: {{ test.totalQuestions }}</v-col>
          <v-col md="6"> </v-col>
        </v-row>
      </v-card-text>
    </v-card>
    <v-card :elevation="8">
      <v-form>
        <v-card-text>
          <v-row>
            <v-col md="12" v-for="question in test.questions" :key="question.questionId">
              <Question :question="question" :questionResults="answersForm.questionResults" />
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
    </v-card>
    <v-btn @click="scrollToTop" v-if="position[1] > 200" class="position-fixed" style="bottom: 16px; right: 16px"
      icon="mdi-menu-up" color="success">
    </v-btn>
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
    }
  }),
  computed: {
    ...mapState(testStore, ['test', 'clearTest']),
  },
  mounted() {
    window.addEventListener('beforeunload', this.confirmLeave);
  },
  methods: {
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
