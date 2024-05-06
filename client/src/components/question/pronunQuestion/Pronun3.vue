<template>
  <div class="d-flex flex-column">
    <h3 class="font-semi-bold text-lg">
      Bài {{ indexQuestion + 1 }}: {{ question.title }}
    </h3>
    <v-col>
      <div v-for="(sub, idx) in question.subQuestions" :key="idx">
        <p style="margin-bottom: 4px">{{ idx + 1 }}. {{ sub.content }}</p>
        <Audio :file="sub.files[0].url" color="success" :config="{ sound: true }"></Audio>
        <v-row>
          <v-col cols="3" v-for="(option, indexOption) in sub.options" :key="indexOption" >
            <v-text-field hide-details v-model="option.option"></v-text-field>
          </v-col>
        </v-row>
      </div>
    </v-col>
  </div>
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
    indexQuestion: Number,
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
        qr.answers = sub.options.map(option => this.normalizeText(option.option));
        let selected = qr.answers
        let sc = 0
        sub.answers.forEach(answer => {
          let correct = selected.some(s => s === answer.answer)
          sc += correct ? 1 : 0
        });
        qr.score = sc / selected.length
        qr.rightAnswer = sc === selected.length
      })
    }
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
