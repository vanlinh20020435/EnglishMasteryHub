<template>
  <GroupQuestion :questionSkill="questionSkill" :groupTitleQuestion="groupTitleQuestion"
    :handleDeleteSkill="handleDeleteSkill" @updateGroupTitleQuestionOri="updateGroupTitleQuestion($event)">
    <template v-slot:list-questions>
      <v-col class="d-flex flex-column align-center pa-3">
        <v-col cols="12" v-for="(question, index) in questions" :key="index" class="pa-0 mt-2 mb-2 ml-2 mr-2"
          style="border: 1px solid #00bd7e">
          <HeaderAction :handleToggleShowFull="() => handleToggleShowFullQuestion(index)" :title="question.content ? question.content : `Question ${index + 1}`
            " :handleDelete="() => handleDeleteQuestion(index)" />

          <v-row :class="{
            hide: !showFullQuestion[index],
            show: showFullQuestion[index],
          }" class="d-flex mt-3 pl-3">
            <v-col cols="12" class="mr-2">
              <v-textarea rows="1" max-rows="4" :rules="required" placeholder="Câu hỏi" hide-no-data clearable auto-grow
                :model-value="question?.content || `Question ${index + 1}`" @input="(event) => updateTitleQuestion(index, event)">
              </v-textarea>

              <v-col class="pt-1 pb-0">
                <v-row class="d-flex flex-row justify-end">
                  <v-col cols="2">
                    <v-list-subheader>Đáp án</v-list-subheader>
                  </v-col>

                  <v-col cols="8">
                    <v-text-field variant="solo" :rules="required" placeholder="Đáp án ..." hide-no-data clearable
                      :model-value="question.answers[0].answer"
                      @input="(event) => handleUpdateAnswer(index, 0, event.target.value)">
                    </v-text-field>
                  </v-col>
                </v-row>
              </v-col>

              <v-col v-for="indexOption in question.answers.length - 1" :key="indexOption"
                class="d-flex justify-end pl-0 pr-0 pt-1 pb-0">
                <v-col cols="2">
                  <v-list-subheader>Đáp án khác</v-list-subheader>
                </v-col>
                <v-col cols="8">
                  <v-row class="d-flex flex-row">
                    <v-col cols="11">
                      <v-text-field hide-details hide-no-data single-line variant="solo" placeholder="Đáp án khác ..."
                        :model-value="question.answers[indexOption]?.answer || ''" @input="(event) =>
                          handleUpdateAnswer(
                            index,
                            indexOption,
                            event.target.value
                          )
                          "></v-text-field>
                    </v-col>
                    <v-col cols="1" class="d-flex flex-column align-end mt-4">
                      <v-icon class="pr-5 cursor-pointer" color="red" size="default"
                        @click="() => handleDeleteOtherAnswer(0, indexOption)">
                        mdi-trash-can
                      </v-icon>
                    </v-col>
                  </v-row>
                </v-col>
              </v-col>

              <div color="#fff" class="mt-6 mb-4 d-flex pl-3 pr-3" style="border: none">
                <v-spacer></v-spacer>
                <v-btn @click="() => handleAddOtherAnswer(index)" color="#00bd7e" theme="dark">Thêm đáp án khác</v-btn>
              </div>

              <v-col class="mt-4 pb-8">
                <v-row class="d-flex flex-row justify-end">
                  <v-col cols="2">
                    <v-list-subheader>Giải thích</v-list-subheader>
                  </v-col>

                  <v-col cols="8">
                    <v-textarea rows="2" max-rows="4" placeholder="Giải thích ..." hide-no-data clearable hide-details
                      :model-value="question?.answers[0]?.explanation || ''" @input="(event) =>
                        updateExplanation(
                          index,
                          event.target.value
                        )
                        ">
                    </v-textarea>
                  </v-col>
                </v-row>
              </v-col>
            </v-col>
          </v-row>
        </v-col>
      </v-col>

      <div color="#fff" class="mt-1 mb-4 d-flex pl-3 pr-3" style="border: none">
        <v-spacer></v-spacer>
        <v-btn @click="() => handleAddQuestion()" color="#FBB03B" theme="dark" class="btn-add-question">Thêm câu
          hỏi</v-btn>
      </div>
    </template>
  </GroupQuestion>
</template>

<script>
import GroupQuestion from "@/components/exams/GroupQuestion.vue";
import HeaderAction from "@/components/header/HeaderAction.vue";
import { apiCallerPost } from "@/services/teacher";

export default {
  name: "Writing1Manage",
  components: {
    GroupQuestion,
    HeaderAction,
  },
  data() {
    return { showFullQuestion: [], questions: [] };
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
    updateGroupTitleQuestion(value) {
      // Emit the updated groupTitleQuestion value to the parent component
      this.$emit("updateGroupTitleQuestion", value);
    },
    handleToggleShowFullQuestion(index) {
      // Toggle the visibility state for the question at the specified index
      this.showFullQuestion[index] = !this.showFullQuestion[index];
    },
    handleDeleteQuestion(index) {
      // Call the parent component method to delete the question at the specified index
      this.$emit("deleteQuestion", index);
    },
    handleAddQuestion() {
      // Add a new question
      const newIndex = this.questions?.length + 1;
      this.questions.push({
        title: `Question ${newIndex}`,
        numOptions: 2,
        options: Array.from({ length: 2 }, (_, i) => ({
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

    handleDeleteOtherAnswer(questionIndex, optionIndex) {
      if (questionIndex >= 0 && questionIndex < this.questions?.length) {
        // Access the question object
        const question = this.questions[questionIndex];

        // Check if optionIndex is valid
        if (optionIndex >= 0 && optionIndex < question.answers.length) {
          // Remove the option at the specified index
          question.answers.splice(optionIndex, 1);

          // that an option has been deleted
          this.$emit("optionDeleted", { questionIndex, optionIndex });
        } else {
          console.error("Invalid optionIndex");
        }
      } else {
        console.error("Invalid questionIndex");
      }
    },
    handleAddOtherAnswer(questionIndex) {
      // Check if questionIndex is valid
      if (questionIndex >= 0 && questionIndex < this.questions?.length) {
        // Access the question object
        const question = this.questions[questionIndex];
        question.options[0] = ({
          option: "",
        });

        question.answers.push({
          answer: "",
          explanation: ''
        });
      } else {
        console.error("Invalid questionIndex");
      }
    },

    handleUpdateAnswer(questionIndex, optionIndex, value) {
      // Update the answer value in the corresponding question option
      this.questions[questionIndex].answers[optionIndex].answer = value;
    },

    updateTitleQuestion(questionIndex, event) {
      this.questions[questionIndex].content = event.target.value;
      this.questions[questionIndex].options[0].option = event.target.value;
    },

    updateExplanation(questionIndex, newValue) {
      this.questions[questionIndex].answers.forEach((answer) => answer.explanation = newValue);
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
  height: auto;
  /* Adjust according to your content height */
  opacity: 1;
  transition: max-height 0.3s ease-in, opacity 0.3s ease-in;
}

.btn-add-question .v-btn__content {
  color: #fff;
}
</style>
