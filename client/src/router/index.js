import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'
import DefaultLayout from '@/layouts/DefaultLayout.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      component: HomeView
    },
    {
      path: '/register',
      component: HomeView
    },
    {
      path: '/teacher',
      component: HomeView,
      children: [
        {
          path: 'manager',
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/login'
    }
  ]
})

router.beforeEach((to, from, next) => {
  const isLoggedIn = true;
  console.log(111111, to.fullPath);
  if (!isLoggedIn) {
    next('/login');
  } else {
    next();
  }
});

export default router
