import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import IndexView from '@/views/index/IndexView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: IndexView,
      redirect: 'home',
      children: [{ path: 'home', component: HomeView }]
    },

    {
      path: '/login',
      name: 'login',

      component: () => import('@/views/login/LoginView.vue')
    }
  ]
})

export default router
