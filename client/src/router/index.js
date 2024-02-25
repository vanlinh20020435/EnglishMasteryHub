import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import Teacher from "@/views/Teacher/index.vue";
import Manager from "@/views/Teacher/Manager.vue";
import Admin from "@/views/Admin/index.vue";
import User from "@/views/Admin/User/index.vue";
import { ManageClass, ManageExam, ManageCurriculum } from "@/views/Teacher";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      component: HomeView,
    },
    {
      path: "/register",
      component: HomeView,
    },
    {
      path: "/teacher",
      component: Teacher,
      children: [
        {
          path: "",
          component: Manager,
        },
        {
          path: "manager",
          component: Manager,
        },
        {
          path: "class",
          component: ManageClass,
        },
        {
          path: "exam",
          component: ManageExam,
        },
        {
          path: "curriculum",
          component: ManageCurriculum,
        },
      ],
    },
    {
      path: "/admin",
      component: Admin,
      children: [
        {
          path: "user",
          children: [
            {
              path: "",
              component: User,
            },
            {
              path: "student",
              component: HomeView,
            },
            {
              path: "teacher",
              component: HomeView,
            },
          ],
        },
        {
          path: "class",
          component: HomeView,
        },
        {
          path: "event",
          component: HomeView,
        },
        {
          path: "/:pathMatch(.*)*",
          redirect: "user",
        },
      ],
    },
    {
      path: "/:pathMatch(.*)*",
      redirect: "/login",
    },
  ],
});

router.beforeEach((to, from, next) => {
  var isLoggedIn = false;
  const path = to.fullPath;
  // check path
  if (path) {
    isLoggedIn = true;
  }
  isLoggedIn = true;
  if (!isLoggedIn) {
    next("/login");
  } else {
    next();
  }
});

export default router;
