<template>
  <div>
    <Listening1Manage
      v-if="question.type == '1'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Listening2Manage
      v-if="question.type == '2'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Listening3Manage
      v-if="question.type == '3'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />

    <Listening4Manage
      v-if="question.type == '4'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
  </div>
</template>

<script>
import Listening1Manage from "./Listening1Manage.vue";
import Listening2Manage from "./Listening2Manage.vue";
import Listening4Manage from "./Listening4Manage.vue";
import Listening3Manage from "./Listening3Manage.vue";

export default {
  name: "ListeningManage",
  components: {
    Listening1Manage,
    Listening2Manage,
    Listening4Manage,
    Listening3Manage,
  },
  data() {
    return {};
  },
  props: {
    skillTypeSelected: String,
    question: String,
    indexQuestion: String,
    handleDeleteSkill: Function,
  },
  methods: {
    deleteQuestion(event) {
      this.$emit("deleteQuestion", event);
    },
    updateGroupTitleQuestion(event) {
      this.$emit("updateGroupTitleQuestion", event);
    },
  },
};
</script>
