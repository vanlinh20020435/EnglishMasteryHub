<template>
  <Pronun1 v-if="question?.type === '1'" :question="question" :questionResults="questionResults"></Pronun1>
  <Pronun4 v-else-if="question?.type === '4'" :question="question" :questionResults="questionResults"></Pronun4>
</template>

<script>
import Pronun1 from '@/components/question/pronunQuestion/Pronun1.vue';
import Pronun4 from '@/components/question/pronunQuestion/Pronun4.vue';
export default {
  components: {
    Pronun1,
    Pronun4,
  },
  props: {
    question: Object,
    questionResults: Object
  },
  mounted() {
  },
};
</script>
