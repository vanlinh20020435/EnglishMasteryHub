<template>
  <div>
    <Grammar1Manage
      v-if="question.type == '1'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Grammar2Manage
      v-if="question.type == '2'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Grammar3Manage
      v-if="question.type == '3'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Grammar4Manage
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
import Grammar1Manage from "./Grammar1Manage.vue";
import Grammar4Manage from "./Grammar4Manage.vue";
import Grammar3Manage from "./Grammar3Manage.vue";
import Grammar2Manage from "./Grammar2Manage.vue";

export default {
  name: "GrammarManage",
  components: {
    Grammar1Manage,
    Grammar4Manage,
    Grammar3Manage,
    Grammar2Manage,
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
