<template>
  <div>
    <Grammar1Question
      v-if="question?.type === '1'"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :dataQuestion="question"
      :reviewExam="reviewExam"
    ></Grammar1Question>
    <Grammar2Question
      v-if="question?.type === '2'"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :dataQuestion="question"
      :reviewExam="reviewExam"
    ></Grammar2Question>
    <Grammar3Question
      v-if="question?.type === '3'"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :dataQuestion="question"
      :reviewExam="reviewExam"
    ></Grammar3Question>
    <Grammar4Question
      v-if="question?.type === '4'"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :dataQuestion="question"
      :reviewExam="reviewExam"
    ></Grammar4Question>
  </div>
</template>

<script>
import Grammar1Question from "@/components/question/grammarQuestion/Grammar1.vue";
import Grammar2Question from "@/components/question/grammarQuestion/Grammar2.vue";
import Grammar3Question from "@/components/question/grammarQuestion/Grammar3.vue";
import Grammar4Question from "@/components/question/grammarQuestion/Grammar4.vue";

export default {
  components: {
    Grammar1Question,
    Grammar2Question,
    Grammar3Question,
    Grammar4Question,
  },
  props: {
    question: Object,
    questionResults: Object,
    indexQuestion: Number,
    reviewExam: String,
  },
  mounted() {},
};
</script>