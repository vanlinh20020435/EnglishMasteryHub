<template>
  <div class="d-flex flex-column">
    <h3 class="font-semi-bold">Bài {{ indexQuestion + 1 }}: {{ dataQuestion.title }}</h3>
    <v-col style="z-index: 3;">
      <v-col
        v-for="(subQuestion, index) in dataQuestion.subQuestions"
        :key="index"
      >
        <div class="font-weight-medium">
          <span class="font-weight-medium mr-3">{{ index + 1 }}.</span
          >{{ subQuestion?.content }}
        </div>
        <div style="white-space: pre-wrap" class="pt-3 pb-3">
          <div class="font-semi-bold">Gợi ý:</div> {{ subQuestion?.description }}
        </div>
        <v-textarea
          class="mt-3 field-writting-disabled"
          rows="10"
          max-rows="15"
          placeholder="Fill in your answer ..."
          variant="outlined"
          @input="(event) => handleChange(event, index)"
          :model-value="!!reviewExam ? this.answerStudent : ''"
          hide-details
          :readonly="!!reviewExam"
        ></v-textarea>
      </v-col>

      <v-col v-if="!!reviewExam" >
        <div class="d-flex align-center pt-5 pb-3">
          <h3 class="font-bold text-lg">{{this.reviewExam == 'teacherMark' ? 'CHẤM ĐIỂM BÀI WRITING': 'ĐIỂM BÀI WRITING:'}}</h3>
        </div>
        <v-col class="mark-writing-box d-flex align-center">
          <div class="mark-writing-header mr-5">
            <h4 class="font-semi-bold color-black ">ĐIỂM</h4>
          </div>
          <div class="mark-writing-input">
            <input
              v-if="!!dataQuestion.subQuestions?.[0]?.studentResult" 
              type="number"
              step="0.1" 
              min="0" 
              max="1" 
              class="font-bold"
              @input="(event) => handleChangeScoreDecimal(event)"
              v-model="scoreMark"
              :readonly="reviewExam !== 'teacherMark'"
            />
            <input 
              type="text" 
              class="font-bold" 
              readonly v-else
               value="Chờ chấm" 
            />
          </div>
      </v-col>
      </v-col>
    </v-col>
  </div>
</template>

<script>
import { formatOriginalText } from "@/base/helper.js";
export default {
  name: "Writing3Question",
  data() {
    return {
      answerStudent: '',
      subquestionResults: [],
      scoreMark: null,
    };
  },
  props: {
    dataQuestion: Object,
    questionResults: Object,
    indexQuestion: Number,
    reviewExam: String,
  },
  mounted() {
    console.log('dataQuestion ======', this.dataQuestion);
    this.subquestionResults = this?.dataQuestion?.subQuestions?.map((item) => {
      return (
        !!this.reviewExam ? {
          questionId: item.questionId,
          ...item.studentResult
        } : {
          questionId: item.questionId,
          answers: [],
          rightAnswer: null,
          score: 0,
          defaultScore: 1,
        }
      )
    });
    this.questionResults.push(...this.subquestionResults);
    this.scoreMark = this.subquestionResults?.[0]?.score;
    
    this.answerStudent = this.dataQuestion?.subQuestions?.[0]?.studentResult?.answers?.[0];
  },
  watch: {},
  methods: {
    normalizeText(text) {
      if (!text) return "";
      return text.toLowerCase().trim().replace(/\s+/g, " ");
    },
    handleChange(event, key) {
      let newValue = event.target.value;
      newValue = this.normalizeText(newValue);
      let subQuestion = this.dataQuestion.subQuestions[key];
      let questionResult = this.questionResults?.find(
        (q) => q.questionId === subQuestion?.questionId
      );
      questionResult.answers = [newValue];
    },
    isInteger(number) {
      return Number?.isInteger(number);
    },
     // Change Score Deci
    handleChangeScoreDecimal(e) {
      const value = parseFloat(e.target.value);
      this.subquestionResults.forEach((item) => {
        if (
          value <= 1 &&
          value.toString().length <= 4
        ) {
          item.score = value;
          this.scoreMark = value;
        } else if (value > 1) {
          item.score = 1;
          this.scoreMark = 1;
        } else if (isNaN(value) || value < 0) {
          item.score = 0;
          this.scoreMark = 0;
        }
        item.score = parseFloat(item.score?.toFixed(2));
        this.scoreMark = parseFloat(this.scoreMark?.toFixed(2));
      });
    }
  },
};
</script>

<style>
  .field-writting-disabled .v-field {
    opacity: 1;
  }

  .mark-writing-box {
    background-color: var(--primary-color);
    border-radius: 0.25rem;
    padding-left: 15%;
    padding-right: 15%;
  }

  .mark-writing-header {
    padding: 12px;
    background-color: #FFFFFF;
    border-radius: 2rem;
    flex: 1.5;
    padding-left: 24px;
  }

  .mark-writing-input {
    flex: 1;
    background-color: #FFFFFF;
    border-radius: 2rem;
  }

  .mark-writing-input input {
    width: 100%;
    background-color: #FFFFFF;
    padding: 12px;
    border-radius: 2rem;
    text-align: center;
    outline: none;
    font-size: 1.125rem !important;
    color: #be1e2d;
  }

  /* Chrome, Safari, Edge, Opera */
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }

  /* Firefox */
  input[type=number] {
    -moz-appearance: textfield;
  }

</style>
