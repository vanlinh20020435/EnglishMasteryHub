<template>
  <div>
    <v-container>
      <DoGrammar1 />
    </v-container>
  </div>
</template>

<script>
import { DoGrammar1 } from '@/components/skillDoExam/grammarDoExam';
export default {
  name: "DoExam",
  components: {
    DoGrammar1
}
};
</script>

<style>
@import "./DoExam.style.scss";
</style>
