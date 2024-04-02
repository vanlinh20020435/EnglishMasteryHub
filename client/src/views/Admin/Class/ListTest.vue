<template>
  <v-card style="margin-top: 16px">
    <v-toolbar color="#ebebeba3" flat>
      <v-toolbar-title>Danh sách bài kiểm tra</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon="mdi-filter" style="margin-right: 8px" :color="isOpenFilter ? '#00bd7e' : ''"
        @click="() => (isOpenFilter = !isOpenFilter)"></v-btn>
      <v-btn class="mb-2" color="#00bd7e" dark variant="outlined" @click="() => addExam">
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
      :sort-by="[{ key: 'studentId', order: 'asc' }]">
    </v-data-table>
  </v-card>
</template>

<script>
import {
  getTestsOfClass
} from '@/services';
import { authenticationRole, toastStore } from '@/stores';
import { mapState } from 'pinia';
export default {
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
        title: "Thời gian (phút)", key: "time",
        sortable: false,
      },
      {
        title: "Số câu hỏi", key: "totalQuestions",
        sortable: false,
      },
      {
        title: "Ngày tạo", key: "created",
        sortable: false,
      },
      { title: "Mô tả", key: "description", sortable: false },
      {
        title: "Actions",
        key: "actions",
        sortable: false,
      }
    ],
    isOpenFilter: false,
    creatorFilter: null,
    nameFilter: null
  }),
  async mounted() {
    await this.fetchTests();
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
    filteredTests() {
      if (!this.creatorFilter && !this.nameFilter) {
        return this.tests
      } else {
        return []
      }
    }
  },
  methods: {
    async fetchTests() {
      this.isLoadingTests = true;
      const res = await getTestsOfClass(this.authentication?.accessToken?.token,
        this.$route.params.id);
      if (res.success) {
        this.tests = res.data;
      }
      this.isLoadingTests = false;
    },
  },
  watch: {
    isOpenFilter(val) {
      if (!val) {
        this.creatorFilter = null
        this.nameFilter = null
      }
    }
  }
};
</script>
