<template>
  <v-card v-if="isLoading" class="d-flex justify-center">
    <v-progress-circular
      :size="70"
      :width="7"
      color="success"
      indeterminate></v-progress-circular>
  </v-card>
  <v-list
    v-else-if="examNotis.length"
    style="padding-top: 0; padding-bottom: 0">
    <v-card-title
      v-if="examNotis.length"
      style="padding-top: 0; padding-bottom: 0; font-size: 1rem"
      >Bài kiểm tra đang diễn ra</v-card-title
    >
    <v-hover v-for="noti in examNotis" v-slot="{ isHovering, props }">
      <v-card
        @click="$router.replace(`/student/test/${noti.testId}`)"
        :class="{ 'on-hover': isHovering }"
        :elevation="isHovering ? 4 : 2"
        v-bind="props"
        style="margin: 0 8px 8px">
        <ExamNoti :noti="noti" />
      </v-card>
    </v-hover>
  </v-list>
  <v-card-subtitle v-else>Bạn không có thông báo nào.</v-card-subtitle>
</template>

<script>
import datetime from '@/utils/datetime';
import { getTestInfoByClass } from '@/services';
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';
import ExamNoti from '@/views/Student/Notification/ExamNoti.vue';

export default {
  components: {
    ExamNoti,
  },
  data: () => ({
    examNotis: [],
    isExamLoading: false,
  }),
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(studentStore, ['student']),
    isLoading() {
      return this.isExamLoading;
    },
  },
  async mounted() {
    this.isExamLoading = true;
    const res = await getTestInfoByClass(
      this.authentication.accessToken.token,
      this.student.class.classId
    );
    var exams = [];
    if (res.success) {
      exams = res.data;
    }
    this.examNotis = exams.filter(
      (exam) =>
        datetime(exam.startDate).value < new Date() &&
        new Date() < datetime(exam.endDate).value
    );
    this.isExamLoading = false;
  },
  methods: {
    onclicknoti() {},
  },
};
</script>
