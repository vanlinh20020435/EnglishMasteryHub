<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        title="Tài liệu học tập"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>

      <v-col
        class="d-flex flex-column class-list class-docs class-students align-center"
      >
        <div
          v-if="dataStudyDocs.length > 0"
          style="width: 80%; min-width: 28rem; max-width: 70rem; flex: 1"
          class="d-flex flex-column"
        >
          <h3 class="font-bold">Danh sách tài liệu</h3>

          <v-col class="pa-8">
            <v-col
              v-for="(doc, index) in dataStudyDocs"
              :key="index"
              class="cursor-pointer pa-3 pt-5 pb-8 mb-5 pl-12 d-flex student-item docs-item box-shadow"
              @click="handleOpenDocs(doc.url)"
            >
              <v-row
                class="pl-5 pr-5 pt-3 d-flex flex-row w-100 justify-space-between"
              >
                <div class="d-flex flex-row align-center w-100">
                  <div class="d-flex align-center justify-center img_type">
                    <img
                      :src="checkImgTypeDocs(doc.type)"
                      alt="Icon Type Doc"
                    />
                  </div>
                  <div class="d-flex justify-space-between w-100">
                    <div class="d-flex flex-column">
                      <div
                        class="font-semi-bold student-name ml-5 mr-5 d-flex align-center"
                      >
                        {{ doc.documentName }}
                      </div>
                      <div class="student-name ml-5 mr-5 d-flex align-center">
                        Ngày tạo: {{ doc.created }}
                      </div>
                    </div>

                    <div class="d-flex align-center">
                      <v-btn
                        class="btn-delete-linear"
                        @click.stop.prevent="
                          () => {
                            this.docSelected = doc;
                          }
                        "
                      >
                        <v-icon color="white" size="large"
                          >mdi-trash-can</v-icon
                        >
                      </v-btn>
                    </div>
                  </div>
                </div>
              </v-row>
            </v-col>
          </v-col>
        </div>

        <v-row v-else-if="!isLoading" style="height: 90%" class="w-100">
          <v-col>
            <div
              class="d-flex flex-column align-center class-empty class-docs_empty"
            >
              <img
                src="@/assets/images/empty_study_docs.png"
                alt="Img Empty Class"
              />
              <h3 class="d-flex d-flex flex-column align-center w-100">
                Lớp chưa tài liệu học tập nào.
                <br />
                <span>
                  Chọn <strong>"Thêm mới"</strong> để thêm tài liệu.
                </span>
              </h3>
            </div>
          </v-col></v-row
        >
      </v-col>

      <v-col class="pa-5 pt-8 pb-8" style="flex: 0">
        <v-row class="d-flex align-center justify-end">
          <v-btn
            @click="() => handleAddDocs()"
            color="#00bd7e"
            theme="dark"
            class="btn-add-question"
            >Thêm mới</v-btn
          >
        </v-row>
      </v-col>
    </v-container>
    <!-- Dialog -->
    <v-dialog v-model="isOpenForm" max-width="500px">
      <v-card class="d-flex pa-8 pt-6 pb-6">
        <v-card-title class="text-center pb-8">{{ dialogTitle }}</v-card-title>

        <v-form v-model="valid" @submit.prevent="handleCreateNewDocs">
          <v-col>
            <v-row class="pt-2 pb-2">
              <h4 class="font-semi-bold">File tài liệu</h4>
            </v-row>

            <v-row>
              <v-file-input
                :rules="rules.rulesSizeFileUpload"
                @change="(event) => handleFileUpload(event)"
                label="File upload"
                accept=".docx,.doc,.xls,.xlsx,.csv,.pdf,.pptx,.ppt,.txt,.zip,image/*"
                show-size
                variant="outlined"
                :model-value="selectedFileName"
                class="input-file-custom"
              ></v-file-input>
            </v-row>
          </v-col>

          <v-col>
            <v-row class="pb-2">
              <h4 class="font-semi-bold">Tên tài liệu</h4>
            </v-row>

            <v-row>
              <v-textarea
                placeholder="Tên tài liệu"
                rows="2"
                max-rows="3"
                variant="outlined"
                auto-grow
                hide-no-data
                v-model="dataNewDocs.documentName"
                :rules="rules.required"
                @change="handleChangeNameDocs"
              >
              </v-textarea>
            </v-row>
          </v-col>

          <v-col class="pt-10 mt-5" style="flex: 0">
            <v-row class="d-flex align-center justify-end">
              <v-btn type="submit" color="#00bd7e" theme="dark">Lưu</v-btn>
            </v-row>
          </v-col>
        </v-form>
      </v-card>
    </v-dialog>
    <PopUpYesNo
      msg="Bạn có chắc chắn muốn xóa tài liệu này?"
      :visible="!!docSelected?.id"
      :handleClickYes="() => handleDeleteDoc(docSelected)"
      :handleClickNo="
        () => {
          this.docSelected = {};
        }
      "
      @update:visible="handleVisible"
    />
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import {
  apiCallerGet,
  apiCallerPut,
  apiCallerPost,
  apiCallerDelete,
} from "@/services/teacher";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
import ico_doc from "@/assets/images/icon/ico_doc.png";
import ico_document from "@/assets/images/icon/ico_document.png";
import ico_excel from "@/assets/images/icon/ico_excel.png";
import ico_img from "@/assets/images/icon/ico_img.png";
import ico_pdf from "@/assets/images/icon/ico_pdf.png";
import ico_ppt from "@/assets/images/icon/ico_ppt.png";

