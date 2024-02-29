import { ref } from "vue";
import { defineStore } from "pinia";

export const authenticationRole = defineStore("authentication", () => {
  var locationUser = JSON.parse(localStorage.getItem("user"));
  var locationAccessToken = JSON.parse(localStorage.getItem("accessToken"));
  if (locationAccessToken?.expires < new Date()) {
    locationUser = {};
    locationAccessToken = {};
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
  }
  const authentication = ref({
    user: locationUser || {},
    accessToken: locationAccessToken || {},
  });
  localStorage.setItem(
    "accessToken",
    JSON.stringify(authentication.value.accessToken)
  );
  localStorage.setItem("user", JSON.stringify(authentication.value.user));
  function updateAuth(newData) {
    authentication.value = {
      ...authentication.value,
      ...newData,
    };
    localStorage.setItem(
      "accessToken",
      JSON.stringify(authentication.value.accessToken)
    );
    localStorage.setItem("user", JSON.stringify(authentication.value.user));
  }

  return { authentication, updateAuth };
});
