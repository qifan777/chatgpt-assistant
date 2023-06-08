import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import IndexView from '@/views/index/IndexView.vue'
import UserSetting from '@/views/user/UserSetting.vue'
import UserProfile from '@/views/user/profile/UserProfile.vue'
import GPTSetting from '@/views/user/gpt/GPTSetting.vue'

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
          component: UserSetting,
          children: [
            {
              path: 'profile',
              component: UserProfile
            },
            {
              path: 'gpt',
              component: GPTSetting
            }
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
