<template>
  <!-- backlog: permanent = true if width > 1024px -->
  <v-navigation-drawer :model-value="drawer" location="left" permanent>
    <v-list v-model:opened="open">
      <v-list-item prepend-icon="mdi-home" title="Home"></v-list-item>
      <v-list-group value="Users">
        <template v-slot:activator="{ props }">
          <v-list-item
            prepend-icon="mdi-home"
            v-bind="props"
            title="Users"></v-list-item>
        </template>
        <v-list-item
          v-for="([title, icon], i) in menus"
          :key="i"
          :value="title"
          :title="title"
          :prepend-icon="icon"></v-list-item>
      </v-list-group>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
export default {
  props: {
    drawer: Boolean,
  },
  data: () => ({
    open: ['Users'],
    menus: [
      ['Create', 'mdi-plus-outline'],
      ['Read', 'mdi-file-outline'],
      ['Update', 'mdi-update'],
      ['Delete', 'mdi-delete'],
    ],
  }),
  watch: {
    drawer(val) {
      console.log(val);
    },
  },
};
</script>
