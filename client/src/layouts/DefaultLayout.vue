<template>
  <v-layout>
    <DefaultNavbar :drawwing="() => (drawer = !drawer)" :user="authentication.user" />
    <DefaultSidebar :drawer="drawer" :menu="menu" />
    <v-main class="d-flex justify-center" style="width: 100vw; min-height: 100vh">
      <v-container :class="typeLayout == 'teacher'
      ? 'v-container__full pd-0'
      : 'v-container__full'
      " style="padding: 16px; background-color: #fff">
        <slot></slot>
      </v-container>
    </v-main>
  </v-layout>
</template>

<script>
import DefaultSidebar from '@/components/sidebar/DefaultSidebar.vue';
import { authenticationRole } from '@/stores';
import { mapState } from 'pinia';
import DefaultNavbar from '@/components/navbar/DefaultNavbar.vue';
export default {
  components: {
    DefaultSidebar,
    DefaultNavbar,
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
