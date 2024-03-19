<template>
  <v-app>
    <PublicNavbar />
    <Admin v-if="authentication.user?.role === 'admin'" />
    <Teacher v-if="authentication.user?.role === 'teacher'" />
    <Student v-if="authentication.user?.role === 'student'" />
  </v-app>
</template>

<script>
import { mapState } from 'pinia';
import { authenticationRole, toastStore } from '@/stores';
import PublicNavbar from '@/components/navbar/PublicNavbar.vue';
import Admin from '@/views/Account/Admin.vue';
import Teacher from '@/views/Account/Teacher.vue';
import Student from '@/views/Account/Student.vue';

export default {
  components: {
    PublicNavbar,
    Admin,
    Teacher,
    Student,
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
  },
  mounted() {},
};
</script>
