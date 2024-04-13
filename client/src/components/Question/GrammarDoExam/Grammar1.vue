<template>
	<div class="d-flex flex-column">
		<h3 class="font-semi-bold">{{ dataQuestion.title }}</h3>
		<v-col>
			<v-col class="d-flex" v-for="(subQuestion, index) in dataQuestion.subQuestions" :key="index">
				<span class="mr-3">{{ index + 1 }}.</span>
				<div v-html="replacedSentence(subQuestion?.content, index)"></div>
				<input type="text">
			</v-col>
		</v-col>
	</div>
</template>

<script>
export default {
	name: "Grammar1DoExam",
	data() {
		return {
		};
	},
	props: {
		dataQuestion: Object,
	},
	mounted() {
		// this.fetchDetailExam();

		window.handleChange = (event, key) => {
			let newValue = event.target.value;
			if (newValue == this.dataQuestion.subQuestions[key].answers[0].answer) {
				this.dataQuestion.subQuestions[key].score = 1;
			} else {
				this.dataQuestion.subQuestions[key].score = 0;
			}

		};
	},
	watch: {
	},
	methods: {
		replacedSentence(sentence, index) {

			// Regular expression to match placeholders enclosed in curly braces {}
			const regex = /\{(.*?)\}/g;
			let replaced = sentence;
			let match;

			// Loop through each match and replace it with an input element
			while ((match = regex.exec(sentence)) !== null) {
				const placeholder = match[0];
				const inputPlaceholder = match[1];
				const inputHtml = `<input type="text" class="input-answer" oninput="handleChange(event, ${index})" data-index="${index}"  />`;
				replaced = replaced.replace(placeholder, inputHtml);
			}

			// Convert the replaced HTML string to a DOM element
			const tempDiv = document.createElement('div');
			tempDiv.innerHTML = replaced;

			// Return the replaced HTML string
			return tempDiv.innerHTML;
		},
		attachEventListeners() {
			const inputs = document.querySelectorAll('.input-answer');
			inputs.forEach(input => {
				input.addEventListener('input', event => {
					const inputValue = event.target.value;
					console.log('Input value:', inputValue);
				});
			});
		},
		renderFormAnswer() {
			let question = "";
			for (let option of this.options) {
				question = option.question_content;
			}
			let textsMatch = getFromBetween.get(question, "{", "}");
			for (let text of textsMatch) {
				question = question.replace(`{${text}}`, "{}");
			}
			let arrQuestion = question.split("{}");
			this.arrQuestionResult = [];
			arrQuestion.forEach((item, index) => {
				if (index !== 0 || item.length === 0) {
					this.arrQuestionResult.push({
						type: "input",
						value: this.answer ? this.answer[this.arrQuestionResult.length]?.value || "" : "",
					});
				}
				this.arrQuestionResult.push({
					type: "text",
					value: item,
				});
			});
		}

	},
};
</script>

<style>
.input-answer {
	border-bottom: 1px solid #797B7C;
	outline: none;
	text-align: center;
	min-width: 5rem;
}
</style>
