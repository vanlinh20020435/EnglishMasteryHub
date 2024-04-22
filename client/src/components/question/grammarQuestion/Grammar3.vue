<template>
  <div class="d-flex flex-column">
    <h3 class="font-semi-bold text-lg">Bài {{ indexQuestion + 1 }}: {{ dataQuestion.title }}</h3>

    <v-col>
      <v-col class="d-flex w-100" v-for="(subQuestion, index) in dataQuestion.subQuestions" :key="index">
        <div class="d-flex flex-column w-100">
          <div class="d-flex align-center pb-2">
            <span class="mr-3 font-semi-bold">{{ index + 1 }}.</span>
            <div>{{ subQuestion?.content }}</div>
          </div>

          <div class="d-flex align-center">
            <v-icon size="x-large" class="pr-5 pl-3">mdi-pencil-circle-outline</v-icon>
            <div class="pr-2 font-semi-bold">{{ subQuestion?.description }}</div>

            <input @change="(event) => handleChangeInputAnswer(index, event)" type="text" placeholder="..."
              class="flex-1-1 input-answer-grammar3 pt-1 pb-1">
          </div>
        </div>
      </v-col>
    </v-col>
  </div>
</template>


<script>
import { formatOriginalText } from '@/base/helper.js';

export default {
  name: 'Grammar3Question',
  data() {
    return {

    }
  },
  props: {
    dataQuestion: Object,
    indexQuestion: Number,
    questionResults: Object
  },
  mounted() {
    const subquestionResults = this.dataQuestion.subQuestions.map(item => ({
      questionId: item.questionId,
      answers: [],
      rightAnswer: null,
      score: 0,
      defaultScore: 1,
    }))

    this.questionResults.push(...subquestionResults)
  },
  methods: {
    handleChangeInputAnswer(subQuestionIndex, event) {
      const newValue = event?.target?.value;
      let subQuestionSelected = this.dataQuestion.subQuestions?.[subQuestionIndex];
      let subQuestionInResult = this.questionResults.find(item => item?.questionId == subQuestionSelected?.questionId);

      const matchFound = subQuestionSelected.answers.some(answerObj => {
        const answer = formatOriginalText(answerObj?.answer);
        return formatOriginalText(newValue) == answer;
      });

      if (matchFound) {
        subQuestionInResult.score = 1;
      } else {
        subQuestionInResult.score = 0;
      }
			subQuestionInResult.answers[0] =  newValue;
      const indexToUpdate = this.questionResults.findIndex(item => item.questionId === subQuestionInResult.questionId);
      if (indexToUpdate !== -1) {
        this.questionResults[indexToUpdate] = subQuestionInResult;
      } else {
        this.questionResults.push(subQuestionInResult);
      }
    }
  }
}
</script>

<style>
.input-answer-grammar3 {
  outline: none;
  min-width: 5rem;
  max-width: 80%;
  color: #8e1c76;
}
.input-answer-grammar3:focus {
  border-bottom: 1px solid #999;
}
</style>