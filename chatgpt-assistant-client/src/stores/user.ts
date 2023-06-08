import { defineStore } from 'pinia'
import { User } from '../../typings'
import { getUserInfo } from '@/api/user'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<User>({ username: '', password: '' } as User)

  function getInfo() {
    getUserInfo().then((res) => {
      userInfo.value = res.result
    })
  }

  return { userInfo, getInfo }
})
