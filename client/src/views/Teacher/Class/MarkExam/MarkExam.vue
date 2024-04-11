vv
<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        title="Bài kiểm tra đã giao"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>

      <v-col
        class="d-flex flex-column class-list class-docs class-students align-center"
      >
        <div
          v-if="dataExamMark.length > 0"
          style="width: 80%; min-width: 28rem; max-width: 70rem; flex: 1"
          class="d-flex flex-column"
        >
          <!-- <h3 class="font-bold">Danh sách bài kiểm tra</h3> -->

          <v-col class="pa-8 pt-3">
            <v-card
              v-for="(exam, index) in dataExamMark"
              :key="index"
              class="cursor-pointer pa-3 pb-7 mb-5 d-flex student-item docs-item box-shadow"
              @click="handleNavigateDetailAssign(doc)"
            >
              <v-rowc
                class="pl-4 pr-4 pt-3 d-flex flex-row w-100 justify-space-between"
              >
                <div class="d-flex flex-row align-center w-100">
                  <div class="d-flex align-center justify-center">
                    <img
                      src="@/assets/images/img_exam.png"
                      alt="Icon Type Doc"
                    />
                  </div>
                  <div class="d-flex justify-space-between w-100">
                    <div class="d-flex flex-column">
                      <div
                        class="font-semi-bold student-name ml-2 mr-2 d-flex align-center"
                      >
                        {{ exam.testName }}
                      </div>
                      <div class="student-name ml-2 mr-2 d-flex align-center">
                        {{ exam.description ? `${exam.description}` : "" }}
                      </div>
                      <div class="student-name ml-2 mr-2 d-flex align-center">
                        Thời gian làm: {{ exam.time }}
                      </div>
                    </div>

                    <div class="d-flex align-center">
                      <v-btn
                        @click.stop.prevent="() => handleOpenModalAssign(exam)"
                        class="btn-delete-linear"
                        theme="dark"
                      >
                        <span>Giao bài</span>
                      </v-btn>
                    </div>
                  </div>
                </div>
              </v-rowc>
            </v-card>
          </v-col>
        </div>

        <v-row v-else-if="!isLoading" style="height: 90%" class="w-100 pt-15">
          <v-col>
            <div
              class="d-flex flex-column align-center class-empty class-docs_empty"
            >
              <img
                src="@/assets/images/empty_mark_excercise.png"
                alt="Img Empty Exam"
              />
              <h3
                class="d-flex d-flex flex-column font-semi-bold align-center w-100"
              >
                Bạn chưa có bài kiểm tra nào cần chấm!
              </h3>
            </div>
          </v-col></v-row
        >
      </v-col>
    </v-container>

    <!-- Popup -->
    <PopUpYesNo
      :msg="msgPopup"
      :visible="!!openPopup"
      :handleClickYes="
        () => {
          this.openPopup = false;
        }
      "
      hideBtnNo
      btnYes="Đóng"
      @update:visible="handleVisible"
    />
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet, apiCallerPost } from "@/services/teacher";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
export default {
  name: "AssignExam",
  components: { HeaderTitle, PopUpYesNo },
  data() {
    return {
      dataExamMark: [],
      isLoading: false,
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
      openPopup: false,
      isOpenForm: false,
      msgPopup: "",
    };
  },
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
  },
  mounted() {
    // this.fetchDataDocuments();
  },
  methods: {
    async fetchDataDocuments() {
      let urlAPI = "/api/testss";

      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataExams = result.data;
      }

      this.isLoading = false;
    },
    handleGoBack() {
      this.$router.go(-1);
    },
    handleVisible(newValue) {
      this.openPopup = newValue;
    },
  },
};
</script>

<style>
@import "../ClassStudents/ClassStudents.style.scss";
@import "../Class.style.scss";
@import "../StudyDocuments/StudyDocuments.style.scss";
</style>
