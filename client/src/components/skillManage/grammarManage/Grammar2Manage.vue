<template>
  <GroupQuestion
    :questionSkill="questionSkill"
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
            :handleDelete="() => handleDeleteQuestion(index)"
          />

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
                :model-value="question?.content || `Question ${index + 1}`"
                @change="(event) => updateTitleQuestion(index, event)"
              >
              </v-textarea>
              <v-col>
                <v-row class="d-flex justify-end">
                  <v-col cols="2" class="align-end">
                    <v-list-subheader class="d-flex justify-end"
                      >Đáp án</v-list-subheader
                    >
                  </v-col>

                  <v-col cols="9">
                    <v-text-field
                      rows="2"
                      max-rows="4"
                      placeholder="Đáp án ..."
                      hide-no-data
                      clearable
                      auto-grow
                      @click:clear="() => handleClear('answer', index)"
                      variant="solo"
                      :model-value="question?.answers[0]?.answer || ''"
                      @input="
                        (event) => updateAnswer(index, event.target.value)
                      "
                    >
                    </v-text-field>
                  </v-col>
                </v-row>
                <v-row class="d-flex justify-end mt-0">
                  <v-col cols="2" class="align-end">
                    <v-list-subheader class="d-flex justify-end"
                      >Giải thích</v-list-subheader
                    >
                  </v-col>

                  <v-col cols="9">
                    <v-textarea
                      rows="2"
                      max-rows="4"
                      placeholder="Giải thích ..."
                      hide-no-data
                      @click:clear="() => handleClear('explanation', index)"
                      clearable
                      auto-grow
                      @change="
                        (event) => updateExplanation(index, event.target.value)
                      "
                      :model-value="question?.answers[0]?.explanation || ''"
                    >
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
        <v-btn
          @click="() => handleAddQuestion()"
          color="#FBB03B"
          theme="dark"
          class="btn-add-question"
          >Thêm câu hỏi</v-btn
        >
      </div>
    </template>
  </GroupQuestion>
</template>

<script>
import GroupQuestion from "@/components/exams/GroupQuestion.vue";
import HeaderAction from "@/components/header/HeaderAction.vue";

export default {
  name: "Grammar2Manage",
  components: {
    GroupQuestion,
    HeaderAction,
  },
  data() {
    return {
      showFullQuestion: [],
      required: [
        (v) => {
          if (v) return true;
          return "Trường nhập bắt buộc!";
        },
      ],
      questions: [],
    };
  },

  created() {
    this.questions = this.questionSkill.subQuestions;
    // Initialize the showFullQuestion array with default visibility state for each question
    this.showFullQuestion = Array(this.questions?.length).fill(true);
  },
  props: {
    groupTitleQuestion: String,
    handleDeleteSkill: Function,
    questionSkill: Object,
  },
  methods: {
    updateGroupTitleQuestion(value) {
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
    updateGroupTitleQuestion(value) {
      // Emit the updated groupTitleQuestion value to the parent component
      this.$emit("updateGroupTitleQuestion", value);
    },
    handleAddQuestion() {
      // Add a new question
      const newIndex = this.questions?.length + 1;
      this.questions.push({
        title: `Question ${newIndex}`,
        content: `Question ${newIndex}`,
        numOptions: 4,
        options: Array.from({ length: 4 }, (_, i) => ({
          option: "",
        })),
        answers: [
          {
            answer: "",
            explanation: "",
          },
        ],
      });
      this.$nextTick(() => {
        this.showFullQuestion[newIndex - 1] = true;
      });
      // Emit an event to notify the parent component about the addition
      this.$emit("addQuestion", newIndex);
    },
    updateTitleQuestion(questionIndex, event) {
      const valueTitle = event.target.value;
      this.questions[questionIndex].content = valueTitle;

      const regex = /\[(.*?)\]/g;
      let matches = Array.from(valueTitle.matchAll(regex), m => m[1]?.trim());

      this.questions[questionIndex]?.options.forEach((option, index) => {
        option.option = matches[index] || '';
      });

      this.questions[questionIndex].options = 
      this.questions[questionIndex].options.filter((option, index) => matches[index] !== undefined);
    
    },
    updateAnswer(questionIndex, newValue) {
      this.questions[questionIndex].answers[0].answer = newValue;
    },
    updateExplanation(questionIndex, newValue) {
      this.questions[questionIndex].answers[0].explanation = newValue;
    },
    handleClear(typeClear, questionIndex) {
      if (typeClear == "answer") {
        this.questions[questionIndex].answers[0].answer = "";
      } else if (typeClear == "explanation") {
        this.questions[questionIndex].answers[0].explanation = "";
      }
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
