<template>
  <v-card class="height-100">
    <v-snackbar color="success" v-model="stateExams.success" location="top">
      Xóa bài kiểm tra thành công!
      <template v-slot:actions>
        <v-btn color="#EFE841" variant="text" @click="stateExams.success = false">
          Đóng
        </v-btn>
      </template>
    </v-snackbar>

    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle isSearch isCreate title="Quản lý bài kiểm tra" :createNew="createNewExam"
        @update:searchValue="handleSearchInputChange" :searchValue="searchValue" />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-row style="height: 90%" class="d-flex justify-center">
        <v-col class="height-100" cols="12" md="11">
          <v-data-table style="overflow-y: auto" class="height-100 scrollbar-custom v-data-table__exam" fixed-header
            :headers="headers" :items="filteredStateExams" :sort-by="[{ key: 'testId', order: 'asc' }]"
            :loading="isLoading" hover @click:row="(a, b) => handleClickItem(b.item)">
            <template v-slot:top>
              <PopUpYesNo msg="Bạn có chắc chắn muốn xóa bài kiểm tra này?" :visible="dialogDelete"
                :handleClickYes="deleteItemConfirm" :handleClickNo="closeDelete" @update:visible="handleVisible" />
            </template>
            <template v-slot:item.actions="{ item }">
              <v-icon color="#00bd7e" size="default" class="me-2" @click.stop.prevent="editItem(item)">
                mdi-pencil
              </v-icon>
              <v-icon color="red" size="default" @click.stop.prevent="deleteItem(item)">
                mdi-trash-can
              </v-icon>
            </template>
            <template v-slot:no-data>
              <!-- <v-btn color="primary" @click="initialize"> Reset </v-btn> -->
              <div class="exam_empty d-flex flex-column align-center">
                <img src="@/assets/images/img_empty_exam.png" alt="Empty Exam" class="img_empty" />
                <h3 class="font-bold">{{ msgEmptyExam }}</h3>
              </div>
            </template>
          </v-data-table>
        </v-col>
      </v-row>
      <PopUpYesNo msg="Bạn không có quyền chỉnh sửa bài kiểm tra này!" btnYes="Đồng ý" :visible="openPopupEdit"
        :handleClickYes="() => {
            this.openPopupEdit = false;
          }
          " hideBtnNo @update:visible="(newValue) => {
            this.openPopupEdit = newValue;
          }
          " />
    </v-container>
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
import { mapState } from "pinia";
import { authenticationRole } from "@/stores";
import { apiCallerGet, apiCallerDelete } from "@/services/teacher";
import { removeVietnameseDiacritics } from "@/base/helper";

export default {
  name: "ManageExam",
  components: {
    HeaderTitle,
    PopUpYesNo,
  },
  data: () => ({
    openPopupEdit: false,
    isLoading: false,
    selected: [],
    dialog: false,
    dialogDelete: false,
    headers: [
      {
        title: "ID",
        align: "start",
        sortable: false,
        key: "testId",
      },
      {
        title: "Tên bài kiểm tra",
        key: "testName",
      },
      { title: "Mô tả", key: "description", sortable: false },
      { title: "Thời gian (phút)", key: "time" },
      { title: "Số câu hỏi", key: "totalQuestions" },
      { title: "Ngày tạo", key: "created" },
    ],
    desserts: [],
    editedIndex: -1,
    stateExams: {
      data: [],
      loading: true,
      error: false,
      success: false,
      msg: "",
    },
    searchValue: "",
    msgEmptyExam: "Bạn chưa có bài kiểm tra nào!",
  }),
  props: {
    isAll: Boolean,
    isAdmin: Boolean,
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
    },
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
    filteredStateExams() {
      if (!this.searchValue) {
        // If searchValue is empty, return the original stateExams data
        return this.stateExams.data;
      } else {
        const normalizedSearchInput = removeVietnameseDiacritics(
          this.searchValue.toLowerCase()?.trim()
        );
        // If searchValue is not empty, filter stateExams based on testName
        return this.stateExams.data.filter((exam) => {
          const normalizedTitle = removeVietnameseDiacritics(
            exam.testName.toLowerCase()?.trim()
          );
          if (normalizedSearchInput == "d") {
            return (
              normalizedTitle.includes("d") || normalizedTitle.includes("đ")
            );
          } else {
            return normalizedTitle.includes(normalizedSearchInput);
          }
        });
      }
    },
  },
  created() {
    if (!this.isAll || !!this.isAdmin) {
      const actionHeader = {
        title: "Thao tác",
        key: "actions",
        sortable: false,
      };
      if (!this.headers.some((header) => header.key == "actions")) {
        this.headers.push(actionHeader); // Add "Thao tác" if it's not already in the headers array
      }
    } else {
      this.headers = this.headers.filter((header) => header.key != "actions"); // Remove "Thao tác" if it's present
    }
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
    searchValue(newVal) {
      this.handleSearchInputChange(newVal);
      if (!!newVal) {
        this.msgEmptyExam = "Không tìm thấy bài kiểm tra nào!";
      } else {
        this.msgEmptyExam = "Bạn chưa có bài kiểm tra nào!";
      }
    },
    isAll: {
      immediate: true, // Run the handler immediately when the component is created
      handler(newVal) {
        // React to changes in the isAll prop
        if (newVal) {
          this.headers = this.headers.filter(
            (header) => header.key != "actions"
          ); // Remove "Thao tác" if it's present
        } else {
          const actionHeader = {
            title: "Thao tác",
            key: "actions",
            sortable: false,
          };
          if (!this.headers.some((header) => header.key == "actions")) {
            this.headers.push(actionHeader); // Add "Thao tác" if it's not already in the headers array
          }
        }
        this.fetchDataExam();
      },
    },
  },

  // mounted() {
  //   this.fetchDataExam();
  // },

  methods: {
    async fetchDataExam() {
      this.isLoading = true;
      let urlFetch = this.isAdmin
        ? "/api/testss"
        : !this.isAll
          ? "/api/testss/find-by-user/" + this.authentication.user?.userId
          : "/api/testss";
      const result = await apiCallerGet(urlFetch);
      if (result?.data) {
        this.isLoading = false;
        this.stateExams.data = result.data;
      }
    },
    editItem(item) {
      this.$router.push(
        `/${this.authentication.user.role}/exam/edit/${item.testId}`
      );
    },

    deleteItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    async deleteItemConfirm() {
      // this.desserts.splice(this.editedIndex, 1);
      const result = await apiCallerDelete(
        "/api/testss/" + this.editedItem.testId
      );
      if (result.success) {
        this.stateExams.success = true;
        this.fetchDataExam();
      }
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.desserts[this.editedIndex], this.editedItem);
      } else {
        this.desserts.push(this.editedItem);
      }
      this.close();
    },
    createNewExam() {
      this.$router.push(`/${this.authentication.user.role}/exam/add`);
    },
    handleVisible(newValue) {
      this.dialogDelete = newValue;
    },
    handleSearchInputChange(valueSearch) {
      this.searchValue = valueSearch;
    },

    handleClickItem(item) {
      this.$router.push(`/teacher/exam/preview/${item.testId}`)
    },
  },
};
</script>

<style>
@import "./Exam.style.scss";
</style>
