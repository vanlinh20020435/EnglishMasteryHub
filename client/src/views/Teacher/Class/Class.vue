<template>
  <v-card class="height-100 class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle title="Quản lý lớp" :createNew="createNewClass" />

      <v-divider class="header_divider" :thickness="2"></v-divider>

      <div style="max-height: 90%; overflow-y: auto" class="d-flex flex-column class-list">
        <div v-if="isLoading" class="d-flex justify-center align-center height-100"
          style="background-color: transparent;">
          <v-progress-circular :size="70" :width="7" color="success" indeterminate></v-progress-circular>
        </div>
        <div v-if="listClasses.length > 0 && !isLoading" style="width: 84%"
          class="d-flex justify-start align-self-center flex-wrap">
          <v-col cols="6" class="pt-4 pb-4" v-for="(classData, index) in listClasses" :key="index">
            <v-card @click="() => handleNavigateDetail(classData?.classId)" hover class="pa-3 pl-4 pr-4 class-box"
              style="border-radius: 1rem">
              <v-row>
                <div class="pa-3 class-img d-flex align-center">
                  <img :src="getClassAvatar(classData)" alt="Img Class" />
                </div>

                <v-col style="flex: 1" class="d-flex flex-column">
                  <h3 class="font-semi-bold text-center class-name pt-1 pb-1 pl-3 pr-3">
                    {{ classData?.className }}
                  </h3>

                  <div class="pt-2">
                    <strong>Mô tả:</strong> {{ classData?.description }}
                  </div>

                  <div class="pt-2">
                    <strong>Thời gian:</strong> {{ classData?.startDate }} -
                    {{ classData?.endDate }}
                  </div>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </div>
        <v-row v-else-if="!isLoading" class="ma-0" style="height: 90%">
          <v-col>
            <div class="d-flex flex-column align-center class-empty">
              <img src="@/assets/images/img_empty_class.png" alt="Img Empty Class" />
              <h3 class="d-flex d-flex flex-column align-center">
                Bạn chưa được thêm vào lớp học nào.
                <br />
                <strong>Hãy liên hệ Admin để bắt đầu! </strong>
              </h3>
            </div>
          </v-col>
        </v-row>
      </div>
    </v-container>
  </v-card>
</template>

<script>
import { authenticationRole } from "@/stores";
import { mapState } from "pinia";
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import { apiCallerGet } from "@/services/teacher";
import classDefaultImage from "@/assets/images/class.png";

export default {
  name: "ManageClass",
  components: {
    HeaderTitle,
  },
  data() {
    return {
      listClasses: [],
      isLoading: true,
    };
  },
  props: {},
  computed: {
    ...mapState(authenticationRole, ["updateAuth", "authentication"]),
  },
  mounted() {
    this.fetchDataClass();
  },
  methods: {
    async fetchDataClass() {
      let urlAPI =
        "/api/teachers/" +
        this.authentication.user?.teacherId +
        "/classes/get-all";
      const result = await apiCallerGet(urlAPI);
      if (result?.data) {
        this.listClasses = result.data;
      }
      this.isLoading = false;
    },
    getClassAvatar(classData) {
      return classData && classData.avatar
        ? classData.avatar
        : classDefaultImage; // Return the class avatar or the default image
    },
    handleNavigateDetail(classId) {
      this.$router.push(`/${this.authentication?.user?.role}/class/${classId}`);
    },
  },
};
</script>

<style>
@import "./Class.style.scss";
</style>
