<script setup lang="ts">
import router from '@/router'
import { useUserStore } from '@/stores/user'
import { toRefs } from 'vue'
const userStore = useUserStore()
userStore.getInfo()
const { userInfo } = toRefs(userStore)
const switchRoute = (routeName: string) => {
  router.replace({ name: routeName, params: { routeName } })
}
</script>

<template>
  <div class="index-view">
    <div>
      <div class="header-wrapper">
        <div class="header">
          <div class="logo-wrapper">
            <img class="logo" src="@/assets/logo.jpg" />
            <div>ChatGPT助手</div>
          </div>
          <div class="avatar-wrapper">
            <div class="nickname">{{ userInfo.nickname || userInfo.nickname }}</div>
            <el-dropdown>
              <el-avatar
                :src="userInfo.avatar || 'https://www.jarcheng.top/images/logo.jpg'"
                :size="30"
                shape="square"
              ></el-avatar>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="switchRoute('profile')"> 我的信息 </el-dropdown-item>
                  <el-dropdown-item @click="switchRoute('gpt')">GPT设置</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
      <el-container>
        <router-view></router-view>
      </el-container>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.header-wrapper {
  height: 60px;
  width: 100%;
  background-color: rgb(36, 41, 47);
  .header {
    display: flex;
    justify-content: space-between;
    height: 100%;
    color: white;
    padding: 0 20px;
    .logo-wrapper {
      display: flex;
      align-items: center;
      .logo {
        width: 30px;
        height: auto;
        margin-right: 10px;
        border-radius: 15px;
      }
    }
    .avatar-wrapper {
      display: flex;
      align-items: center;
      .nickname {
        margin-right: 10px;
      }
    }
  }
}
</style>
