<template>
  <v-card>
    <v-card-title>
      {{ question.title }}
    </v-card-title>
    <v-card-text class="">
      <v-row>
        <v-col lg="6" v-for="(sub, idx) in question.subQuestions" click>
          <p style="margin-bottom: 4px">{{ idx + 1 }}. {{ sub.content }}</p>
          <!-- <Audio :file="sub.files[0].url" color="success" :config="{ sound: true }"></Audio> -->
          <v-row>
            <v-col v-for="option in sub.options" md="6">
              <v-checkbox @change="() => " v-model="temporaryAnswers[sub.questionId]" :label="option.option"
                :value="option.option"></v-checkbox>
            </v-col>
          </v-row>
        </v-col>
        <v-col lg="6">
        </v-col>
      </v-row>
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
    questionResults: Object
  },
  computed: {
    temporaryAnswers() {
      const instance = {}
      this.question?.subQuestions.forEach(item => {
        instance[item.questionId] = null
      })
      return instance
    },
    temporaryAnswerWatchers() {
      const instance = {}
      this.question?.subQuestions.forEach(item => {
        instance[`this.temporaryAnswers.${item.questionId}`] = {
          handler(newVal, oldVal) {
            console.log(newVal);
          },
          deep: true,
        }
      })
      return instance
    },
  },
  mounted() {
    const subquestionResults = this.question.subQuestions.map(item => ({
      questionId: item.questionId,
      answers: [],
      rightAnswer: null,
      score: 0,
      defaultScore: 1,
    }))
    this.questionResults.push(...subquestionResults)
  },
  methods: {
    clickedCheckbox(qid) {
      let q = this.question.subQuestions.find(item => item.questionId === qid)
      let qr = this.questionResults.find(item => item.questionId === qid)
      console.log(this.temporaryAnswers[qid], q.answers);
      if (q.answers.find(answer => answer.answer === this.temporaryAnswers[qid])) {
        // alert('hihi')
      }
    }
  },
};
</script>
