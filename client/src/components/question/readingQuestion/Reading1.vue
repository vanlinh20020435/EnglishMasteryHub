<template>
  <div class="d-flex flex-column">
    <h3 class="font-semi-bold text-lg">Bài {{ indexQuestion + 1 }}: {{ dataQuestion.title }}</h3>
    <v-col class="mt-5 mb-5">
      <h3 class="font-semi-bold text-lg text-center pb-1">{{ dataQuestion.description }}</h3>
      <v-col class="d-flex flex-column align-center pb-10">
        <div class="w-75 ">{{ dataQuestion.content }}</div>
      </v-col>

      <v-col class="d-flex pt-8" v-for="(subQuestion, index) in dataQuestion?.subQuestions" :key="index">
        <div class="d-flex flex-column w-100">
          <div class="d-flex align-center pb-2">
            <span class="mr-3 font-semi-bold">{{ index + 1 }}.</span>
            <div>{{ subQuestion?.content }}</div>
          </div>


          <v-radio-group v-model="selectedOptions[index]">
            <div class="d-flex align-center flex-wrap">
              <v-col cols="12" class="pt-1 pb-1" v-for="(optionSub, indexOption) in subQuestion?.options" :key="indexOption">
                <span>
                  <v-radio @input="handleRadioInput(optionSub?.option, index)" color="#00bd7e"
                    :label="optionSub?.option" :value="optionSub?.option"></v-radio>
                </span>
              </v-col>
            </div>
          </v-radio-group>
        </div>
      </v-col>
    </v-col>
  </div>
</template>


<script>
import { formatOriginalText } from '@/base/helper.js';
export default {
  name: 'Reading1Question',
  data() {
    return {
      selectedOptions: [],
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
      rightAnswer: false,
      score: 0,
      defaultScore: 1,
    }))

    this.questionResults.push(...subquestionResults)
  },
  methods: {
    handleRadioInput(value, subQuestionIndex) {
      let subQuestionSelected = this.dataQuestion.subQuestions?.[subQuestionIndex];
      let subQuestionInResult = this.questionResults.find(item => item?.questionId == subQuestionSelected?.questionId);

      const matchFound = subQuestionSelected.answers.some(answerObj => {
        const answer = formatOriginalText(answerObj?.answer);
        return formatOriginalText(value) == answer;
      });

      if (matchFound) {
				subQuestionInResult.rightAnswer = true;
        subQuestionInResult.score = 1;
      } else {
				subQuestionInResult.rightAnswer = false;
        subQuestionInResult.score = 0;
      }
			subQuestionInResult.answers[0] =  value;

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