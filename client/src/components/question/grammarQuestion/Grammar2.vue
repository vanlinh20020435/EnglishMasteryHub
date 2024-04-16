<template>
  <div class="d-flex flex-column">
    <h3 class="font-semi-bold text-lg">Bài {{ indexQuestion + 1 }}: {{ dataQuestion.title }}</h3>
    <v-col>
      <v-col class="d-flex" v-for="(subQuestion, index) in dataQuestion.subQuestions" :key="index">
        <div class="d-flex flex-column w-100">
          <div class="d-flex align-center pb-2">
            <span class="mr-3 font-semi-bold">{{ index + 1 }}.</span>
            <div v-html="replacedTextUnderline(subQuestion?.content, index)"></div>
          </div>

          <v-radio-group v-model="selectedOptions[index]">
            <div class="d-flex align-center flex-wrap">
              <v-col cols="6" v-for="(optionSub, indexOption) in subQuestion.options" :key="indexOption">
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
  name: 'Grammar2Question',
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
      rightAnswer: null,
      score: 0,
      defaultScore: 1,
    }))

    this.questionResults.push(...subquestionResults)
  },
  watch: {
  },
  methods: {
    replacedTextUnderline(sentence, index) {
      // Regular expression to match placeholders enclosed in curly braces {}
      const regex = /\[(.*?)\]/g;
      let replaced = sentence;
      let match;

      // Loop through each match and replace it with an input element
      while ((match = regex.exec(sentence)) !== null) {
        const placeholder = match[0];
        const inputPlaceholder = match[1];
        const htmlTextUnderline = `<span class="text-underline-offset-6">${inputPlaceholder.trim()}</span>`;
        replaced = replaced.replace(placeholder, htmlTextUnderline);
      }

      // Convert the replaced HTML string to a DOM element
      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = replaced;


      // Return the replaced HTML string
      return tempDiv.innerHTML;
    },
    handleRadioInput(value, subQuestionIndex) {
      let subQuestionSelected = this.dataQuestion.subQuestions?.[subQuestionIndex];
      let subQuestionInResult = this.questionResults.find(item => item?.questionId == subQuestionSelected?.questionId);

      const matchFound = subQuestionSelected.answers.some(answerObj => {
        const answer = formatOriginalText(answerObj?.answer);
        return formatOriginalText(value) == answer;
      });

      if (matchFound) {
        subQuestionInResult.score = 1;
      } else {
        subQuestionInResult.score = 0;
      }

      subQuestionInResult.answers[0] = {
        ...subQuestionInResult.answers[0],
        answer: value,
      }

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
.v-selection-control .v-label {
  color: #000000;
  opacity: 1;
}
</style>