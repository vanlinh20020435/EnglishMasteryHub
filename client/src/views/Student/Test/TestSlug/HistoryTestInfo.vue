<template>
  <div class="do-exam-container">
    <div>
      <v-card :elevation="8" style="margin-bottom: 16px" class="pt-3">
        <v-card-title>{{ dataTestInfor?.testName }} -
          <span style="color: #333; font-size: 14px">Tổng số câu hỏi: {{ dataTestInfor?.totalQuestions }}</span>
        </v-card-title>
        <v-card-subtitle>
          Thời gian làm bài: {{ dataTestInfor?.time }} phút
        </v-card-subtitle>
        <v-card-text class="d-flex">
          <v-spacer></v-spacer>
        </v-card-text>
      </v-card>
      <v-card class="do-exam-content" :elevation="8">
        <v-form>
          <v-card-text>
            <v-row>
              <v-col md="12" v-for="(question, index) in dataExamStudent?.test.questions" :key="question?.id">
                <Question :question="{ ...question }" :questionResults="answersForm.questionResults"
                  :indexQuestion="index" reviewExam="studentHistory" />
              </v-col>
            </v-row>
          </v-card-text>
        </v-form>
        <div style="
              position: absolute;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
            "></div>
      </v-card>
    </div>
  </div>
</template>

<script>
import { apiCallerGet } from "@/services/teacher";
import Question from "@/components/question/index.vue";
import { mapState } from "pinia";
import {
  authenticationRole,
  studentStore,
  testStore,
  toastStore,
} from '@/stores';


export default {
  name: 'HistoryTestInfo',
  components: {
    Question,
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
      examId: this.$route?.params?.examId,
      dataTestInfor: {},
    };
  },
  computed: {
    ...mapState(authenticationRole, ["authentication"]),
    ...mapState(toastStore, ["updateToast"]),
    ...mapState(studentStore, ['student']),
  },
  mounted() {
    this.fetchDataExamStudent();
  },
  methods: {
    async fetchDataExamStudent() {
      this.isLoading = true;
      let urlAPI =
        "/api/test-result?classId=" +
        this.student.class.classId +
        "&testId=" +
        this.examId +
        "&studentId=" +
        this.student.studentId;

      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataExamStudent = result.data?.[result.data.length - 1];
        this.dataTestInfor = result.data?.[0]?.test;
      }
      this.isLoading = false;
    },
  }
}
</script>