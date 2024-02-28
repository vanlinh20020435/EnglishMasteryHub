<template>
  <v-row class="align-center">
    <v-col
      cols="12"
      md="8"
      class="header_title font-bold color-primary d-flex align-center"
    >
      <div class="header_icon d-flex align-center">
        <img
          src="@/assets/images/ico_header.png"
          alt="Icon Header"
          class="contain_image"
        />
      </div>
      {{ title }}
    </v-col>
    <v-col cols="12" md="4" style="padding-right: 5%">
      <v-row class="d-flex align-center header_search justify-end">
        <v-autocomplete
          v-if="!!isSearch"
          :items="items"
          auto-select-first
          class="flex-full-width"
          density="comfortable"
          item-props
          menu-icon=""
          placeholder="Tìm kiếm tên bài kiểm tra"
          prepend-inner-icon="mdi-magnify"
          rounded
          theme="light"
          variant="solo"
          hide-details
          color="#00bd7e"
        ></v-autocomplete>

        <v-icon
          @click="createNew"
          v-if="!!isCreate"
          color="#00bd7e"
          size="x-large"
          class="cursor-pointer"
          style="padding-left: 2rem"
        >
          mdi-plus-circle
        </v-icon>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "HeaderTitle",
  props: {
    title: String,
    isSearch: Boolean,
    isCreate: Boolean,
    createNew: Function,
  },
};
</script>

<style>
@import "./HeaderTitle.style.scss";
</style>
