<template>
  <div class="d-flex flex-column">
    <h3 class="font-semi-bold text-lg">
      Bài {{ indexQuestion + 1 }}: {{ dataQuestion.title }}
    </h3>
    <v-col>
      <v-col
        class="d-flex"
        v-for="(subQuestion, index) in dataQuestion.subQuestions"
        :key="index"
      >
        <span class="mr-3 font-semi-bold">{{ index + 1 }}.</span>
        <div v-html="replacedSentence(subQuestion, index)"></div>
      </v-col>
    </v-col>
  </div>
</template>

<script>
import { formatOriginalText } from "@/base/helper.js";

export default {
  name: "Grammar1Question",
  data() {
    return {};
  },
  props: {
    dataQuestion: Object,
    indexQuestion: Number,
    questionResults: Object,
    reviewExam: Boolean,
  },
  mounted() {
    const subquestionResults = this.dataQuestion.subQuestions.map((item) => ({
      questionId: item.questionId,
      answers: [],
      rightAnswer: false,
      score: 0,
      defaultScore: 1,
    }));

    this.questionResults.push(...subquestionResults);

    window.handleChange = (event, subQuestionIndex) => {
      let newValue = event.target.value;
      let subQuestionSelected =
        this.dataQuestion.subQuestions?.[subQuestionIndex];
      let subQuestionInResult = this.questionResults.find(
        (item) => item?.questionId == subQuestionSelected?.questionId
      );

      const matchFound = subQuestionSelected.answers.some((answerObj) => {
        const answer = formatOriginalText(answerObj?.answer);
        return formatOriginalText(newValue) == answer;
      });

      if (matchFound) {
        subQuestionInResult.rightAnswer = true;
        subQuestionInResult.score = 1;
      } else {
        subQuestionInResult.rightAnswer = false;
        subQuestionInResult.score = 0;
      }

      subQuestionInResult.answers[0] = newValue;
      const indexToUpdate = this.questionResults.findIndex(
        (item) => item.questionId === subQuestionInResult.questionId
      );
      if (indexToUpdate !== -1) {
        this.questionResults[indexToUpdate] = subQuestionInResult;
      } else {
        this.questionResults.push(subQuestionInResult);
      }
    };
  },
  methods: {
    replacedSentence(subQuestion, index) {
			const sentence = subQuestion?.content;
      // Regular expression to match placeholders enclosed in curly braces {}
      const regex = /\{(.*?)\}/g;
      let replaced = sentence;
      let match;

      // Loop through each match and replace it with an input element
      while ((match = regex.exec(sentence)) !== null) {
        const placeholder = match[0];
        const inputPlaceholder = match[1];
        const inputHtml = 
				`<input type="text" ${this.reviewExam &&  'readOnly'} ${this.reviewExam && `value="${subQuestion?.studentResult?.answers?.[0]}"`}  class="input-answer font-semi-bold ${this.reviewExam ? subQuestion?.studentResult?.rightAnswer ? 'color-right' : 'color-wrong' : '' }" onchange="handleChange(event, ${index})" data-index="${index}"  />`;
        replaced = replaced.replace(placeholder, inputHtml);
      }

      // Convert the replaced HTML string to a DOM <element></element>
      const tempDiv = document.createElement("div");
      tempDiv.innerHTML = replaced;

      // Return the replaced HTML string
      return tempDiv.innerHTML;
    },
  },
};
</script>

<style>
.input-answer {
  border-bottom: 1px solid #797b7c;
  outline: none;
  text-align: center;
  min-width: 5rem;
}
</style>
