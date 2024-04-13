<template>
  <div>
    <PronunQuestion v-if="question?.skill == 'pronunciation'" :question="question" />
    <GrammarQuestion v-if="question?.skill == 'grammar'" :question="question" />
    <WritingQuestion v-if="question?.skill == 'writing'" :question="question" />
  </div>
</template>

<script>
import PronunQuestion from '@/components/question/pronunQuestion/index.vue';
import GrammarQuestion from '@/components/question/grammarQuestion/index.vue';
import WritingQuestion from '@/components/question/writingQuestion/index.vue';

export default {
  name: 'QuestionDoExam',
  components: {
    PronunQuestion,
    GrammarQuestion,
    WritingQuestion
  },
  props: {
    question: Object,
  },
  mounted() {
  },
}
</script>