<template>
  <v-row class="align-center" style="flex: 0">
    <v-col
      cols="12"
      :md="itemSelection?.length > 0 ? 6 : 8"
      class="header_title font-bold color-primary d-flex align-center"
    >
      <div class="header_icon d-flex align-center">
        <img
          src="@/assets/images/icon/ico_header.png"
          alt="Icon Header"
          class="contain_image"
        />
      </div>
      {{ title }}
    </v-col>
    <v-col
      v-if="itemSelection?.length > 0"
      cols="2"
      class="d-flex flex-column align-center"
    >
      <v-row style="width: 80%" class="d-flex align-center header_selection">
        <v-select
          v-model="this.typeSelection"
          :items="itemSelection"
          hide-details
          hide-no-data
          variant="solo"
        ></v-select
      ></v-row>
    </v-col>
    <v-col cols="12" md="4" style="padding-right: 5%">
      <v-row class="d-flex align-center header_search justify-end">
        <v-text-field
          v-model="localSearchValue"
          @input="updateSearchValue"
          placeholder="Tìm kiếm tên bài kiểm tra"
          theme="light"
          color="#00bd7e"
          prepend-inner-icon="mdi-magnify"
          v-if="!!isSearch"
          rounded
          hide-no-data
          hide-details
          :clearable="!unClearable"
          @click:clear="clearSearchValue"
        ></v-text-field>

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

        <v-btn
          rounded
          @click="handleClickBtn"
          v-if="!!textBtn"
          color="#FBB03B"
          theme="dark"
          class="header-btn font-bold"
          >{{ textBtn }}
        </v-btn>
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
    itemSelection: Array,
    textBtn: Boolean,
    handleClickBtn: Function,
  },
  data() {
    return {
      typeSelection: "Cá nhân",
      localSearchValue: "",
      unClearable: false, // Use a local data property to store the search value
    };
  },
  watch: {
    // Watch for changes in the searchValue prop and update localSearchValue accordingly
    searchValue(newVal) {
      this.localSearchValue = newVal;
    },
    typeSelection(newVal) {
      this.typeSelection = newVal;

      this.$emit("update:selection", newVal);
    },
  },
  methods: {
    // Emit an event when localSearchValue changes to update the searchValue prop in the parent component
    updateSearchValue() {
      this.$emit("update:searchValue", this.localSearchValue);
    },
    clearSearchValue() {
      if (!this.unClearable) {
        this.updateSearchValue("");
      }
    },
  },
};
</script>

<style>
@import "./HeaderTitle.style.scss";
</style>
