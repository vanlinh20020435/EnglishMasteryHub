import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import "@mdi/font/css/materialdesignicons.css";
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import App from "@/App.vue";
import router from "@/router";
import { $axios } from "@/services"

const vuetify = createVuetify({
  components,
  directives,
});

const app = createApp(App);
app.provide("router", router);
app.use(router).use(vuetify).use(createPinia())
app.config.globalProperties.$axios = $axios
app.mount("#app");
