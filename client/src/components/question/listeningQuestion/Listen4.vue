<template>
  <v-card>
    <v-card-title>
      {{ question.title }}
    </v-card-title>
    <v-card-text>
      <Audio :file="question.files[0].url" color="success"></Audio>
      <div v-for="(sub, idx) in question.subQuestions">
        <p style="margin-bottom: 8px;">{{ idx + 1 }}. {{ sub.content }}</p>
        <v-row>
          <v-col cols="12" style="padding-top: 0">
            <v-checkbox v-for="option in sub.options" hide-details v-model="sub.selected" :label="option.option"
              :value="option.option"></v-checkbox>
          </v-col>
        </v-row>
      </div>
    </v-card-text>
  </v-card>
</template>

<script>
import Audio from '@/components/Audio.vue';

export default {
  components: {
    Audio,
  },
  props: {
    question: Object,
    questionResults: Object,
  },
  mounted() {
    this.question.subQuestions.forEach((sub) => {
      const subquestionResult = {
        questionId: sub.questionId,
        answers: [],
        rightAnswer: null,
        score: 0,
        defaultScore: 1,
      };
      this.questionResults.push(subquestionResult);
    });
  },
  methods: {
    normalizeText(text) {
      if (!text)
        return '';
      return text.toLowerCase().trim().replace(/\s+/g, ' ');
    },
    caculatePoint(data) {
      data.forEach((sub) => {
        const qr = this.questionResults.find(
          (q) => q.questionId === sub.questionId
        );
        if (sub.selected) {
          qr.answers = [sub.selected];
          if (sub.answers.some(answer => this.normalizeText(answer.answer) === this.normalizeText(sub.selected))) {
            qr.rightAnswer = true;
            qr.score = 1;
          } else {
            qr.rightAnswer = false;
            qr.score = 0;
          }
        } else {
          qr.answers = [];
          qr.rightAnswer = false;
          qr.score = 0;
        }
      });
    },
  },
  watch: {
    'question.subQuestions': {
      handler: function (val) {
        this.caculatePoint(val);
      },
      deep: true,
    },
  },
};
</script>
