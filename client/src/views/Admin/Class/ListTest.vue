<template>
  <v-card style="margin-top: 16px">
    <v-toolbar color="#ebebeba3" flat>
      <v-toolbar-title>Danh sách bài kiểm tra</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon="mdi-filter" style="margin-right: 8px" :color="isOpenFilter ? '#00bd7e' : ''"
        @click="() => (isOpenFilter = !isOpenFilter)"></v-btn>
      <v-btn class="mb-2" color="#00bd7e" dark variant="outlined" @click="() => isOpenSelectTest = true">
        Thêm mới
      </v-btn>
    </v-toolbar>
    <v-row v-if="isOpenFilter" style="padding: 8px; margin-top: 8px">
      <v-col cols="12" md="6">
        <v-text-field v-model="nameFilter" label="Tên bài kiểm tra" clearable></v-text-field>
      </v-col>
      <v-col cols="12" md="6">
        <v-text-field v-model="creatorFilter" label="Người tạo" clearable></v-text-field>
      </v-col>
    </v-row>
    <v-data-table :loading="isLoadingTests" :headers="testsHeaders" :items="filteredTests"
      :sort-by="[{ key: 'studentId', order: 'asc' }]" @click:row="(a, b) => handleClickItem(b.item)">
      <template v-slot:item.actions="{ item }">
        <v-icon size="small" color="error" @click.stop.prevent="() => openDelete(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-card>
  <v-dialog v-model="isOpenSelectTest" max-width="500px">
    <v-card>
      <v-card-title>Chọn bài kiểm tra</v-card-title>
      <v-card-text>
        <v-select label="Select" v-model="selectedTest" :items="listSelectTest" clearable>
        </v-select>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn variant="tonal" @click="cancelSelectTest">
          Hủy
        </v-btn>
        <v-btn color="success" variant="flat" :disabled="!selectedTest" @click="submitSelectTest">
          Xác nhận
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog v-model="isOpenAddTest" max-width="500px">
    <v-card>
      <v-card-title>{{ selectedTest?.testName }}</v-card-title>
      <v-form v-model="addTestValid" @submit.prevent="handleAssignExam">
        <v-card-text>
          <v-dialog ref="dialog" v-model="openDatePickerStart" :return-value.sync="dateAssignStart" persistent auto
            width="290px">
            <template v-slot:activator="{ startDate }">
              <v-text-field v-model="dateAssignStartComputed" label="Ngày bắt đầu" readonly v-bind="startDate" clearable
                @click="() => (openDatePickerStart = true)" variant="outlined"></v-text-field>
            </template>
            <v-date-picker v-model="dateAssignStart" year-icon="mdi-calendar-blank" prev-icon="mdi-skip-previous"
              next-icon="mdi-skip-next" scrollable
              @update:model-value="() => (openDatePickerStart = false)"></v-date-picker>
          </v-dialog>
          <v-dialog ref="dialog" v-model="openDatePickerEnd" :return-value.sync="dateAssignEnd" persistent auto
            width="290px">
            <template v-slot:activator="{ endDate }">
              <v-text-field v-model="dateAssignEndComputed" label="Ngày kết thúc" readonly v-bind="endDate" clearable
                @click="() => (openDatePickerEnd = true)" variant="outlined"></v-text-field>
            </template>
            <v-date-picker v-model="dateAssignEnd" year-icon="mdi-calendar-blank" prev-icon="mdi-skip-previous"
              next-icon="mdi-skip-next" scrollable
              @update:model-value="() => (openDatePickerEnd = false)"></v-date-picker>
          </v-dialog>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="() => (this.isOpenAddTest = false)" variant="tonal">Hủy</v-btn>
          <v-btn type="submit" color="success" variant="flat">Hoàn tất</v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
  <PopUpYesNo :msg="`Bạn có chắc chắn muốn xoá bài kiểm tra ${delettingItem.testName}?`" :visible="isOpenDelete"
    :handleClickYes="deleteItem" :handleClickNo="() => (isOpenDelete = false)" />
</template>

