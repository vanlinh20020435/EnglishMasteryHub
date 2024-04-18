<template>
  <div>
    <PronunQuestion v-if="question?.skill == 'pronunciation'" :question="question" :questionResults="questionResults" />
    <GrammarQuestion v-if="question?.skill == 'grammar'" :question="question" :questionResults="questionResults"
      :indexQuestion="indexQuestion" />
    <WritingQuestion v-if="question?.skill == 'writing'" :question="question" :questionResults="questionResults" />
    <ListeningQuestion v-if="question?.skill == 'listening'" :question="question" :questionResults="questionResults" />
  </div>
</template>

<script>
import PronunQuestion from '@/components/question/pronunQuestion/index.vue';
import GrammarQuestion from '@/components/question/grammarQuestion/index.vue';
import WritingQuestion from '@/components/question/writingQuestion/index.vue';
import ListeningQuestion from '@/components/question/listeningQuestion/index.vue';

export default {
  name: 'QuestionDoExam',
  components: {
    PronunQuestion,
    GrammarQuestion,
    WritingQuestion,
    ListeningQuestion
  },
  props: {
    question: Object,
    questionResults: Object,
    indexQuestion: Number,
  },
  mounted() {
  },
}
</script>