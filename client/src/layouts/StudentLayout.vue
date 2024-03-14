<template>
  <v-app v-if="cls?.classId">
    <StudentNavbar style="flex: 0" :drawwing="() => (drawer = !drawer)" :user="authentication?.user" />
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
import DefaultSidebar from '@/components/sidebar/DefaultSidebar.vue';
import { authenticationRole } from '@/stores';
import { mapState } from 'pinia';
import StudentNavbar from '@/components/navbar/StudentNavbar.vue';
export default {
  components: {
    DefaultSidebar,
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
    drawer: true
  })
};
</script>
