<template>
  <v-card class="height-100" style="overflow-y: auto">
    <v-snackbar color="success" location="top">
      Thêm bài kiểm tra thành công
      <template v-slot:actions>
        <v-btn color="#EFE841" variant="text"> Đóng </v-btn>
      </template>
    </v-snackbar>

    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle title="Thêm bài kiểm tra mới" />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-row style="height: 90%" class="d-flex justify-center">
        <v-col
          style="padding-right: 2%"
          class="height-100 d-flex flex-column align-center"
          cols="12"
          md="8"
        >
          <v-col>
            <v-row class="d-flex align-center">
              <v-col cols="3">
                <v-list-subheader>Tên bài kiểm tra</v-list-subheader>
              </v-col>

              <v-col cols="9">
                <v-text-field
                  :rules="[required]"
                  :model-value="dataExam?.testName"
                  placeholder="Tên bài kiểm tra"
                  hide-no-data
                  clearable
                >
                </v-text-field>
              </v-col>
            </v-row>

            <v-row class="d-flex align-center">
              <v-col cols="3">
                <v-list-subheader>Thời gian làm bài</v-list-subheader>
              </v-col>

              <v-col cols="9">
                <v-text-field
                  :rules="[required]"
                  :model-value="dataExam?.time"
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
                <v-list-subheader>Tổng số câu hỏi</v-list-subheader>
              </v-col>

              <v-col cols="9">
                <v-text-field
                  :model-value="dataExam?.totalQuestions ?? 0"
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
                  :model-value="dataExam?.description"
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
                    >
                    </v-autocomplete>
                  </v-col>
                  <v-col cols="6">
                    <v-autocomplete
                      v-model="selectedTypeSkill"
                      hide-details
                      :items="filteredDataTypes"
                    >
                    </v-autocomplete>
                  </v-col>
                </v-row>
              </v-col>
              <v-col cols="3" class="d-flex justify-end">
                <v-btn color="#00bd7e" theme="dark">Thêm mới</v-btn>
              </v-col>
            </v-col>

            <v-row>
              <v-col cols="9">
                <v-row class="d-flex align-center">
                  <v-col cols="2">
                    <v-list-subheader>Dạng bài</v-list-subheader>
                  </v-col>
                  <v-col cols="4">
                    <v-autocomplete auto-select-first> </v-autocomplete>
                  </v-col>
                  <v-col cols="6">
                    <v-autocomplete model-value="Pronunciation">
                    </v-autocomplete>
                  </v-col>
                </v-row>
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
              điền sẽ nằm trong dấu {}, khi hiển thị đoạn {Đáp án đúng} sẽ được
              thay thế bằng chỗ trống ____</v-card-text
            >
            <v-card-text
              >{Đáp án đúng / đáp án khác}: Quy ước sử dụng cho các dạng bài
              điền từ, nếu có nhiều hơn 1 đáp án, giữa các đáp án phân cách bằng
              dấu /</v-card-text
            >
            <v-card-text
              >[Nội dung hiển thị]: Quy ước sử dụng cho các dạng bài gạch chân.
              Phần nội dung trong [] sẽ được gạch chân khi hiển
              thị.</v-card-text
            >
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import { authenticationRole } from "@/stores";
import HeaderTitle from "@/components/header/HeaderTitle.vue";
export default {
  name: "CreateExam",
  components: {
    HeaderTitle,
  },
  data: () => ({
    dataExam: {
      testName: "",
      time: "",
      totalQuestions: "",
      description: "",
    },
    dataSkills: [
      {
        id: 1,
        title: "Pronunciation",
        dataTypes: [
          {
            id: 1,
            title: "Listen to each audio and choose the word you hear",
          },
          {
            id: 2,
            title: "Choose the correct sound",
          },
          {
            id: 3,
            title: "Listen and write the words you hear",
          },
          {
            id: 4,
            title: "Listen and choose the correct intonation",
          },
          {
            id: 5,
            title: "Listen and stress the words",
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
          },
          {
            id: 2,
            title: "Fill in the sentences with the suitable given words",
          },
          {
            id: 3,
            title: "Rewrite the sentences beginning with the given words",
          },
          {
            id: 4,
            title: "Choose the best option to complete each sentence",
          },
          {
            id: 5,
            title: "Put the words in the correct order",
          },
        ],
      },
      {
        id: 3,
        title: "Reading",
        dataTypes: [
          {
            id: 1,
            title: "Choose the best answer to each question",
          },
          {
            id: 2,
            title: "Read the passage and answer the questions",
          },
          {
            id: 3,
            title: "Match each definition with its corresponding word",
          },
          {
            id: 4,
            title: "Choose a suitable heading for each paragraph",
          },
          {
            id: 5,
            title: "True/ False",
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
          },
          {
            id: 2,
            title: "Listen and fill in the sentences",
          },
          {
            id: 3,
            title: "True/ False",
          },
          {
            id: 4,
            title: "Listen and choose the best option",
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
          },
          {
            id: 2,
            title: "Use the given words to make complete sentences",
          },
          {
            id: 3,
            title:
              "Write about the advantages and disadvantages of playing sports",
          },
        ],
      },
    ],
    selectedSkill: "",
    selectedTypeSkill: "",
  }),
  methods: {
    required(v) {
      return !!v || "Trường nhập bắt buộc!";
    },
  },
  created() {
    // Auto-select the first item's title
    if (this.dataSkills.length > 0) {
      this.selectedSkill = this.dataSkills[0].title;
    }
    if (this.dataSkills.length > 0) {
      this.selectedTypeSkill = this.dataSkills[0].dataTypes[0].title;
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

      console.log("selectedItem", selectedItem);

      // If the selected item is found, set selectedTypeSkill to its type
      if (selectedItem) {
        this.selectedTypeSkill = selectedItem?.dataTypes[0]?.title; // Assuming selectedItem has a 'type' property
      }
    },
  },
};
</script>

<style>
@import "./Exam.style.scss";
</style>
