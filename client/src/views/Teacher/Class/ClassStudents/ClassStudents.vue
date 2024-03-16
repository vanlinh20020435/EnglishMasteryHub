<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        title="Thành viên"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-col
        style="max-height: 90%; overflow-y: auto"
        class="d-flex flex-column class-list class-students align-center"
      >
        <div
          v-if="dataStudents.length > 0"
          style="width: 84%"
          class="d-flex"
        ></div>
        <v-row v-else style="height: 90%">
          <v-col>
            <div class="d-flex flex-column align-center class-empty w-100">
              <img
                src="@/assets/images/img_empty_students.jpg "
                alt="Img Empty Class"
              />
              <h3 class="d-flex d-flex flex-column align-center">
                Lớp chưa có thành viên nào.
                <br />
                <strong>Hãy liên hệ Admin để bắt đầu! </strong>
              </h3>
            </div>
          </v-col>
        </v-row>
      </v-col>
    </v-container></v-card
  >
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet } from "@/services/teacher";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";

export default {
  name: "ClassStudents",
  components: { HeaderTitle },
  data() {
    return {
      dataStudents: [],
    };
  },
  props: {},
  computed: {},
  mounted() {},
  methods: {
    async fetchDataStudents() {
      let urlAPI = "/api/class/" + this.$route.params.id + "/students/get-all";

      const result = await apiCallerGet(urlAPI);

      if (result?.data) {
        this.dataStudents = result?.data;
      }
    },
    handleGoBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style>
@import "./ClassStudents.style.scss";
@import "../Class.style.scss";
</style>
