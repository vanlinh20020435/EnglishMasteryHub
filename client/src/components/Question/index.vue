<template>
  <div>
    <PronunDoExam v-if="question?.skill == 'pronunciation'" :question="question" />
    <GrammarDoExam v-if="question?.skill == 'grammar'" :question="question" />
  </div>
</template>

<script>
import PronunDoExam from '@/components/Question/PronunDoExam/index.vue';
import GrammarDoExam from '@/components/Question/GrammarDoExam/index.vue';

export default {
  name: 'QuestionDoExam',
  components: {
    PronunDoExam,
    GrammarDoExam
  },
  props: {
    question: Object,
  },
  mounted() {
    console.log('question=====', this.question)
  },
}
</script>