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
            @input="updateGroupTitleQuestionOri"
          >
          </v-textarea>
        </v-col>
      </v-row>

      <v-col class="pa-0" v-if="hasPassage">
        <v-row class="d-flex mt-1 pl-3">
          <v-col cols="3" class="d-flex justify-center">
            <v-list-subheader>Tiêu đề bài đọc</v-list-subheader>
          </v-col>

          <v-col cols="9">
            <v-text-field
              :rules="required"
              placeholder="Tiêu đề"
              hide-no-data
              clearable
              auto-grow
              :model-value="this.questionSkill?.description"
              @input="updateTitltePassage"
            >
            </v-text-field>
          </v-col>
        </v-row>

        <v-row class="d-flex mt-1 pl-3">
          <v-col cols="3" class="d-flex justify-center">
            <v-list-subheader>Nội dung bài đọc</v-list-subheader>
          </v-col>

          <v-col cols="9">
            <v-textarea
              rows="3"
              max-rows="6"
              :rules="required"
              placeholder="Nội dung"
              hide-no-data
              clearable
              auto-grow
              :model-value="this.questionSkill?.content"
              @input="updateContentPassage"
            >
            </v-textarea>
          </v-col>
        </v-row>
      </v-col>

      <v-col class="pa-0" v-if="hasAudio">
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
          <v-col cols="8" v-else>
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

        <v-row class="d-flex mt-1 pl-3">
          <v-col cols="3" class="d-flex justify-center">
            <v-list-subheader>Nội dung file nghe</v-list-subheader>
          </v-col>

          <v-col cols="9">
            <v-textarea
              rows="3"
              max-rows="6"
              placeholder="Nội dung"
              hide-no-data
              clearable
              auto-grow
              :model-value="this.questionSkill?.content"
              @input="updateContentAudio"
            >
            </v-textarea>
          </v-col>
        </v-row>
      </v-col>
      <slot name="list-questions"></slot>
    </v-col>
  </v-col>
</template>

<script>
import HeaderAction from "../header/HeaderAction.vue";
import { apiCallerPost } from "@/services/teacher";

export default {
  name: "GroupQuestion",
  components: {
    HeaderAction,
  },
  data() {
    return {
      showFull: true,
      required: [
        (v) => {
          if (v) return true;
          return "Trường nhập bắt buộc!";
        },
      ],
      selectedFileName: [],
      selectedFile: null,
      defaultFile: null
    };
  },
  props: {
    groupTitleQuestion: String,
    handleDeleteSkill: Function,
    hasPassage: Boolean,
    fileName: String,
    hasAudio: Boolean,
    contentAudio: String,
    questionSkill: Object,
  },
  mounted() {
    this.defaultFile = this.questionSkill?.files?.[0]?.name
  },
  methods: {
    handleToggleShowFull() {
      this.showFull = !this.showFull; // Toggle the state
    },
    updateGroupTitleQuestionOri(event) {
      // Emit the updated groupTitleQuestion value to the parent component
      this.$emit("updateGroupTitleQuestionOri", event.target.value);
    },
    updateTitltePassage(event) {
      // this.$emit("updateTitltePassage", event.target.value);
      this.questionSkill["description"] = event.target.value;
    },
    updateContentPassage(event) {
      this.questionSkill["content"] = event.target.value;
    },
    updateContentAudio(event) {
      this.questionSkill["content"] = event.target.value;
    },
    handleFileUpload(event) {
      // Extract the selected file from the event object
      const file = event.target.files[0];

      this.selectedFile = file;
      // Store the selected file in the data property
      this.selectedFileName[0] = file ? file.name : "";

      // Now you can use this.selectedFile to make your API call
      this.uploadFile();
    },
    async uploadFile() {
      // Prepare form data to send the file
      const formData = new FormData();
      formData.append("file", this.selectedFile);

      const result = await apiCallerPost(
        "/upload",
        JSON.parse(localStorage?.getItem("accessToken"))?.token,
        formData
      );

      this.questionSkill["files"] = result.data;
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
</style>
