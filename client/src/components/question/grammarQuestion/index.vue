<template>
  <Grammar1Question v-if="question?.type === '1'" :dataQuestion="question"></Grammar1Question>
</template>

<script>
import Grammar1Question from '@/components/question/grammarQuestion/Grammar1.vue';
export default {
  components: {
    Grammar1Question,
  },
  props: {
    question: Object,
  },
  mounted() {
  },
};
</script>