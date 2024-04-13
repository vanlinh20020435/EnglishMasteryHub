<template>
  <Grammar1DoExam v-if="question?.type === '1'" :dataQuestion="question"></Grammar1DoExam>
</template>

<script>
import Grammar1DoExam from './Grammar1.vue';
export default {
  components: {
    Grammar1DoExam,
  },
  props: {
    question: Object,
  },
  mounted() {
  },
};
</script>