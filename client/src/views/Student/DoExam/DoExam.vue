<template>
  <div v-if="test.testId">
    <v-card>
      <v-card-title>{{ test.testName }}</v-card-title>
      <v-card-text>
        <v-row>
          <v-col md="6">Số câu hỏi: {{ test.totalQuestions }}</v-col>
        </v-row>
      </v-card-text>
    </v-card>
    <v-card>
      <v-form>
        <v-card-text>
          <v-row>
            <v-col md="12" v-for="question in test.questions" :key="question.questionId">
              <Question :question="question" />
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
    </v-card>
  </div>
  <div v-else>no test</div>
</template>

<script>
import Question from '@/components/Question/index.vue'
import { authenticationRole, studentStore, testStore, toastStore } from '@/stores';
import { mapState } from 'pinia';
export default {
  components: {
    Question
  },
  computed: {
    ...mapState(testStore, ['test', 'clearTest']),
  },
  mounted() {
    if (this.test.testId) {
      window.addEventListener('beforeunload', this.confirmLeave);
      console.log(this.test);
    } else {
      this.$router.replace('/student')
    }
  },
  methods: {
    confirmLeave(event) {
      event.preventDefault();
      event.returnValue = 'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
      return 'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
    }
  },
  beforeUnmount() {
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
};
</script>

<!-- <style>
@import "./DoExam.style.scss";
</style> -->
