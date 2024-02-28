import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const authenticationRole = defineStore("authentication", () => {
  const authentication = ref({
    role: "teacher",
  });
  function updateAuth(newData) {
    authentication.value = {
      ...authentication.value,
      ...newData,
    };
  }

  return { authentication, updateAuth };
});
