<template>
  <v-layout>
    <v-app-bar :order="0" color="grey-lighten-2" flat title="Application bar">
      <template v-slot:prepend>
        <v-btn variant="text" icon="mdi-menu" @click="drawer = !drawer"></v-btn>
      </template>
      <template v-slot:append>
        <v-btn variant="tonal" style="margin-right: 5px">click</v-btn>
        <v-btn variant="tonal">click</v-btn>
      </template>
    </v-app-bar>
    <DefaultSidebar :drawer="drawer" :menu="menu" />
    <v-main class="d-flex justify-center" style="width: 100vw; height: 100vh;">
      <slot />
    </v-main>
  </v-layout>
</template>

<script>
import DefaultSidebar from '@/components/sidebar/DefaultSidebar.vue';
export default {
  components: {
    DefaultSidebar,
  },
  props: {
    menu: Array
  },
  data: () => ({
    drawer: true,
  }),
};
</script>
