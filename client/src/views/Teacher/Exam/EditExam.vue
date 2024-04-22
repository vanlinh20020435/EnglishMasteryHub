<template>
  <v-card class="height-100 create-exam" style="overflow-y: auto">
    <v-snackbar color="success" location="top">
      Chỉnh sửa bài kiểm tra thành công
      <template v-slot:actions>
        <v-btn color="#EFE841" variant="text"> Đóng </v-btn>
      </template>
    </v-snackbar>

    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        title="Chỉnh sửa bài kiểm tra"
        textBtn="Quay lại"
        :handleClickBtn="
          () => {
            this.$router.go(-1);
          }
        "
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-form v-model="valid" @submit.prevent="handleSaveExam">
        <v-row class="d-flex justify-center">
          <v-col
            style="padding-right: 2%"
            class="height-100 d-flex flex-column align-center"
            cols="12"
            md="8"
          >
            <v-col>
              <v-row class="d-flex align-center">
                <v-col cols="3">
                  <v-list-subheader class="label-required"
                    >Tên bài kiểm tra</v-list-subheader
                  >
                </v-col>

                <v-col cols="9">
                  <v-text-field
                    :rules="required"
                    placeholder="Tên bài kiểm tra"
                    hide-no-data
                    clearable
                    v-model="dataExam.testName"
                  >
                  </v-text-field>
                </v-col>
              </v-row>

              <v-row class="d-flex align-center">
                <v-col cols="3">
                  <v-list-subheader class="label-required"
                    >Thời gian làm bài</v-list-subheader
                  >
                </v-col>

                <v-col cols="9">
                  <v-text-field
                    :rules="required"
                    v-model="dataExam.time"
                    placeholder="Thời gian"
                    hide-no-data
                    clearable
                    single-line
                    type="number"
                  >
                  </v-text-field>
                </v-col>
              </v-row>

              <v-row
                v-if="!!dataExam?.totalQuestions"
                class="d-flex align-center"
              >
                <v-col cols="3">
                  <v-list-subheader class="label-required"
                    >Tổng số câu hỏi</v-list-subheader
                  >
                </v-col>

                <v-col cols="9">
                  <v-text-field
                    v-model="dataExam.totalQuestions"
                    hide-no-data
                    single-line
                    type="number"
                    variant="solo"
                    readonly
                  >
                  </v-text-field>
                </v-col>
              </v-row>

              <v-row class="d-flex align-center">
                <v-col cols="3">
                  <v-list-subheader>Mô tả</v-list-subheader>
                </v-col>

                <v-col cols="9">
                  <v-textarea
                    placeholder="Mô tả"
                    rows="4"
                    max-rows="4"
                    variant="filled"
                    auto-grow
                    v-model="dataExam.description"
                    hide-no-data
                    counter
                    hide-details
                  >
                  </v-textarea>
                </v-col>
              </v-row>
            </v-col>

            <v-col style="padding-top: 5rem">
              <v-col
                class="d-flex flex-row align-center"
                style="padding-bottom: 2rem"
              >
                <v-col cols="9">
                  <v-row class="d-flex align-center">
                    <v-col cols="2">
                      <v-list-subheader>Dạng bài</v-list-subheader>
                    </v-col>
                    <v-col cols="4">
                      <v-autocomplete
                        v-model="selectedSkill"
                        :items="dataSkills"
                        hide-details
                        auto-select-first
                      >
                      </v-autocomplete>
                    </v-col>
                    <v-col cols="6">
                      <v-autocomplete
                        v-model="selectedTypeSkill"
                        hide-details
                        :items="filteredDataTypes"
                        item-text="title"
                        return-object
                      >
                      </v-autocomplete>
                    </v-col>
                  </v-row>
                </v-col>
                <v-col cols="3" class="d-flex justify-end">
                  <v-btn @click="handleAddSkill" color="#00bd7e" theme="dark"
                    >Thêm mới</v-btn
                  >
                </v-col>
              </v-col>

              <v-row>
                <v-col cols="12" class="listSkill">
                  <v-row
                    v-for="(skill, index) in dataSkills"
                    :key="index"
                    class="d-flex align-center"
                  >
                    <v-col class="pa-0 mt-2 mb-3 toolbar-with-border">
                      <v-toolbar color="#fff" flat>
                        <v-toolbar-title>{{ skill.title }}</v-toolbar-title>
                      </v-toolbar>
                      <v-col
                        v-for="(question, indexQuestion) in dataExam.questions"
                        :key="indexQuestion"
                        class="pa-0"
                      >
                        <!-- Pronunciation -->
                        <v-col
                          class="pa-2 pt-0"
                          v-if="
                            question.skill == skill.title.toLowerCase() &&
                            question.skill == 'pronunciation'
                          "
                          ><PronunManage
                            :skillTypeSelected="
                              question?.title || selectedTypeSkill
                            "
                            :question="question"
                            :handleDeleteSkill="
                              () => removePronun1(indexQuestion)
                            "
                            @deleteQuestion="
                              handleDeleteQuestionInPronun1(
                                indexQuestion,
                                $event
                              )
                            "
                            @updateGroupTitleQuestion="
                              handleUpdateGroupTitleQuestion(
                                indexQuestion,
                                $event
                              )
                            "
                          />
                        </v-col>
                        <!-- Grammar -->
                        <v-col
                          class="pa-2 pt-0"
                          v-if="
                            question.skill == skill.title.toLowerCase() &&
                            question.skill == 'grammar'
                          "
                          ><GrammarManage
                            :skillTypeSelected="
                              question?.title || selectedTypeSkill
                            "
                            :question="question"
                            :handleDeleteSkill="
                              () => removePronun1(indexQuestion)
                            "
                            @deleteQuestion="
                              handleDeleteQuestionInPronun1(
                                indexQuestion,
                                $event
                              )
                            "
                            @updateGroupTitleQuestion="
                              handleUpdateGroupTitleQuestion(
                                indexQuestion,
                                $event
                              )
                            "
                          />
                        </v-col>

                        <!-- Reading -->
                        <v-col
                          class="pa-2 pt-0"
                          v-if="
                            question.skill == skill.title.toLowerCase() &&
                            question.skill == 'reading'
                          "
                          ><ReadingManage
                            :skillTypeSelected="
                              question?.title || selectedTypeSkill
                            "
                            :question="question"
                            :handleDeleteSkill="
                              () => removePronun1(indexQuestion)
                            "
                            @deleteQuestion="
                              handleDeleteQuestionInPronun1(
                                indexQuestion,
                                $event
                              )
                            "
                            @updateGroupTitleQuestion="
                              handleUpdateGroupTitleQuestion(
                                indexQuestion,
                                $event
                              )
                            "
                          />
                        </v-col>

                        <!-- Listening -->
                        <v-col
                          class="pa-2 pt-0"
                          v-if="
                            question.skill == skill.title.toLowerCase() &&
                            question.skill == 'listening'
                          "
                          ><ListeningManage
                            :skillTypeSelected="
                              question?.title || selectedTypeSkill
                            "
                            :question="question"
                            :handleDeleteSkill="
                              () => removePronun1(indexQuestion)
                            "
                            @deleteQuestion="
                              handleDeleteQuestionInPronun1(
                                indexQuestion,
                                $event
                              )
                            "
                            @updateGroupTitleQuestion="
                              handleUpdateGroupTitleQuestion(
                                indexQuestion,
                                $event
                              )
                            "
                          />
                        </v-col>

                        <!-- Writing -->
                        <v-col
                          class="pa-2 pt-0"
                          v-if="
                            question.skill == skill.title.toLowerCase() &&
                            question.skill == 'writing'
                          "
                          ><WritingManage
                            :skillTypeSelected="
                              question?.title || selectedTypeSkill
                            "
                            :question="question"
                            :handleDeleteSkill="
                              () => removePronun1(indexQuestion)
                            "
                            @deleteQuestion="
                              handleDeleteQuestionInPronun1(
                                indexQuestion,
                                $event
                              )
                            "
                            @updateGroupTitleQuestion="
                              handleUpdateGroupTitleQuestion(
                                indexQuestion,
                                $event
                              )
                            "
                          />
                        </v-col>
                      </v-col> </v-col
                  ></v-row>
                </v-col>
              </v-row>
            </v-col>
          </v-col>
          <v-col class="height-100" cols="12" md="3">
            <v-card
              class="mx-auto card_tips"
              max-width="344"
              prepend-icon="mdi-lightbulb-on"
              title="Tips: Hướng dẫn tạo dữ liệu !!!"
            >
              <v-card-text
                >{Đáp án đúng}: Quy ước sử dụng cho các dạng bài điền từ, từ cần
                điền sẽ nằm trong dấu {}, khi hiển thị đoạn {Đáp án đúng} sẽ
                được thay thế bằng chỗ trống ____</v-card-text
              >
              <v-card-text
                >{Đáp án đúng / đáp án khác}: Quy ước sử dụng cho các dạng bài
                điền từ, nếu có nhiều hơn 1 đáp án, giữa các đáp án phân cách
                bằng dấu /</v-card-text
              >
              <v-card-text
                >[Nội dung hiển thị]: Quy ước sử dụng cho các dạng bài gạch
                chân. Phần nội dung trong [] sẽ được gạch chân khi hiển
                thị.</v-card-text
              >
            </v-card>
          </v-col>
        </v-row>
        <div
          style="max-width: 95%"
          color="#fff"
          class="d-flex flex-row mt-15 pb-10"
        >
          <v-spacer></v-spacer>
          <v-row class="d-flex justify-end">
            <v-btn
              @click="
                () => {
                  this.$router.go(-1);
                }
              "
              color="#F1F2F7"
              theme="dark"
              class="mr-10"
              >Hủy</v-btn
            >
            <v-btn type="submit" color="#00bd7e" theme="dark">Lưu</v-btn>
          </v-row>
        </div>
      </v-form>
    </v-container>
    <PopUpYesNo
      msg="Chỉnh sửa bài kiểm tra thành công"
      :visible="dialogCreateSuccess"
      btnYes="Đồng ý"
      hideBtnNo
      :handleClickYes="
        () => {
          this.$router.go(-1);
        }
      "
    />
  </v-card>
