<template>
  <v-app v-if="cls?.classId">
    <StudentNavbar v-if="!isExam" :drawwing="() => (drawer = !drawer)" :user="authentication?.user" />
    <v-main class="d-flex justify-center" style="width: 100vw; flex: 1;">
      <v-container>
        <slot></slot>
      </v-container>
    </v-main>
  </v-app>
  <v-app v-else>
    <v-container class="d-flex justify-center align-center" style="height: 100%;">
      <v-progress-circular :size="70" :width="7" color="green" indeterminate></v-progress-circular>
    </v-container>
  </v-app>
</template>

<script>
import { authenticationRole } from '@/stores';
import { mapState } from 'pinia';
import StudentNavbar from '@/components/navbar/StudentNavbar.vue';
export default {
  components: {
    StudentNavbar,
  },
  props: {
    cls: Object,
    typeLayout: String,
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
  },
  data: () => ({
    drawer: true,
    isExam: false
  }),
  created() {
    const path = this.$route.fullPath;
    const pathSplit2 = path.split("/")[2];
    this.isExam = pathSplit2 === 'exam'
  },
  watch: {
    $route(val) {
      const current = val.path.split('/')[2]
      this.isExam = current === 'exam'
    }
  }
};
</script>
