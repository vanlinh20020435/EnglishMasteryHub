<template>
  <v-card>
    <v-card-title>
      {{ question.title }}
    </v-card-title>
    <v-card-text>
      <v-row>
        <v-col v-for="(sub, idx) in question.subQuestions">
          <p style="margin-bottom: 4px">{{ idx + 1 }}.</p>
          <Audio :file="sub.url" color="success"></Audio>
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
  },
  mounted() {
    console.log(this.question);
  },
};
</script>
