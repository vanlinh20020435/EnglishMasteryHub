<template>
  <v-card style="min-height: 100%;" class="class-container">
    <v-container class="d-flex flex-column height-100 v-container__full">
      <HeaderTitle title="Preview" textBtn="Quay lại" :handleClickBtn="() => {
          this.$router.go(-1);
        }
        " />
      <v-divider class="header_divider" :thickness="2"></v-divider>
      <v-col class="pt-0 class-list class-docs class-students align-center do-exam-container do-exam-container-mark">
        <DoExam :preview="true"></DoExam>
      </v-col>
    </v-container>
  </v-card>
</template>

<script>
import HeaderTitle from "@/components/header/HeaderTitle.vue";
import DoExam from '@/views/Student/Test/TestSlug/DoExam/DoExam.vue';
export default {
    components: {
        DoExam,
        HeaderTitle,
    },
}</script>