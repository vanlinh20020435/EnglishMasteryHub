<template>
  <v-container> hihi </v-container>
</template>

<script>
import { mapState } from 'pinia';
import { authenticationRole, toastStore } from '@/stores';
import PublicNavbar from '@/components/navbar/PublicNavbar.vue';

export default {
  components: {
    PublicNavbar,
  },
  computed: {
    ...mapState(authenticationRole, ['authentication']),
    ...mapState(toastStore, ['updateToast']),
  },
  mounted() {},
};
</script>
