<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle
        title="Tài liệu hướng dẫn"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>

      <v-col class="d-flex flex-column class-list class-docs align-center">
        <div v-if="dataStudyDocs.length > 0" class="d-flex flex-column"></div>

        <v-row v-else-if="!isLoading" style="height: 90%" class="w-100">
          <v-col>
            <div
              class="d-flex flex-column align-center class-empty class-docs_empty"
            >
              <img
                src="@/assets/images/empty_study_docs.png"
                alt="Img Empty Class"
              />
              <h3 class="d-flex d-flex flex-column align-center w-100">
                Lớp chưa tài liệu học tập nào.
                <br />
                <span>
                  Chọn <strong>"Thêm mới"</strong> để thêm tài liệu.
                </span>
              </h3>
            </div>
          </v-col></v-row
        >
      </v-col>

      <v-col class="pa-5 pt-8 pb-8" style="flex: 0">
        <v-row class="d-flex align-center justify-end">
          <v-btn
            @click="() => handleAddQuestion()"
            color="#00bd7e"
            theme="dark"
            class="btn-add-question"
            >Thêm mới</v-btn
          >
        </v-row>
      </v-col>
    </v-container>
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet, apiCallerPut } from "@/services/teacher";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";
export default {
  name: "StudyDocuments",
  components: { HeaderTitle, PopUpYesNo },
  data() {
    return {
      dataStudyDocs: [],
      isLoading: true,
    };
  },
  props: {},
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
  },
  mounted() {
    this.fetchDataDocuments();
  },
  methods: {
    async fetchDataDocuments() {
      let urlAPI = "/api/class/" + this.$route.params.id + "/files/get-all";

      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataStudyDocs = result.data;
      }

      this.isLoading = false;
    },
    handleGoBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style>
@import "./StudyDocuments.style.scss";
@import "../Class.style.scss";
</style>
