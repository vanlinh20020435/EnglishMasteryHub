<template>
  <div>
    <Reading1Manage
      v-if="question.type == '1'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Reading2Manage
      v-if="question.type == '2'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Reading3Manage
      v-if="question.type == '3'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Reading4Manage
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
import Reading1Manage from "./Reading1Manage.vue";
import Reading2Manage from "./Reading2Manage.vue";
import Reading3Manage from "./Reading3Manage.vue";
import Reading4Manage from "./Reading4Manage.vue";

export default {
  name: "ReadingManage",
  components: {
    Reading1Manage,
    Reading2Manage,
    Reading3Manage,
    Reading4Manage,
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
