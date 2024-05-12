<script>
import DefaultLayout from "@/layouts/DefaultLayout.vue";

export default {
  components: {
    DefaultLayout,
  },
  data: () => ({
    menu: [
      // {
      //   title: "Quản lý",
      //   value: "foo",
      //   icon: "",
      //   children: [
      //     {
      //       title: "Fizz",
      //       value: "fizz",
      //       icon: "",
      //     },
      //     {
      //       title: "Buzz",
      //       value: "buzz",
      //       icon: "",
      //     },
      //   ],
      // },
      {
        title: "Quản lý lớp",
        value: "class",
        icon: "mdi-account-multiple",
      },
      {
        title: "Quản lý bài kiểm tra",
        value: "exam",
        icon: "mdi mdi-file-edit",
        children: [
          {
            title: "Bài kiểm tra của tôi",
            value: "my_exam",
            icon: "",
          },
          {
            title: "Tất cả",
            value: "all",
            icon: "",
          },
        ],
      },
      // {
      //   title: "Giáo trình",
      //   value: "curriculum",
      //   icon: "mdi mdi-book-open-blank-variant",
      // },
    ],
  }),
};
</script>

<template>
  <DefaultLayout :menu="menu" typeLayout="teacher">
    <!-- <div style="width: 100%; height: 100%">teacher</div> -->
    <router-view />
  </DefaultLayout>
</template>
