<template>
  <div>
    <Pronun1Manage
      v-if="question.type == '1'"
      :groupTitleQuestion="question.title"
      :handleDeleteSkill="handleDeleteSkill"
      :key="'pronun_' + indexQuestion"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Pronun2Manage
      v-if="question.type == '2'"
      :groupTitleQuestion="question.title"
      :key="'pronun_' + indexQuestion"
      :handleDeleteSkill="handleDeleteSkill"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Pronun3Manage
      v-if="question.type == '3'"
      :groupTitleQuestion="question.title"
      :key="'pronun_' + indexQuestion"
      :handleDeleteSkill="handleDeleteSkill"
      :questionSkill="question"
      @deleteQuestion="deleteQuestion($event)"
      @updateGroupTitleQuestion="updateGroupTitleQuestion($event)"
    />
    <Pronun4Manage
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
import Pronun1Manage from "./Pronun1Manage.vue";
import Pronun2Manage from "./Pronun2Manage.vue";
import Pronun3Manage from "./Pronun3Manage.vue";
import Pronun4Manage from "./Pronun4Manage.vue";

export default {
  name: "PronunManage",
  components: {
    Pronun1Manage,
    Pronun2Manage,
    Pronun3Manage,
    Pronun4Manage,
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
