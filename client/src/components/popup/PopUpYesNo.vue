<template>
  <v-dialog v-model="dialogVisible" max-width="500px">
    <v-card class="card-popup">
      <v-card-title v-if="typeof msg == 'string'" class="popup-msg">{{
        msg
      }}</v-card-title>
      <h3
        v-else-if="typeof msg == 'object'"
        v-for="(msgItem, index) in msg"
        :key="index"
        class="popup-msg font-semi-bold"
      >
        {{ msgItem }}
      </h3>
      <v-card-actions class="popup-actions d-flex justify-center">
        <v-btn
          v-if="!hideBtnNo"
          color=""
          variant="text"
          class="btn-default d-flex align-center justify-center mr-7"
          @click="handleClickNo"
          >Không</v-btn
        >
        <v-btn
          v-if="!hideBtnYes"
          color="blue-darken-1"
          class="btn-default btn-gradient d-flex align-center justify-center"
          variant="text"
          @click="handleClickYes"
          >{{ btnYes ? btnYes : "Có" }}</v-btn
        >
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
    hideBtnNo: Boolean,
    btnYes: String,
    hideBtnYes: Boolean,
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
@import "./PopUpYesNo.style.scss";
</style>
