<template>
  <v-dialog v-model="dialogVisible" max-width="500px">
    <v-card class="card-popup">
      <v-card-title class="popup-msg">{{ msg }}</v-card-title>
      <v-card-actions class="popup-actions">
        <v-spacer></v-spacer>
        <v-btn
          color=""
          variant="text"
          class="btn-default d-flex align-center justify-center"
          @click="handleClickNo"
          >Không</v-btn
        >
        <v-spacer></v-spacer>
        <v-btn
          color="blue-darken-1"
          class="btn-default btn-gradient d-flex align-center justify-center"
          variant="text"
          @click="handleClickYes"
          >Có</v-btn
        >
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "PopUpYesNo",
  props: {
    msg: String,
    handleClickYes: Function,
    handleClickNo: Function,
    visible: Boolean,
  },
  data() {
    return {
      dialogVisible: this.visible, // Internal state to control dialog visibility
    };
  },
  watch: {
    visible(newValue) {
      // Update internal state when parent prop changes
      this.dialogVisible = newValue;
    },
    dialogVisible(newValue) {
      // Emit input event when internal state changes
      this.$emit("update:visible", newValue);
    },
  },
};
</script>

<style>
@import "./PpopUpYesNo.style.scss";
</style>
