<template>
  <Listen1
    v-if="question?.type === '1'"
    :question="question"
    :questionResults="questionResults"
    :indexQuestion="indexQuestion"
    :reviewExam="reviewExam"
  ></Listen1>
  <Listen2
    v-else-if="question?.type === '2'"
    :question="question"
    :questionResults="questionResults"
    :indexQuestion="indexQuestion"
    :reviewExam="reviewExam"
  ></Listen2>
  <Listen4
    v-else-if="question?.type === '4'"
    :question="question"
    :questionResults="questionResults"
    :indexQuestion="indexQuestion"
    :reviewExam="reviewExam"
  ></Listen4>
</template>

<script>
import Listen1 from "@/components/question/listeningQuestion/Listen1.vue";
import Listen2 from "@/components/question/listeningQuestion/Listen2.vue";
import Listen4 from "@/components/question/listeningQuestion/Listen4.vue";
export default {
  components: {
    Listen1,
    Listen2,
    Listen4,
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
