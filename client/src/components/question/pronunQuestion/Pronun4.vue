<template>
  <v-card>
    <v-card-title>
      {{ question.title }}
    </v-card-title>
    <v-card-text>
      <div v-for="(sub, idx) in question.subQuestions">
        <p style="margin-bottom: 4px">{{ idx + 1 }}. {{ sub.content }}</p>
        <v-row v-if="$vuetify.display.smAndDown">
          <v-col cols="12" style="padding-bottom: 0;">
            <Audio :file="sub.files[0].url" color="success" :config="{ sound: true }"></Audio>
          </v-col>
          <v-col cols="12" style="padding-top: 0;">
            <v-checkbox v-for="option in sub.options" hide-details v-model="sub.selected" :label="option.option"
              :value="option.option"></v-checkbox>
          </v-col>
        </v-row>
        <v-row v-else>
          <v-col cols="6">
            <v-checkbox v-for="option in sub.options" hide-details v-model="sub.selected" :label="option.option"
              :value="option.option"></v-checkbox>
          </v-col>
          <v-col cols="6">
            <Audio :file="sub.files[0].url" color="success" :config="{ sound: true }"></Audio>
          </v-col>
        </v-row>
      </div>
    </v-card-text>
  </v-card>
</template>

<script>
import Audio from "@/components/Audio.vue";

export default {
  components: {
    Audio,
  },
  props: {
    question: Object,
    questionResults: Object,
  },
  mounted() {
    this.question.subQuestions.forEach(sub => {
      const subquestionResult = {
        questionId: sub.questionId,
        answers: [],
        rightAnswer: null,
        score: 0,
        defaultScore: 1,
      }
      this.questionResults.push(subquestionResult)
    })
  },
  methods: {
    caculatePoint(data) {
      data.forEach((sub) => {
        const qr = this.questionResults.find(q => q.questionId === sub.questionId)
        qr.answers = [sub.selected]
        if (sub.selected === sub.answers[0].answer) {
          qr.rightAnswer = true
          qr.score = 1
        } else {
          qr.rightAnswer = false
          qr.score = 0
        }
      });
    }
  },
  watch: {
    'question.subQuestions': {
      handler: function (val) {
        this.caculatePoint(val)
      },
      deep: true
    }
  }
};
</script>
