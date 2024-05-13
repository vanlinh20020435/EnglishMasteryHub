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

          <v-col v-if="defaultFile" cols="8">
            <v-text-field
              prepend-icon="mdi-paperclip"
              :rule="required"
              hide-no-data
              variant="outlined"
              v-model="defaultFile"
              clearable
              readonly
            ></v-text-field>
          </v-col>

          <v-col v-else cols="8">
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
        <v-col>
          <v-row class="d-flex justify-center">
            <v-col cols="3">
              <v-list-subheader>Danh sách các từ xuất hết</v-list-subheader>
            </v-col>

            <v-col cols="9">
              <v-textarea
                rows="2"
                max-rows="4"
                :rules="required"
                placeholder="Danh sách các từ xuất hiện vd: boost, boot, rain, ..."
                hide-no-data
                clearable
                auto-grow
                :model-value="stringAnswers"
                @input="(event) => handleConvertAnswer(event.target.value, 0)"
              >
              </v-textarea>
            </v-col>
          </v-row>
        </v-col>
      </v-col>
    </template>
  </GroupQuestion>
</template>

<script>
import GroupQuestion from "@/components/exams/GroupQuestion.vue";
import { apiCallerPost } from "@/services/teacher";

export default {
  name: "Pronun3Manage",
  components: {
    GroupQuestion,
  },
  data() {
    return {
      selectedFileName: [],

      fileUpload: {},
      required: [
        (v) => {
          if (v) return true;
          return "Trường nhập bắt buộc!";
        },
      ],
      questions: [],
      stringAnswers: "",
      defaultFile: null
    };
  },
  props: {
    groupTitleQuestion: String,
    handleDeleteSkill: Function,
    questionSkill: Object,
  },
  created() {
    this.questions = this.questionSkill.subQuestions;
    this.stringAnswers = this.questionSkill.subQuestions[0].answers.map(obj => obj?.answer).join(', ');
  
    this.defaultFile = this.questionSkill.subQuestions?.[0]?.files?.[0]?.name;
  },
  methods: {
    updateGroupTitleQuestion(value) {
      // Emit the updated groupTitleQuestion value to the parent component
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

    handleConvertAnswer(newValue, questionIndex) {
      let splitValues = newValue.split(", ");
      let resultArray = [];
      let optionsArray = [];

      splitValues.forEach(value => {
        optionsArray.push({option: ''})
        resultArray.push({ answer: value });
      });

      this.questions[questionIndex].answers = resultArray;
      this.questions[questionIndex].options = optionsArray;

    }
  },
};
</script>