</template>

<script>
import { authenticationRole } from "@/stores";
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import {
  PronunManage,
  GrammarManage,
  ReadingManage,
  WritingManage,
  ListeningManage,
} from "@/components/skillManage";
import { apiCallerGet, apiCallerPost, apiCallerPut } from "@/services/teacher";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";

export default {
  name: "EditExam",
  components: {
    HeaderTitle,
    PopUpYesNo,
    PronunManage,
    GrammarManage,
    ReadingManage,
    WritingManage,
    ListeningManage,
  },
  data: () => ({
    dataExam: {
      testName: "",
      time: "",
      totalQuestions: "",
      description: "",
      questions: [],
    },
    dataSkills: [
      {
        id: 1,
        title: "Pronunciation",
        dataTypes: [
          {
            id: 1,
            title: "Listen to each audio and choose the word you hear",
            option: 2,
          },
          // {
          //   id: 2,
          //   title: "Choose the correct sound",
          //   option: 2,
          // },
          {
            id: 3,
            title: "Listen and write the words you hear",
            option: 2,
          },
          {
            id: 4,
            title: "Listen and choose the correct intonation",
            option: 2,
          },
        ],
      },
      {
        id: 2,
        title: "Grammar",
        dataTypes: [
          {
            id: 1,
            title: "Fill the blank with the correct form of the words",
            option: 0,
          },
          {
            id: 2,
            title: "Choose the underlined part that needs correction",
            option: 4,
          },
          {
            id: 3,
            title: "Rewrite the sentences beginning with the given words",
            option: 0,
          },
          {
            id: 4,
            title: "Choose the best option to complete each sentence",
            option: 4,
          },
        ],
      },
      {
        id: 3,
        title: "Reading",
        dataTypes: [
          {
            id: 1,
            title:
              "Read the passage and choose the best answer to each question",
            option: 4,
          },
          {
            id: 2,
            title: "Read the passage and answer the questions",
            option: 0,
          },
          {
            id: 3,
            title: "Choose the suitable word(s) for each blank",
            option: 4,
          },
          {
            id: 4,
            title:
              "Read the passage and decide whether the statements are True or False",
            option: 0,
          },
        ],
      },
      {
        id: 4,
        title: "Listening",
        dataTypes: [
          {
            id: 1,
            title: "Listen and answer the questions",
            option: 1,
          },
          {
            id: 2,
            title: "Listen and fill in the sentences",
            option: 1,
          },
          {
            id: 3,
            title:
              "Listen and decide whether the following sentences are True or False. ",
            option: 2,
          },
          {
            id: 4,
            title: "Listen and choose the best option",
            option: 4,
          },
        ],
      },
      {
        id: 5,
        title: "Writing",
        dataTypes: [
          {
            id: 1,
            title: "Write complete sentences using the given words",
            option: 0,
          },
          {
            id: 2,
            title: "Use the given words to make complete sentences",
            option: 0,
          },
          {
            id: 3,
            title:
              "Write about the advantages and disadvantages of playing sports",
            option: 1,
          },
        ],
      },
    ],

    selectedSkill: "",
    selectedTypeSkill: {
      id: 1,
      title: "",
    },
    selectedTypeQuestion: "",
    required: [
      (v) => {
        if (v) return true;
        return "Trường nhập bắt buộc!";
      },
    ],
    valid: true,
    dialogCreateSuccess: false,
  }),
  created() {
    // Auto-select the first item's title
    if (this.dataSkills.length > 0) {
      this.selectedSkill = this.dataSkills[0].title;
      this.selectedTypeSkill = this.dataSkills[0].dataTypes[0];
    }
  },
  computed: {
    filteredDataTypes() {
      if (this.selectedSkill) {
        const selectedItem = this.dataSkills.find(
          (item) => item.title == this.selectedSkill
        );
        return selectedItem ? selectedItem.dataTypes : [];
      } else {
        return [];
      }
    },
  },
  watch: {
    selectedSkill(newValue, oldValue) {
      // Reset selectedDataType when selectedItem changes
      this.selectedTypeSkill = null;
      // Find the selected item in the items array
      const selectedItem = this.dataSkills.find(
        (item) => item.title === newValue
      );

      // If the selected item is found, set selectedTypeSkill to its type
      if (selectedItem) {
        this.selectedTypeSkill = selectedItem?.dataTypes[0]; // Assuming selectedItem has a 'type' property
      }
    },
  },
  mounted() {
    this.fetchDetailExam();
  },
  methods: {
    async fetchDetailExam() {
      const urlAPI = "/api/testss/" + this.$route.params.id;

      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataExam = result.data;
      }
    },
    handleAddSkill() {
      // Add Pronun1Manage component to dataExam?.questions array

      const questions = [
        {
          title: "Question 1",
          numOptions: this.selectedTypeSkill.option,
          options: Array.from(
            { length: this.selectedTypeSkill.option },
            (_, i) => ({
              option: "",
            })
          ),
          answers: [
            {
              answer: "",
              explanation: "",
            },
          ],
        },
      ];
      const selectedItem = this.dataSkills.find(
        (item) => item.title == this.selectedSkill
      );

      const selectedItemSkill = selectedItem.dataTypes.find(
        (item) => item.title == this.selectedTypeSkill.title
      );

      this.dataExam.questions.push({
        skill: this.selectedSkill.toLowerCase(),
        type: selectedItemSkill?.id || "1",
        title: this.selectedTypeSkill.title,
        subQuestions: questions, // Assign the questions array to the dataExam?.questions item
      });
    },
    removePronun1(index) {
      // Remove Pronun1Manage component at specified index from dataExam?.questions array
      this.dataExam.questions.splice(index, 1);
    },
    handleDeleteQuestionInPronun1(pronun1Index, questionIndex) {
      // Remove the question at the specified index from the dataExam?.questions array
      this.dataExam.questions[pronun1Index].subQuestions?.splice(
        questionIndex,
        1
      );
    },
    handleUpdateGroupTitleQuestion(index, updatedValue) {
      // Update the groupTitleQuestion property in dataExam?.questions at the specified index

      this.dataExam.questions[index].title = updatedValue;
      this.$emit("updateGroupTitleQuestion", updatedValue); // Emit the event
    },
    async handleSaveExam() {
      console.log('123')
      if (!!this.valid) {
        const convertQuestion = (question, questionParent) => ({
          content: question?.content?.trim() || '',
          skill: questionParent?.skill?.trim() || '',
          type: `${questionParent?.type?.toString()?.trim()}` || '1',
          description: question?.description?.trim(),
          answers: question?.answers.map((answer) => ({
            answer: answer?.answer?.trim(),
            explanation: answer?.explanation?.trim() || "",
          })),
          options: question.options.map((option) => ({
            option: option?.option?.trim(),
          })),
          files: !!question?.files?.type || question?.files?.[0]?.type
            ? [
                {
                  type: question?.files?.type || question?.files?.[0]?.type,
                  url: question?.files?.url || question?.files?.[0]?.url,
                  name: question?.files?.name || question?.files?.[0]?.name,
                },
              ]
            : [],
        });

        const convertedData = this.dataExam.questions.map((item) => ({
          content: item?.content?.trim() || '',
          description: item?.description?.trim() || '',
          title: item?.title?.trim(),
          type: `${item?.type?.toString()?.trim()}`,
          skill: item?.skill?.trim(),
          time: 0,
          requiresGrading: item?.requiresGrading || false,
          subQuestions: item.subQuestions.map((question) =>
            convertQuestion(question, item)
          ),
          files: !!item?.files?.type || item?.files?.[0]?.type
            ? [
                {
                  type: item?.files?.type || item?.files?.[0]?.type,
                  url: item?.files?.url || item?.files?.[0]?.url,
                  name: item?.files?.name || item?.files?.[0]?.name,
                },
              ]
            : [],
        }));

        const body = {
          testName: this.dataExam.testName?.trim(),
          time: this.dataExam.time,
          status: "0",
          description: this.dataExam.description?.trim(),
          questions: convertedData,
        };

        const urlAPI = "/api/testss/" + this.$route.params.id;

        const result = await apiCallerPut(urlAPI, body);

        if (result.success) {
          this.dialogCreateSuccess = true;
        }
      }
    },
  },
};
</script>

<style>
@import "./Exam.style.scss";
</style>
