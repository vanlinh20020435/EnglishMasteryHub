<template>
  <div>
    <Writing1Manage
      v-if="question.type == '1'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Writing2Manage
      v-if="question.type == '2'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Writing3Manage
      v-if="question.type == '3'"
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
import Writing1Manage from "./Writing1Manage.vue";
import Writing2Manage from "./Writing2Manage.vue";
import Writing3Manage from "./Writing3Manage.vue";

export default {
  name: "WritingManage",
  components: { Writing1Manage, Writing2Manage, Writing3Manage },
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
