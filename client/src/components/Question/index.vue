<template>
  <Pronun1 v-if="question?.type === '1'" :question="question"></Pronun1>
  <Pronun4 v-else-if="question?.type === '4'" :question="question"></Pronun4>
</template>

<script>
import Pronun1 from '@/components/Question/Pronun1.vue';
import Pronun4 from '@/components/Question/Pronun4.vue';
export default {
  components: {
    Pronun1,
    Pronun4,
  },
  props: {
    question: Object,
  },
  mounted() {
    console.log('hihihihihi: ', this.question);
  },
};
</script>
