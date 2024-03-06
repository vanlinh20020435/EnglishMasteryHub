<template>
  <v-col>
    <v-row class="d-flex">
      <v-col cols="1" class="pa-0">
        <v-checkbox
          :checked="isChecked"
          :model-value="isChecked"
          @change="handleCheckboxChange"
          large
          class="checkbox-large"
          color="primary"
        >
        </v-checkbox>
      </v-col>
      <v-col cols="10">
        <v-row>
          <v-text-field
            :rule="required"
            hide-no-data
            single-line
            variant="solo"
            :model-value="option"
            @input="updateOption($event.target.value)"
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
            :model-value="explanation"
            @input="updateExplanation($event.target.value)"
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
    option: String,
    explanation: String,
    questionIndex: Number,
  },
  data: () => ({
    required: [
      (v) => {
        if (v) return true;
        return "Trường nhập bắt buộc!";
      },
    ],

    isChecked: false,
  }),
  methods: {
    updateOption(value) {
      this.$emit("update:option", value);
    },
    updateExplanation(value) {
      this.$emit("update:updateExplanation", value);
    },
    handleCheckboxChange() {
      if (this.isChecked) {
        // Push option and explanation to the parent component
        this.$emit("addAnswer", {
          option: this.option,
          explanation: this.explanation,
          questionIndex: this.questionIndex,
        });
      }
    },
  },
};
</script>

<style></style>
