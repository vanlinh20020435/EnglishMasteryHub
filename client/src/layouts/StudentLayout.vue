<template>
  <v-app>
    <StudentNavbar :drawwing="() => (drawer = !drawer)" :user="authentication.user" />
    <v-main class="d-flex justify-center" style="width: 100vw; height: 100vh">
      <v-container>
        <slot></slot>
      </v-container>
    </v-main>
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
    menu: Array,
    typeLayout: String,
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
  },
  data: () => ({
    drawer: true,
  }),
};
</script>
