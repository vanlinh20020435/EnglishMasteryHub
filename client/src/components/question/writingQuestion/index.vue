<template>
  <Writing1Question
    v-if="question?.type === '1'"
    :dataQuestion="question"
    :questionResults="questionResults"
    :indexQuestion="indexQuestion"
    :reviewExam="reviewExam"
  ></Writing1Question>
  <Writing3Question
    v-else-if="question?.type === '3'"
    :dataQuestion="question"
    :questionResults="questionResults"
    :indexQuestion="indexQuestion"
    :reviewExam="reviewExam"
  ></Writing3Question>
</template>

<script>
import Writing1Question from "@/components/question/writingQuestion/Writing1.vue";
import Writing3Question from "@/components/question/writingQuestion/Writing3.vue";
export default {
  components: {
    Writing1Question,
    Writing3Question,
  },
  props: {
    question: Object,
    questionResults: Object,
    indexQuestion: Number,
    reviewExam: Boolean,
  },
  mounted() {},
};
</script>