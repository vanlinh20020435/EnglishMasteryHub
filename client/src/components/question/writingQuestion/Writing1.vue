<template>
	<div class="d-flex flex-column">
		<h3 class="font-semi-bold">{{ dataQuestion.title }}</h3>
		<v-col>
			<v-col v-for="(subQuestion, index) in dataQuestion.subQuestions" :key="index">
				<div class="font-weight-medium"><span class="font-weight-medium mr-3">{{ index + 1 }}.</span>{{ subQuestion?.content }}</div>
				<v-text-field density="compact" variant="underlined" @input="(event) => handleChange(event, index)" hide-details></v-text-field>
			</v-col>
		</v-col>
	</div>
</template>

<script>
export default {
	name: "Writing1Question",
	data() {
		return {
		};
	},
	props: {
		dataQuestion: Object,
		questionResults: Object
	},
	mounted() {
		const subquestionResults = this.question.subQuestions.map(item => ({
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
			let isMatching = false;
			let subQuestion = this.dataQuestion.subQuestions[key];
			let answers = subQuestion.answers;
			let newValue = event.target.value;
			newValue = this.normalizeText(newValue);
			for (let i = 0; i < answers.length; i++) {
				if (this.normalizeText(answers[i].answer) === newValue) {
					isMatching = true;
					break;
				}
			}

			let questionResult = this.questionResults.find(q => q.questionId === subQuestion.id);
			questionResult.answers = [newValue];
			questionResult.rightAnswer = isMatching;
			questionResult.score = isMatching ? 1 : 0;

		}
	},
};
</script>
