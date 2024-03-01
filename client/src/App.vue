<template>
  <v-snackbar :color="toast.type" v-model="toast.active" location="top">
    {{ toast.message }}
    <template v-slot:actions>
      <v-btn color="white" @click="toast.active = false">
        Close
      </v-btn>
    </template>
  </v-snackbar>
  <RouterView />
</template>

<script>
import { RouterView } from "vue-router";
import { toastStore } from "@/stores";
import { mapState } from "pinia";

export default {
  components: {
    RouterView,
  },
  computed: {
    ...mapState(toastStore, ["toast", "updateToast"]),
  },
  watch: {
    'this.toast.message': function (val) {
      if (!val) this.clearToast()
    }
  }
};
</script>