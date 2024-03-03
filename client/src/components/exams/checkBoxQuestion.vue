<template>
  <v-col>
    <v-row class="d-flex">
      <v-col cols="1" class="pa-0">
        <v-checkbox large class="checkbox-large" color="primary"> </v-checkbox>
      </v-col>
      <v-col cols="10">
        <v-row>
          <v-text-field
            :rule="required"
            hide-no-data
            single-line
            variant="solo"
          >
          </v-text-field>
        </v-row>

        <v-row>
          <v-text-field
            v-if="isDescOption"
            hide-no-data
            single-line
            hide-details
            placeholder="Giải thích đáp án"
          >
          </v-text-field>
        </v-row>
      </v-col>
      <!-- <v-spacer></v-spacer> -->
      <v-col cols="1" class="d-flex flex-column align-end">
        <v-icon
          class="pr-5 cursor-pointer"
          color="red"
          size="default"
          @click="handleDeleteOption"
        >
          mdi-trash-can
        </v-icon>
      </v-col>
    </v-row>
  </v-col>
</template>

<script>
export default {
  name: "QuestionCheckbox",
  props: {
    handleDeleteOption: Function,
    isDescOption: Boolean,
  },
  data: () => ({
    required: [
      (v) => {
        if (v) return true;
        return "Trường nhập bắt buộc!";
      },
    ],
  }),
};
</script>

<style></style>
