<template>
  <v-card class="height-100 class-container">
    <v-container
      class="d-flex flex-column height-100 v-container__full"
      style="height: 92vh"
    >
      <HeaderTitle
        title="Thống kê chi tiết bài kiểm tra"
        textBtn="Quay lại"
        :handleClickBtn="() => handleGoBack()"
      />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-col
        class="d-flex flex-column class-list class-docs class-students align-center"
      >
        <div
          v-if="dataDashboard.length > 0"
          style="width: 80%; min-width: 28rem; max-width: 70rem; flex: 1"
          class="d-flex flex-column"
        >
          <v-col class="pa-8 pt-3">
            <v-card
              v-for="(exam, index) in dataDashboard"
              :key="index"
              class="cursor-pointer pa-3 pb-7 mb-5 d-flex student-item docs-item box-shadow"
              @click="
                () => {
                  console.log('132');
                }
              "
            >
              <v-row
                class="pl-4 pr-4 pt-3 d-flex flex-row w-100 justify-space-between"
              >
              </v-row>
            </v-card>
          </v-col>
        </div>
      </v-col>
    </v-container>
  </v-card>
</template>


<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet, apiCallerPost } from "@/services/teacher";

import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import PopUpYesNo from "@/components/popup/PopUpYesNo.vue";

export default {
  name: "MarkExamDashboard",
  components: {
    HeaderTitle,
    PopUpYesNo,
  },
  data() {
    return {
      dataDashboard: [],
      isLoading: false,
      classID: this.$route.params?.id,
      examID: this.$route.params?.examID,
    };
  },
  props: {},
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
  },
  mounted() {
    this.fetchDataDashboardExam();
  },
  methods: {
    async fetchDataDashboardExam() {
      this.isLoading = true;
      let urlAPI =
        "/api/test-result?classId=" + this.classID + "&testId=" + this.examID;
      const result = await apiCallerGet(urlAPI);

      if (result?.success) {
        this.dataDashboard = result.data;
      }

      this.isLoading = false;
    },
    handleGoBack() {
      this.$router.go(-1);
    },
  },
};
</script>