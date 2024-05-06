<template>
	<div class="d-flex flex-column">
		<h3 class="font-semi-bold">Bài {{ indexQuestion + 1 }}: {{ dataQuestion.title }}</h3>
		<v-col>
			<v-col v-for="(subQuestion, index) in dataQuestion.subQuestions" :key="index">
				<div><span class="mr-3">{{ index + 1 }}.</span>{{ subQuestion?.content }}</div>
				<v-text-field class="field-writting" density="compact" variant="underlined" @input="(event) => handleChange(event, index)" hide-details></v-text-field>
			</v-col>
		</v-col>
	</div>
</template>

<script>
import { formatOriginalText } from '@/base/helper.js';
export default {
	name: "Writing1Question",
	data() {
		return {
		};
	},
	props: {
		dataQuestion: Object,
		questionResults: Object,
		indexQuestion: Number,
	},
	mounted() {
		const subquestionResults = this.dataQuestion?.subQuestions.map(item => ({
			questionId: item.questionId,
			answers: [],
			rightAnswer: false,
			score: 0,
			defaultScore: 1,
		}))
		this.questionResults.push(...subquestionResults)
	},
	watch: {
	},
	methods: {
		normalizeText(text) {
			if(!text)
				return '';
			return text.toLowerCase().trim().replace(/\s+/g, ' ');
		},
		handleChange(event, subQuestionIndex) {
			let newValue = event.target.value;
      let subQuestionSelected = this.dataQuestion.subQuestions?.[subQuestionIndex];
      let subQuestionInResult = this.questionResults.find(item => item?.questionId == subQuestionSelected?.questionId);

			const matchFound = subQuestionSelected.answers.some(answerObj => {
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
			subQuestionInResult.answers[0] =  newValue;
			const indexToUpdate = this.questionResults.findIndex(item => item.questionId === subQuestionInResult.questionId);
      if (indexToUpdate !== -1) {
        this.questionResults[indexToUpdate] = subQuestionInResult;
      } else {
        this.questionResults.push(subQuestionInResult);
      }
		}
	},
};
</script>

<style>
	.field-writting input {
		font-weight: 600 !important;
		color: #8e1c76;
	}
</style>