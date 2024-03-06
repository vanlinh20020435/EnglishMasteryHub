<template>
  <v-col class="pa-0" style="border: 1px solid #00000020">
    <HeaderAction
      :handleToggleShowFull="handleToggleShowFull"
      :title="groupTitleQuestion"
      :handleDelete="handleDeleteSkill"
    />
    <v-col class="pa-0" :class="{ hide: !showFull, show: showFull }">
      <v-row class="d-flex mt-3 pl-3">
        <v-col cols="3">
          <v-list-subheader>Nội dung nhóm câu hỏi</v-list-subheader>
        </v-col>

        <v-col cols="9">
          <v-textarea
            rows="1"
            max-rows="4"
            :rules="required"
            placeholder="Tên bài kiểm tra"
            hide-no-data
            clearable
            auto-grow
            :model-value="groupTitleQuestion"
            @input="updateGroupTitleQuestion"
          >
          </v-textarea>
        </v-col>
      </v-row>
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
            :title="`Question ${index + 1}`"
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
                :model-value="`Question ${index + 1}`"
              >
              </v-textarea>
              <v-row class="d-flex justify-center">
                <v-col cols="2">
                  <v-list-subheader>File phát âm</v-list-subheader>
                </v-col>

                <v-col cols="8">
                  <v-file-input
                    @change="(event) => handleFileUpload(event, index)"
                    :rule="required"
                    hide-no-data
                    label="File upload"
                    accept=".mp3,audio/*"
                    show-size
                    variant="outlined"
                    :model-value="selectedFileName"
                  ></v-file-input>
                </v-col>
              </v-row>
              <v-row>
                <v-col
                  cols="12"
                  v-for="indexOption in question.options.length"
                  :key="indexOption"
                >
                  <QuestionCheckbox
                    isDescOption
                    :handleDeleteOption="
                      () => handleDeleteOption(index, indexOption - 1)
                    "
                    @update:option="
                      (value) =>
                        handleUpdateOption(index, indexOption - 1, value)
                    "
                    @update:updateExplanation="
                      (value) =>
                        handleChangeExplanation(index, indexOption - 1, value)
                    "
                    @addAnswer="(data) => handleAddAnswer(data)"
                  />
                </v-col>
              </v-row>
              <div
                color="#fff"
                class="mt-8 mb-4 d-flex pl-3 pr-3"
                style="border: none"
              >
                <v-spacer></v-spacer>
                <v-btn
                  @click="() => handleAddOption(index)"
                  color="#00bd7e"
                  theme="dark"
                  >Thêm item</v-btn
                >
              </div>
            </v-col>
          </v-row></v-col
        >
      </v-col>

      <div color="#fff" class="mt-1 mb-4 d-flex pl-3 pr-3" style="border: none">
        <v-spacer></v-spacer>
        <v-btn
          @click="() => handleAddQuestion(index)"
          color="#FBB03B"
          theme="dark"
          class="btn-add-question"
          >Thêm câu hỏi</v-btn
        >
      </div>
    </v-col>
  </v-col>
</template>

<script>
import QuestionCheckbox from "../exams/checkBoxQuestion.vue";
import HeaderAction from "../header/HeaderAction.vue";
import { apiCallerPost } from "@/services/teacher";
export default {
  name: "Pronun1Manage",
  components: {
    HeaderAction,
    QuestionCheckbox,
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
    };
  },
  props: {
    handleDeleteSkill: Function,
    groupTitleQuestion: String,
    handleDeleteSkillQuestion: Function,
    questions: Array,
  },
  created() {
    // Initialize the showFullQuestion array with default visibility state for each question
    this.showFullQuestion = Array(this.questions.length).fill(true);
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
    updateGroupTitleQuestion(event) {
      // Emit the updated groupTitleQuestion value to the parent component
      this.$emit("updateGroupTitleQuestion", event.target.value);
    },
    handleAddQuestion(index) {
      // Add a new question
      const newIndex = this.questions.length + 1;
      this.questions.push({
        title: `Question ${newIndex}`,
        numOptions: 2,
        options: Array.from({ length: 2 }, (_, i) => ({
          option: "",
        })),
        answers: [],
      });
      this.$nextTick(() => {
        this.showFullQuestion[newIndex - 1] = true;
      });
      // Emit an event to notify the parent component about the addition
      this.$emit("addQuestion", newIndex);
    },
    handleDeleteOption(questionIndex, optionIndex) {
      if (questionIndex >= 0 && questionIndex < this.questions.length) {
        // Access the question object
        const question = this.questions[questionIndex];

        // Check if optionIndex is valid
        if (optionIndex >= 0 && optionIndex < question.options.length) {
          // Remove the option at the specified index
          question.options.splice(optionIndex, 1);

          // that an option has been deleted
          this.$emit("optionDeleted", { questionIndex, optionIndex });
        } else {
          console.error("Invalid optionIndex");
        }
      } else {
        console.error("Invalid questionIndex");
      }
    },
    handleAddOption(questionIndex) {
      // Check if questionIndex is valid
      if (questionIndex >= 0 && questionIndex < this.questions.length) {
        // Access the question object
        const question = this.questions[questionIndex];

        // Push a new option to the question's options array
        const newIndex = question.options.length + 1;
        question.options.push({
          option: "",
        });

        // Emit an event to notify the parent component about the addition
        this.$emit("optionAdded", { questionIndex, optionIndex: newIndex - 1 });
      } else {
        console.error("Invalid questionIndex");
      }
    },
    handleFileUpload(event, questionIndex) {
      // Extract the selected file from the event object
      const file = event.target.files[0];

      // Store the selected file in the data property
      this.selectedFile = file;
      this.selectedFileName[questionIndex] = file ? file.name : "";

      // Now you can use this.selectedFile to make your API call
      this.uploadFile(questionIndex);
    },
    async uploadFile(questionIndex) {
      // Prepare form data to send the file
      const formData = new FormData();
      formData.append("file", this.selectedFile);

      const result = await apiCallerPost(
        "/upload",
        JSON.parse(localStorage?.getItem("accessToken"))?.token,
        formData
      );

      this.fileUpload = result.data;

      const question = this.questions[questionIndex];

      question["files"] = this.fileUpload;
    },

    handleUpdateOption(questionIndex, optionIndex, value) {
      // Update the answer value in the corresponding question option
      this.questions[questionIndex].options[optionIndex].option = value;
    },

    handleChangeExplanation(questionIndex, optionIndex, value) {
      // Update the answer value in the corresponding question option
      this.questions[questionIndex].options[optionIndex].explanation = value;
    },

    handleAddAnswer(data) {
      const { option, explanation, questionIndex } = data;
      this.question[questionIndex].answers.push({ option, explanation });
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
