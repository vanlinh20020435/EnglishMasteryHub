<template>
  <v-card style="margin-top: 16px">
    <v-toolbar color="#ebebeba3" flat>
      <v-toolbar-title>Danh sách bài kiểm tra</v-toolbar-title>
    </v-toolbar>
    <v-data-table :loading="isLoadingTests" :headers="testsHeaders" :items="tests"
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
        title: 'Test ID',
        align: 'center',
        key: 'testId',
        sortable: false,
      },
      { title: 'Name', key: 'testName', sortable: false },
      { title: 'Total questions', key: 'totalQuestions', sortable: false },
      { title: 'Description', key: 'description', sortable: false },
    ],
  }),
  async mounted() {
    await this.fetchTests();
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
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
};
</script>
