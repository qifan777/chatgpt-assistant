import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import IndexView from '@/views/index/IndexView.vue'
import UserSettingView from '@/views/user/UserSettingView.vue'
import ProfileView from '@/views/user/profile/ProfileView.vue'
import GPTSettingView from '@/views/user/gpt/GPTSettingView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: IndexView,
      redirect: 'home',
      children: [
        { path: 'home', component: HomeView },
        {
          path: 'user',
          component: UserSettingView,
          redirect: '/user/profile',
          children: [
            { path: 'profile', component: ProfileView },
            { path: 'gpt', component: GPTSettingView }
          ]
        }
      ]
    },

    {
      path: '/login',
      name: 'login',

      component: () => import('@/views/login/LoginView.vue')
    }
  ]
})

export default router
