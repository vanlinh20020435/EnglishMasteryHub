<template>
  <div>
    <Reading1Question v-if="question?.type === '1'" :questionResults="questionResults" :indexQuestion="indexQuestion" :dataQuestion="question"></Reading1Question>
    <Reading2Question v-if="question?.type === '2'" :questionResults="questionResults" :indexQuestion="indexQuestion" :dataQuestion="question"></Reading2Question>
    <Reading3Question v-if="question?.type === '3'" :questionResults="questionResults" :indexQuestion="indexQuestion" :dataQuestion="question"></Reading3Question>

  </div>
</template>

<script>
import Reading1Question from './Reading1.vue';
import Reading2Question from './Reading2.vue';
import Reading3Question from './Reading3.vue';
export default {
  components: {
    Reading1Question,
    Reading2Question,
    Reading3Question
  },
  props: {
    question: Object,
    questionResults: Object,
    indexQuestion: Number,
  },
}
</script>