export default {
  name: "StudyDocuments",
  components: { HeaderTitle, PopUpYesNo },
  data() {
    return {
      dataStudyDocs: [],
      isLoading: true,
      isOpenForm: false,
      selectedFile: null,
      selectedFileName: [],
      dataNewDocs: {
        type: "",
        url: "",
        name: "",
        documentName: "",
      },
      rules: {
        rulesSizeFileUpload: [
          (files) => {
            if (!files) return true; // If no files are selected, validation passes
            return (
              !files.some((file) => file.size > 104857600) ||
              "Avatar size should be less than 100 MB!"
            );
          },
        ],
        required: [(v) => !!v || "Trường nhập bắt buộc!"],
      },
      valid: true,
      docSelected: {},
    };
  },
  props: {},
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
    dialogTitle() {
      return !this.dataNewDocs?.url ? "Thêm tài liệu mới" : "Sửa tài liệu";
    },
  },
  mounted() {
    this.fetchDataDocuments();
  },
  methods: {
    async fetchDataDocuments() {
      let urlAPI = "/api/class/" + this.$route.params.id + "/files/get-all";

      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataStudyDocs = result.data;
      }

      this.isLoading = false;
    },
    handleGoBack() {
      this.$router.go(-1);
    },
    handleAddDocs() {
      this.isOpenForm = true;
    },
    handleFileUpload(event) {
      // Extract the selected file from the event object
      const file = event.target.files[0];

      // Store the selected file in the data property
      this.selectedFile = file;
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

      if (result.data) {
        this.dataNewDocs = {
          ...this.dataNewDocs,
          type: result.data?.type,
          url: result.data.url,
          name: result.data.name,
        };
      }
    },
    async handleCreateNewDocs() {
      if (!!this.valid && !!this.dataNewDocs.url) {
        let urlAPI = "/api/class/" + this.$route.params.id + "/files";

        const result = await apiCallerPost(
          urlAPI,
          JSON.parse(localStorage?.getItem("accessToken"))?.token,
          this.dataNewDocs
        );

        console.log("result ====", result.data);

        this.isOpenForm = false;
        this.fetchDataDocuments();
        this.dataNewDocs = {
          type: "",
          url: "",
          name: "",
        };
      }
    },
    checkImgTypeDocs(type) {
      if (
        type?.includes("wordprocessingml.document") ||
        type?.includes("/msword")
      ) {
        return ico_doc;
      } else if (type?.includes("image")) {
        return ico_img;
      } else if (type?.includes("pdf")) {
        return ico_pdf;
      } else if (type?.includes("/zip") || type?.includes("text/plain")) {
        return ico_document;
      } else if (
        type?.includes("spreadsheetml.sheet") ||
        type?.includes("spreadsheetml.sheet") ||
        type?.includes("/csv")
      ) {
        return ico_excel;
      } else if (
        type?.includes("presentationml.presentation") ||
        type?.includes("/vnd.ms-powerpoint")
      ) {
        return ico_ppt;
      }
    },
    handleOpenDocs(url) {
      window.open(url, "_blank");
    },
    async handleDeleteDoc(doc) {
      let urlAPI = "/api/class/" + this.$route.params.id + "/files/" + doc.id;

      const result = await apiCallerDelete(urlAPI);

      this.docSelected = {};
      this.fetchDataDocuments();
    },
    handleVisible(newValue) {
      if (!newValue) {
        this.docSelected = {};
      }
    },
  },
};
</script>

<style>
@import "./StudyDocuments.style.scss";
@import "../ClassStudents/ClassStudents.style.scss";
@import "../Class.style.scss";
</style>
