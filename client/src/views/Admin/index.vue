<script>
import DefaultLayout from "@/layouts/DefaultLayout.vue";

export default {
  components: {
    DefaultLayout,
  },
  data: () => ({
    menu: [
      {
        title: "Quản lí người dùng",
        value: "user",
        icon: "mdi-account",
        children: [
          {
            title: "Quản lí Admin",
            value: "",
            icon: "mdi-account-key",
          },
          {
            title: "Quản lí giáo viên",
            value: "teacher",
            icon: "mdi-account-tie",
          },
          {
            title: "Quản lí học sinh",
            value: "student",
            icon: "mdi-account-group",
          },
        ],
      },
      {
        title: "Quản lí lớp học",
        value: "class",
        icon: "mdi-google-classroom",
      },
      {
        title: "Quản lý bài kiểm tra",
        value: "exam",
        icon: "mdi mdi-file-edit",

      }
    ],
  }),
};
</script>

<template>
  <DefaultLayout :menu="menu">
    <router-view />
  </DefaultLayout>
</template>
