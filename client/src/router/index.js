import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import Teacher from "@/views/Teacher/index.vue";
import Manager from "@/views/Teacher/Manager.vue";
import Admin from "@/views/Admin/index.vue";
import User from "@/views/Admin/User/index.vue";
import { ManageClass, ManageExam, ManageCurriculum } from "@/views/Teacher";
import { authenticationRole } from "@/stores";
import Login from "@/views/Login.vue";
import StudentManager from "@/views/Admin/User/Student.vue";
import TeacherManager from "@/views/Admin/User/Teacher.vue";
const routes = [
  {
    path: "/login",
    component: Login,
    public: true
  },
  {
    path: "/register",
    component: HomeView,
    public: true
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
            component: StudentManager,
          },
          {
            path: "teacher",
            component: TeacherManager,
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
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/login",
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const path = to.fullPath;
  const authenticationStore = authenticationRole();
  const { authentication } = authenticationStore;
  const pathSplitted = path.split('/')
  if (routes.some(route => (route.path === path) && route.public && !to.redirectedFrom)) {
    next()
    return
  }
  if (authentication.role) {
    if (pathSplitted.length && pathSplitted[1] === authentication.role) {
      next();
    } else {
      next("/" + authentication.role);
    }
  } else {
    next("/login");
  }
});

export default router;
