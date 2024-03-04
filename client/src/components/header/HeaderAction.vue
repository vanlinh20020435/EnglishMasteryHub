<template>
  <v-toolbar color="#ebebeba3" flat>
    <v-toolbar-title
      class="cursor-pointer tb-title_hover"
      @click="handleToggleShowFull"
      >{{ title }}</v-toolbar-title
    >
    <v-spacer></v-spacer>
    <v-icon class="pr-5" color="red" size="default" @click="handleDelete">
      mdi-trash-can
    </v-icon>
  </v-toolbar>
</template>

<script>
export default {
  name: "HeaderAction",
  props: {
    handleDelete: Function,
    title: String,
    handleToggleShowFull: Function,
  },
};
</script>

<style>
.tb-title_hover:hover {
  color: #00b377;
}
.tb-title_hover {
  text-decoration: underline;
}
</style>
