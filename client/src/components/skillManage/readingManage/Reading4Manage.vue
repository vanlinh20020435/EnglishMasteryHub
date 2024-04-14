<template>
  <GroupQuestion
    :questionSkill="questionSkill"
    hasPassage
    :groupTitleQuestion="groupTitleQuestion"
    :handleDeleteSkill="handleDeleteSkill"
    @updateGroupTitleQuestionOri="updateGroupTitleQuestion($event)"
  >
    <template v-slot:list-questions>
      <v-col class="d-flex flex-column align-center pa-3">
        <v-col
          cols="12"
          v-for="(question, index) in questions"
          :key="index"
          class="pa-0 mt-2 mb-2 ml-2 mr-2"
          style="border: 1px solid #00bd7e"
        >
          <HeaderAction
            :handleToggleShowFull="() => handleToggleShowFullQuestion(index)"
            :title="
              question.content ? question.content : `Question ${index + 1}`
            "
            :handleDelete="() => handleDeleteQuestion(index)" />
          <v-row
            :class="{
              hide: !showFullQuestion[index],
              show: showFullQuestion[index],
            }"
            class="d-flex mt-3 pl-3"
          >
            <v-col cols="12" class="mr-2">
              <v-textarea
                rows="1"
                max-rows="4"
                :rules="required"
                placeholder="Câu hỏi"
                hide-no-data
                clearable
                auto-grow
                :model-value="question.content"
                @input="(event) => updateTitleQuestion(index, event)"
              >
              </v-textarea>

              <v-col class="pt-1 pb-0">
                <v-row class="d-flex flex-row justify-end">
                  <v-col cols="2">
                    <v-list-subheader>Đáp án</v-list-subheader>
                  </v-col>

                  <v-col cols="8">
                    <v-select
                      :model-value="this.answerSelection"
                      clearable
                      :items="['True', 'False']"
                    ></v-select>
                  </v-col>
                </v-row>
              </v-col>

              <v-col class="mb-8">
                <v-row class="d-flex flex-row justify-end">
                  <v-col cols="2">
                    <v-list-subheader>Giải thích</v-list-subheader>
                  </v-col>

                  <v-col cols="8">
                    <v-textarea
                      rows="2"
                      max-rows="4"
                      placeholder="Giải thích ..."
                      hide-no-data
                      clearable
                      hide-details
                    >
                    </v-textarea>
                  </v-col>
                </v-row>
              </v-col>
            </v-col> </v-row
        ></v-col>
      </v-col>

      <div color="#fff" class="mt-1 mb-4 d-flex pl-3 pr-3" style="border: none">
        <v-spacer></v-spacer>
        <v-btn
          @click="() => handleAddQuestion()"
          color="#FBB03B"
          theme="dark"
          class="btn-add-question"
          >Thêm câu hỏi</v-btn
        >
      </div></template
    >
  </GroupQuestion>
</template>

<script>
import GroupQuestion from "@/components/exams/GroupQuestion.vue";
import HeaderAction from "@/components/header/HeaderAction.vue";
import QuestionCheckbox from "@/components/exams/CheckBoxQuestion.vue";
import { apiCallerPost } from "@/services/teacher";

export default {
  name: "Reading4Manage",
  components: {
    HeaderAction,
    QuestionCheckbox,
    GroupQuestion,
  },
  data: () => {
    return {
      showFull: true, // Track whether v-row is shown or hidden
      showFullQuestion: [], // Track whether v-row is shown or hidden
      required: [
        (v) => {
          if (v) return true;
          return "Trường nhập bắt buộc!";
        },
      ],
      selectedFile: null,
      selectedFileName: [],
      fileUpload: {},
      answerSelection: "True",
      questions: [],
    };
  },
  props: {
    handleDeleteSkill: Function,
    groupTitleQuestion: String,
    handleDeleteSkillQuestion: Function,
    questionSkill: Object,
  },
  created() {
    this.questions = this.questionSkill.subQuestions;
    // Initialize the showFullQuestion array with default visibility state for each question
    this.showFullQuestion = Array(this.questions?.length).fill(true);
  },
  methods: {
    handleToggleShowFull() {
      this.showFull = !this.showFull; // Toggle the state
    },
    handleToggleShowFullQuestion(index) {
      // Toggle the visibility state for the question at the specified index
      this.showFullQuestion[index] = !this.showFullQuestion[index];
    },
    handleDeleteQuestion(index) {
      // Call the parent component method to delete the question at the specified index
      this.$emit("deleteQuestion", index);
    },
    updateGroupTitleQuestion(value) {
      // Emit the updated groupTitleQuestion value to the parent component
      this.$emit("updateGroupTitleQuestion", value);
    },
    handleAddQuestion() {
      // Add a new question
      const newIndex = this.questions?.length + 1;
      this.questions.push({
        title: `Question ${newIndex}`,
        numOptions: 4,
        options: Array.from({ length: 4 }, (_, i) => ({
          option: "",
        })),
        answers: [{
          answer: "",
          explanation: ''
        }],
      });
      this.$nextTick(() => {
        this.showFullQuestion[newIndex - 1] = true;
      });
      // Emit an event to notify the parent component about the addition
      this.$emit("addQuestion", newIndex);
    },

    handleChangeExplanation(questionIndex, optionIndex, value) {
      // Update the answer value in the corresponding question option
      this.questions[questionIndex].options[optionIndex].explanation = value;
    },

    updateTitleQuestion(questionIndex, event) {
      this.questions[questionIndex].content = event.target.value;
    },
  },
};
</script>

<style>
.hide {
  height: 0;
  opacity: 0;
  overflow: hidden;
  transition: max-height 0.3s ease-out, opacity 0.3s ease-out;
}

.show {
  height: auto; /* Adjust according to your content height */
  opacity: 1;
  transition: max-height 0.3s ease-in, opacity 0.3s ease-in;
}

.btn-add-question .v-btn__content {
  color: #fff;
}
</style>
