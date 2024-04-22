<template>
	<div class="d-flex flex-column">
		<h3 class="font-semi-bold">{{ dataQuestion.title }}</h3>
		<v-col>
			<v-col v-for="(subQuestion, index) in dataQuestion.subQuestions" :key="index">
				<div class="font-weight-medium"><span class="font-weight-medium mr-3">{{ index + 1 }}.</span>{{ subQuestion?.content }}</div>
				<div style="white-space: pre-wrap">{{ subQuestion?.description }}</div>
				<v-textarea class="mt-3" placeholder="Fill in your answer ..." variant="outlined" @input="(event) => handleChange(event, index)"></v-textarea>
			</v-col>
		</v-col>
	</div>
</template>

<script>
import { formatOriginalText } from '@/base/helper.js';
export default {
	name: "Writing3Question",
	data() {
		return {
		};
	},
	props: {
		dataQuestion: Object,
		questionResults: Object
	},
	mounted() {
		const subquestionResults = this.dataQuestion?.subQuestions.map(item => ({
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
		normalizeText(text) {
			if(!text)
				return '';
			return text.toLowerCase().trim().replace(/\s+/g, ' ');
		},
		handleChange(event, key) {
			let newValue = event.target.value;
			newValue = this.normalizeText(newValue);
			let subQuestion = this.dataQuestion.subQuestions[key];
			let questionResult = this.questionResults?.find(q => q.questionId === subQuestion?.questionId);
			questionResult.answers = [newValue];

		}
	},
};
</script>
