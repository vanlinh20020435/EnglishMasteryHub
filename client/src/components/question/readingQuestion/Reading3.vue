<template>
  <div class="d-flex flex-column">
    <h3 class="font-semi-bold text-lg">Bài {{ indexQuestion + 1 }}: {{ dataQuestion.title }}</h3>
    <v-col class="mt-5 mb-5">
      <h3 class="font-semi-bold text-lg text-center pb-1">{{ dataQuestion.description }}</h3>
      <v-col class="d-flex flex-column align-center pb-10">
        <div class="w-75 " v-html="replacedNumberQuestionUnderline(dataQuestion.content, indexQuestion)"></div>
      </v-col>

      <v-col class="d-flex flex-column ">
        <v-col class="d-flex pt-4" v-for="(subQuestion, index) in dataQuestion?.subQuestions" :key="index">
        <div class="d-flex flex-column w-100">
          <div class="d-flex align-center pb-2">
            <span class="mr-3 font-semi-bold">{{ index + 1 }}.</span>
            <div class="font-semi-bold">Number {{ index + 1 }}</div>
          </div>


          <v-radio-group hide-details v-model="selectedOptions[index]">
            <div class="d-flex align-center flex-wrap">
              <v-col cols="3" class="pt-1 pb-1" v-for="(optionSub, indexOption) in subQuestion?.options"
                :key="indexOption">
                <span>
                  <v-radio 
                    @input="handleRadioInput(optionSub?.option, index)" 
                    :color="!!this.reviewExam ? subQuestion?.studentResult?.rightAnswer ? '#009444' : '#be1e2d' : '#00bd7e'"
                    :label="optionSub?.option" 
                    :value="optionSub?.option"
                    :readonly="!!reviewExam"
                    :class="!!this.reviewExam ? !!subQuestion?.studentResult?.rightAnswer ? 'checkboxRight' : 'checkboxWrong' : ''"
                  ></v-radio>
                </span>
              </v-col>
            </div>
          </v-radio-group>
        </div>
      </v-col>
      </v-col>
    </v-col>
  </div>
</template>


<script>
import { formatOriginalText } from '@/base/helper.js';
export default {
  name: 'Reading3Question',
  data() {
    return {
      selectedOptions: [],
    }
  },
  props: {
    dataQuestion: Object,
    indexQuestion: Number,
    questionResults: Object,
    reviewExam: String,
  },
  mounted() {
    const subquestionResults = this?.dataQuestion?.subQuestions?.map((item) => {
      !!this.reviewExam && this.selectedOptions.push(item?.studentResult?.answers?.[0]);
      return (
        !!this.reviewExam ? {
          questionId: item.questionId,
          ...item.studentResult
        } : {
          questionId: item.questionId,
          answers: [],
          rightAnswer: false,
          score: 0,
          defaultScore: 1,
        }
      )
    });
    this.questionResults.push(...subquestionResults)
  },
  methods: {
    replacedNumberQuestionUnderline(sentence, index) {
      // Regular expression to match placeholders enclosed in curly braces {}
      const regex = /\{(.*?)\}/g;
      let replaced = sentence;
      let match;

      // Loop through each match and replace it with an input element
      while ((match = regex.exec(sentence)) !== null) {
        const placeholder = match[0];
        const inputPlaceholder = match[1];
        const htmlTextUnderline = `<span class="font-bold ">${inputPlaceholder.trim()} |  _________</span>`;
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