<script>
import PopUpYesNo from '@/components/popup/PopUpYesNo.vue';
import { getTestsOfClass, getTests, addTestToClass, deleteTestInClass } from "@/services";
import { authenticationRole, testStore, toastStore } from "@/stores";
import { mapState } from "pinia";
const optionsFormatDate = { day: "2-digit", month: "2-digit", year: "numeric" }
export default {
  components: {
    PopUpYesNo,
  },
  data: () => ({
    isLoadingTests: false,
    tests: [],
    testsHeaders: [
      {
        title: "Test ID",
        align: "start",
        key: "testId",
      },
      {
        title: "Tên bài kiểm tra",
        key: "testName",
        sortable: false,
      },
      { title: "Người tạo", key: "creator", sortable: false },
      {
        title: "Thời gian (phút)",
        key: "time",
        sortable: false,
      },
      {
        title: "Số câu hỏi",
        key: "totalQuestions",
        sortable: false,
      },
      {
        title: "Ngày tạo",
        key: "created",
        sortable: false,
      },
      { title: "Mô tả", key: "description", sortable: false },
      {
        title: "Hành động",
        key: "actions",
        sortable: false,
      },
    ],
    isOpenFilter: false,
    creatorFilter: null,
    nameFilter: null,
    isOpenAddTest: false,
    isOpenSelectTest: false,
    selectedTest: null,
    listAllTest: [],
    openDatePickerStart: false,
    dateAssignStartComputed: new Date()
      .toLocaleDateString("en-GB", optionsFormatDate)
      .substr(0, 10),
    dateAssignStart: new Date(),
    openDatePickerEnd: false,
    dateAssignEndComputed: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000)
      .toLocaleDateString("en-GB", optionsFormatDate)
      .substr(0, 10),
    dateAssignEnd: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000),
    addTestValid: false,
    delettingItem: {},
    isOpenDelete: false,
  }),
  async mounted() {
    await this.fetchTests();
    await this.fetchAllTests()
  },
  computed: {
    ...mapState(testStore, ["updateTest"]),
    ...mapState(authenticationRole, ["authentication"]),
    ...mapState(toastStore, ["updateToast"]),
    filteredTests() {
      if (!this.creatorFilter && !this.nameFilter) {
        return this.tests;
      } else {
        return [];
      }
    },
    listSelectTest() {
      if (this.listAllTest.length) {
        return this.listAllTest.map(test => ({
          value: test,
          title: test.testName
        }))
      }
      return []
    }
  },
  methods: {
    async handleAssignExam() {
      if (this.addTestValid) {
        if (this.validateEndDate()) {
          const body = {
            startDate: this.selectedTest?.startDate,
            endDate: this.selectedTest?.endDate,
          };
          const res = await addTestToClass(
            JSON.parse(localStorage?.getItem("accessToken"))?.token,
            this.$route.params.id,
            this.selectedTest?.testId,
            body
          );
          if (res.success) {
            this.updateToast('success', "thêm bài kiểm tra thành công!")
            await this.fetchTests();
          } else {
            this.updateToast('error', "Thêm bài kiểm tra thất bại!")
          }
        }
        this.isOpenAddTest = false;
        this.selectedTest = null;
      }
    },
    validateEndDate() {
      const startDate = this.dateAssignStart;
      const endDate = this.dateAssignEnd;
      return (endDate > startDate);
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
    async fetchAllTests() {
      this.isLoadingTests = true;
      const res = await getTests(
        this.authentication?.accessToken?.token
      );
      if (res.success) {
        this.listAllTest = res.data;
      }
      this.isLoadingTests = false;
    },
    async fetchTests() {
      this.isLoadingTests = true;
      const res = await getTestsOfClass(
        this.authentication?.accessToken?.token,
        this.$route.params.id
      );
      if (res.success) {
        this.tests = res.data;
      }
      this.isLoadingTests = false;
    },
    submitSelectTest() {
      this.isOpenSelectTest = false
      this.isOpenAddTest = true
      this.dateAssignStart = new Date();
      this.dateAssignEnd = new Date(Date.now() + 2 * 24 * 60 * 60 * 1000);
      this.dateAssignStartComputed = new Date()
        .toLocaleDateString("en-GB", optionsFormatDate)
        .substr(0, 10);
      this.dateAssignEndComputed = new Date(
        Date.now() + 2 * 24 * 60 * 60 * 1000
      )
        .toLocaleDateString("en-GB", optionsFormatDate)
        .substr(0, 10);
      // this.selectedTest = null
    },
    cancelSelectTest() {
      this.isOpenSelectTest = false
      this.selectedTest = null
    },
    openDelete(item) {
      this.isOpenDelete = true;
      this.delettingItem = { ...item };
    },
    handleClickItem(item) {
      this.updateTest(item);
      this.$router.push(`/admin/exam/preview/${item.testId}`)
    },
    async deleteItem() {
      const res = await deleteTestInClass(
        JSON.parse(localStorage?.getItem("accessToken"))?.token,
        this.$route.params.id,
        this.delettingItem?.testId,
      );
      if (res.success) {
        this.updateToast('success', "Xóa bài kiểm tra thành công!")
        await this.fetchTests();
      } else {
        this.updateToast('error', "Xóa bài kiểm tra thất bại!")
      }
      this.isOpenDelete = false
    },
  },
  watch: {
    isOpenFilter(val) {
      if (!val) {
        this.creatorFilter = null;
        this.nameFilter = null;
      }
    },
    dateAssignStart(val) {
      const currentTime = new Date();
      val.setHours(currentTime.getHours());
      val.setMinutes(currentTime.getMinutes());
      val.setSeconds(currentTime.getSeconds());
      this.dateAssignStartComputed = val
        ? new Date(val)
          .toLocaleDateString("en-GB", optionsFormatDate)
          .substr(0, 10)
        : null;

      this.selectedTest.startDate = this.formatDate(new Date(val));
    },
    dateAssignEnd(val) {
      const currentTime = new Date();
      val.setHours(currentTime.getHours());
      val.setMinutes(currentTime.getMinutes());
      val.setSeconds(currentTime.getSeconds());

      this.dateAssignEndComputed = val
        ? new Date(val)
          .toLocaleDateString("en-GB", optionsFormatDate)
          .substr(0, 10)
        : null;
      this.selectedTest.endDate = this.formatDate(new Date(val));
    },
  },
};
</script>
