<template>
  <GroupQuestion
    :questionSkill="questionSkill"
    :groupTitleQuestion="groupTitleQuestion"
    :handleDeleteSkill="handleDeleteSkill"
    @updateGroupTitleQuestionOri="updateGroupTitleQuestion($event)"
  >
    <template v-slot:list-questions>
      <v-col style="border: 1px solid #00bd7e">
        <v-row class="d-flex justify-center">
          <v-col cols="2">
            <v-list-subheader>File phát âm</v-list-subheader>
          </v-col>

          <v-col cols="8">
            <v-file-input
              @change="(event) => handleFileUpload(event, 0)"
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
            v-for="indexOption in questions[0].options.length"
            :key="indexOption"
          >
            <v-row class="d-flex flex-row ml-2">
              <v-col cols="11">
                <v-row class="d-flex flex-row">
                  <v-col cols="5">
                    <v-text-field
                      :rule="required"
                      hide-no-data
                      single-line
                      variant="solo"
                      placeholder="Tên nhóm từ"
                      :model-value="option"
                      @input="updateOption($event.target.value)"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="7">
                    <v-textarea
                      rows="1"
                      max-rows="4"
                      :rules="required"
                      placeholder="Danh sách các từ đúng vd: table; telephone; ..."
                      hide-no-data
                      clearable
                      auto-grow
                    >
                    </v-textarea>
                  </v-col>
                </v-row>
              </v-col>
              <v-col cols="1" class="d-flex flex-column align-end mt-4">
                <v-icon
                  class="pr-5 cursor-pointer"
                  color="red"
                  size="default"
                  @click="() => handleDeleteOption(0, indexOption - 1)"
                >
                  mdi-trash-can
                </v-icon>
              </v-col>
            </v-row>
          </v-col>
        </v-row>
        <div
          color="#fff"
          class="mt-8 mb-4 d-flex pl-3 pr-3"
          style="border: none"
        >
          <v-spacer></v-spacer>
          <v-btn @click="() => handleAddOption(0)" color="#00bd7e" theme="dark"
            >Thêm item</v-btn
          >
        </div>
      </v-col>
    </template>
  </GroupQuestion>
</template>

<script>
import GroupQuestion from "@/components/exams/GroupQuestion.vue";
import HeaderAction from "@/components/header/HeaderAction.vue";
import { apiCallerPost } from "@/services/teacher";

export default {
  name: "Pronun2Manage",
  components: {
    HeaderAction,
    GroupQuestion,
  },
  data() {
    return {
      selectedFileName: [],
      fileUpload: {},
      option: "",
      required: [
        (v) => {
          if (v) return true;
          return "Trường nhập bắt buộc!";
        },
      ],
      questions: [],
      defaultFile: null,
    };
  },
  props: {
    groupTitleQuestion: String,
    handleDeleteSkill: Function,
    questionSkill: Object,
  },
  created() {
    this.questions = this.questionSkill.subQuestions;
    this.defaultFile = this.questions?.files?.[0]?.name;
  },
  methods: {
    updateGroupTitleQuestion(value) {
      this.$emit("updateGroupTitleQuestion", value);
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

    updateOption(newValue) {},

    handleAddOption(questionIndex) {
      // Check if questionIndex is valid
      if (questionIndex >= 0 && questionIndex < this.questions?.length) {
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
    handleDeleteOption(questionIndex, optionIndex) {
      if (questionIndex >= 0 && questionIndex < this.questions?.length) {
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
  },
};
</script>
