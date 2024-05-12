<template>
  <div>
    <PronunQuestion
      v-if="question?.skill == 'pronunciation'"
      :question="question"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :reviewExam="reviewExam"
    />
    <GrammarQuestion
      v-if="question?.skill == 'grammar'"
      :question="question"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :reviewExam="reviewExam"
    />
    <WritingQuestion
      v-if="question?.skill == 'writing'"
      :question="question"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :reviewExam="reviewExam"
    />
    <ListeningQuestion
      v-if="question?.skill == 'listening'"
      :question="question"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :reviewExam="reviewExam"
    />
    <ReadingQuestion
      v-if="question?.skill == 'reading'"
      :question="question"
      :questionResults="questionResults"
      :indexQuestion="indexQuestion"
      :reviewExam="reviewExam"
    />
  </div>
</template>

<script>
import PronunQuestion from "@/components/question/pronunQuestion/index.vue";
import GrammarQuestion from "@/components/question/grammarQuestion/index.vue";
import WritingQuestion from "@/components/question/writingQuestion/index.vue";
import ListeningQuestion from "@/components/question/listeningQuestion/index.vue";
import ReadingQuestion from "@/components/question/readingQuestion/index.vue";

export default {
  name: "QuestionDoExam",
  components: {
    PronunQuestion,
    GrammarQuestion,
    WritingQuestion,
    ListeningQuestion,
    ReadingQuestion,
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