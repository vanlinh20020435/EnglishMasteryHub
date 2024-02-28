import { ref } from "vue";
import { defineStore } from "pinia";

export const authenticationRole = defineStore("authentication", () => {
  const authentication = ref({
    user: JSON.parse(localStorage.getItem("user")) || {},
    accessToken: JSON.parse(localStorage.getItem("accessToken")) || {}
  });
  localStorage.setItem('accessToken', JSON.stringify(authentication.value.accessToken))
  localStorage.setItem('user', JSON.stringify(authentication.value.user))
  function updateAuth(newData) {
    authentication.value = {
      ...authentication.value,
      ...newData,
    };
    localStorage.setItem('accessToken', JSON.stringify(authentication.value.accessToken))
    localStorage.setItem('user', JSON.stringify(authentication.value.user))
  }

  return { authentication, updateAuth };
});
