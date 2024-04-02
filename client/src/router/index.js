import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import Teacher from "@/views/Teacher/index.vue";
import Manager from "@/views/Teacher/Manager.vue";
import Admin from "@/views/Admin/index.vue";
import User from "@/views/Admin/User/index.vue";
import {
  ManageClass,
  ManageExam,
  ManageCurriculum,
  CreateExam,
  DetailClassTeacher,
  ClassStudents,
  StudyDocuments,
  AssignExam,
  MarkExam,
  EditExam,
} from "@/views/Teacher";
import { authenticationRole } from "@/stores";
import Login from "@/views/Login.vue";
import StudentManager from "@/views/Admin/User/Student.vue";
import TeacherManager from "@/views/Admin/User/Teacher.vue";
import ExamManager from "@/views/Admin/Exam/index.vue";
import CreateExamManager from "@/views/Admin/Exam/Create.vue";
import Class from "@/views/Admin/Class/index.vue";
import ClassSlug from "@/views/Admin/Class/ClassSlug.vue";
import { Account, EditAccount } from "@/views/Account";
import Student from "@/views/Student/index.vue";
import News from "@/views/Student/News.vue";
import Test from "@/views/Student/Test/index.vue";
import Docs from "@/views/Student/Docs.vue";
import TestSlug from "@/views/Student/Test/TestSlug.vue";
const routes = [
  {
    path: "/login",
    component: Login,
    public: true,
  },
  {
    path: "/register",
    component: HomeView,
    public: true,
  },
  {
    path: "/:role/account",
    component: Account,
    public: true,
  },
  {
    path: "/:role/account/edit",
    component: EditAccount,
    public: true,
  },
  {
    path: "/teacher",
    component: Teacher,
    children: [
      {
        path: "",
        component: ManageExam,
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
        path: "class/:id",
        children: [
          {
            path: "",
            component: DetailClassTeacher,
          },
          {
            path: "students",
            component: ClassStudents,
          },
          {
            path: "study-document",
            component: StudyDocuments,
          },
          {
            path: "assign-exam",
            component: AssignExam,
          },
          {
            path: "mark-exam",
            component: MarkExam,
          },
        ],
      },
      {
        path: "exam",
        children: [
          {
            path: "",
            component: ManageExam,
          },
          {
            path: "add",
            component: CreateExam,
          },
          {
            path: "edit/:id",
            component: EditExam,
          },
          {
            path: "my_exam",
            component: ManageExam,
            props: { isAll: false },
          },
          {
            path: "all",
            component: ManageExam,
            props: { isAll: true },
          },
        ],
      },
      {
        path: "curriculum",
        component: ManageCurriculum,
      },
    ],
  },
  {
    path: "/admin",
    redirect: "/admin/user",
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
        component: Class,
      },
      {
        path: "class/:id",
        component: ClassSlug,
      },
      {
        path: "event",
        component: Event,
      },
      {
        path: "exam",
        children: [
          {
            path: "",
            component: ExamManager,
            props: { isAdmin: true },
          },
          {
            path: "add",
            component: CreateExamManager,
          },
        ],
      },
    ],
  },
  {
    path: "/student",
    redirect: "/student/news",
    component: Student,
    children: [
      {
        path: "news",
        component: News,
      },
      {
        path: "test",
        component: Test,
      },
      {
        path: "test/:id",
        component: TestSlug,
      },
      {
        path: "document",
        component: Docs,
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/login",
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  console.log("navigating...", to);
  const path = to.fullPath;
  const authenticationStore = authenticationRole();
  const { authentication } = authenticationStore;
  const pathSplitted = path.split("/");
  if (authentication?.user?.role) {
    if (path === "/login" && to.redirectedFrom) {
      next("/" + authentication?.user?.role);
      return;
    }
    if (pathSplitted.length && pathSplitted[1] === authentication?.user?.role) {
      next();
    } else {
      if (routes.some((route) => route.path === path && route.public)) {
        if (path === "login" || path === "register") {
          localStorage.removeItem("accessToken");
          localStorage.removeItem("user");
        }
        next();
      } else {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("user");
        next("/login");
      }
    }
  } else {
    if (routes.some((route) => route.path === path && route.public)) {
      next();
    } else {
      next("/login");
    }
  }
});

export default router;
