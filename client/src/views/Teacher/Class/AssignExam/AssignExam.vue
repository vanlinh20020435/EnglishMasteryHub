vv
<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        title="Giao bài"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>

      <v-col
        class="d-flex flex-column class-list class-docs class-students align-center"
      >
        <div
          v-if="dataExams.length > 0"
          style="width: 80%; min-width: 28rem; max-width: 70rem; flex: 1"
          class="d-flex flex-column"
        >
          <!-- <h3 class="font-bold">Danh sách bài kiểm tra</h3> -->

          <v-col class="pa-8 pt-3">
            <v-card
              v-for="(exam, index) in dataExams"
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

        <v-row v-else-if="!isLoading" style="height: 90%" class="w-100">
          <v-col>
            <div
              class="d-flex flex-column align-center class-empty class-docs_empty"
            >
              <img
                src="@/assets/images/img_empty_exam.png"
                alt="Img Empty Exam"
              />
              <h3
                class="d-flex d-flex flex-column font-semi-bold align-center w-100"
              >
                Hệ thống chưa có bài kiểm tra nào!
              </h3>
            </div>
          </v-col></v-row
        >
      </v-col>

      <v-col class="pa-5 pt-8 pb-8" style="flex: 0">
        <v-row class="d-flex align-center justify-end">
          <v-btn
            @click="() => handleNavigateCreateExam()"
            color="#00bd7e"
            theme="dark"
            class="btn-add-question"
            >Thêm bài kiểm tra</v-btn
          >
        </v-row>
      </v-col>
    </v-container>

    <v-dialog v-model="isOpenForm" max-width="500px">
      <v-card class="d-flex pa-8 pt-6 pb-6">
        <v-card-title class="text-center pb-8">Giao bài kiểm tra</v-card-title>

        <v-form v-model="valid" @submit.prevent="handleAssignExam">
          <v-col class="pb-7">
            <v-row>
              <h4 class="font-semi-bold">
                Tên bài:
                <span style="font-style: italic">{{
                  examSelected.testName
                }}</span>
              </h4>
            </v-row>
          </v-col>
          <v-col>
            <v-row class="pt-2 pb-2">
              <h4 class="font-semi-bold">Ngày bắt đầu</h4>
            </v-row>

            <v-row>
              <v-dialog
                ref="dialog"
                v-model="openDatePickerStart"
                :return-value.sync="dateAssignStart"
                persistent
                auto
                width="290px"
              >
                <template v-slot:activator="{ startDate }">
                  <v-text-field
                    v-model="dateAssignStartComputed"
                    label="Ngày bắt đầu"
                    readonly
                    v-bind="startDate"
                    clearable
                    @click="() => (openDatePickerStart = true)"
                    variant="outlined"
                  ></v-text-field>
                </template>
                <v-date-picker
                  v-model="dateAssignStart"
                  year-icon="mdi-calendar-blank"
                  prev-icon="mdi-skip-previous"
                  next-icon="mdi-skip-next"
                  scrollable
                  @update:model-value="() => (openDatePickerStart = false)"
                ></v-date-picker>
              </v-dialog>
            </v-row>
          </v-col>

          <v-col>
            <v-row class="pb-2">
              <h4 class="font-semi-bold">Ngày kết thúc</h4>
            </v-row>

            <v-row>
              <v-dialog
                ref="dialog"
                v-model="openDatePickerEnd"
                :return-value.sync="dateAssignEnd"
                persistent
                auto
                width="290px"
              >
                <template v-slot:activator="{ endDate }">
                  <v-text-field
                    v-model="dateAssignEndComputed"
                    label="Ngày kết thúc"
                    readonly
                    v-bind="endDate"
                    clearable
                    @click="() => (openDatePickerEnd = true)"
                    variant="outlined"
                  ></v-text-field>
                </template>
                <v-date-picker
                  v-model="dateAssignEnd"
                  year-icon="mdi-calendar-blank"
                  prev-icon="mdi-skip-previous"
                  next-icon="mdi-skip-next"
                  scrollable
                  @update:model-value="() => (openDatePickerEnd = false)"
                ></v-date-picker>
              </v-dialog>
            </v-row>
          </v-col>

          <v-col class="pt-10 mt-5" style="flex: 0">
            <v-row class="d-flex align-center justify-space-between">
              <v-btn
                @click="() => (this.isOpenForm = false)"
                color="#F1F2F7"
                theme="dark"
                class="btn-action-dialog"
                >Hủy</v-btn
              >
              <v-btn
                type="submit"
                color="#00bd7e"
                theme="dark"
                class="btn-action-dialog"
                >Hoàn tất</v-btn
              >
            </v-row>
          </v-col>
        </v-form>
      </v-card>
    </v-dialog>

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
      dataExams: [],
      isLoading: true,
      valid: true,
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
      examSelected: {
        startDate: this.formatDate(new Date()),
        endDate: this.formatDate(
          new Date(Date.now() + 2 * 24 * 60 * 60 * 1000)
        ),
      },
      openPopup: false,
      isOpenForm: false,
      openDatePickerStart: false,
      openDatePickerEnd: false,
      dateAssignStart: new Date(),
      dateAssignEnd: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000),
      dateAssignStartComputed: new Date()
        .toLocaleDateString("en-GB", this.optionsFormatDate)
        .substr(0, 10),
      dateAssignEndComputed: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000)
        .toLocaleDateString("en-GB", this.optionsFormatDate)
        .substr(0, 10),
      msgPopup: [],
      optionsFormatDate: { day: "2-digit", month: "2-digit", year: "numeric" },
    };
  },
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
  },
  mounted() {
    this.fetchDataDocuments();
  },
  watch: {
    dateAssignStart(val) {
      const currentTime = new Date();
      val.setHours(currentTime.getHours());
      val.setMinutes(currentTime.getMinutes());
      val.setSeconds(currentTime.getSeconds());
      this.dateAssignStartComputed = val
        ? new Date(val)
            .toLocaleDateString("en-GB", this.optionsFormatDate)
            .substr(0, 10)
        : null;

      this.examSelected.startDate = this.formatDate(new Date(val));
    },
    dateAssignEnd(val) {
      const currentTime = new Date();
      val.setHours(currentTime.getHours());
      val.setMinutes(currentTime.getMinutes());
      val.setSeconds(currentTime.getSeconds());

      this.dateAssignEndComputed = val
        ? new Date(val)
            .toLocaleDateString("en-GB", this.optionsFormatDate)
            .substr(0, 10)
        : null;
      this.examSelected.endDate = this.formatDate(new Date(val));
    },
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
    handleNavigateCreateExam() {
      this.$router.push(`/${this.authentication.user.role}/exam/add`);
    },
    handleOpenModalAssign(exam) {
      this.examSelected = {
        ...this.examSelected,
        testId: exam.testId,
        testName: exam.testName,
      };

      this.dateAssignStart = new Date();
      this.dateAssignEnd = new Date(Date.now() + 2 * 24 * 60 * 60 * 1000);
      this.dateAssignStartComputed = new Date()
        .toLocaleDateString("en-GB", this.optionsFormatDate)
        .substr(0, 10);
      this.dateAssignEndComputed = new Date(
        Date.now() + 2 * 24 * 60 * 60 * 1000
      )
        .toLocaleDateString("en-GB", this.optionsFormatDate)
        .substr(0, 10);
      this.isOpenForm = true;
    },
    async handleAssignExam() {
      if (this.validateEndDate()) {
        let urlAPI =
          "/api/class/" +
          this.$route.params.id +
          "/tests/" +
          this.examSelected?.testId;

        const body = {
          startDate: this.examSelected?.startDate,
          endDate: this.examSelected?.endDate,
        };

        const result = await apiCallerPost(
          urlAPI,
          JSON.parse(localStorage?.getItem("accessToken"))?.token,
          body
        );

        if (result.success) {
          this.msgPopup = ["Giao bài kiểm tra thành công!"];
        } else if (result.msg) {
          result.msg?.includes("already exists")
            ? (this.msgPopup = [
                "Bài kiểm tra này đã được giao trước đó.",
                "Vui lòng giao bài kiểm tra khác!",
              ])
            : (this.msgPopup = ["Giao bài kiểm tra thất bại!"]);
        }
        this.isOpenForm = false;
        this.examSelected = {};
        this.openPopup = true;
      }
    },
    validateEndDate() {
      const startDate = this.dateAssignStart;
      const endDate = this.dateAssignEnd;
      return endDate > startDate;
    },
    formatDate(date) {
      // Ensure two digits with leading zero if necessary
      const padZero = (num) => num.toString().padStart(2, "0");

      const day = padZero(date.getDate());
      const month = padZero(date.getMonth() + 1); // Month is zero-indexed, so add 1
      const year = date.getFullYear();
      const hours = padZero(date.getHours());
      const minutes = padZero(date.getMinutes());

      return `${day}/${month}/${year} ${hours}:${minutes}`;
    },
  },
};
</script>

<style>
@import "./AssignExam.style.scss";
@import "../ClassStudents/ClassStudents.style.scss";
@import "../Class.style.scss";
@import "../StudyDocuments/StudyDocuments.style.scss";
</style>
