<template>
  <div v-if="test.testId">Do Exam</div>
  <div v-else>Do Exam</div>
</template>

<script>
import { authenticationRole, studentStore, testStore, toastStore } from '@/stores';
import { mapState } from 'pinia';
export default {
  computed: {
    ...mapState(testStore, ['test', 'clearTest']),
  },
  mounted() {
    if (this.test.testId) {
      window.addEventListener('beforeunload', this.confirmLeave);
    }
  },
  methods: {
    confirmLeave(event) {
      event.preventDefault();
      event.returnValue = 'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
      return 'Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?';
    }
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.confirmLeave);
  },
  beforeRouteLeave(to, from, next) {
    if (this.test.testId) {
      if (confirm("Kết quả chưa được lưu, bạn có chắc muốn rời khỏi trang?") == true) {
        next();
      } else {
        next(false);
      }
    }
  },
};
</script>

<!-- <style>
@import "./DoExam.style.scss";
</style> -->
