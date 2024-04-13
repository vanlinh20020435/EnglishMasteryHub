<template>
  <div>
    <PronunQuestion v-if="question?.skill == 'pronunciation'" :question="question" />
    <GrammarQuestion v-if="question?.skill == 'grammar'" :question="question" />
  </div>
</template>

<script>
import PronunQuestion from '@/components/question/pronunQuestion/index.vue';
import GrammarQuestion from '@/components/question/grammarQuestion/index.vue';

export default {
  name: 'QuestionDoExam',
  components: {
    PronunQuestion,
    GrammarQuestion
  },
  props: {
    question: Object,
  },
  mounted() {
  },
}
</script>