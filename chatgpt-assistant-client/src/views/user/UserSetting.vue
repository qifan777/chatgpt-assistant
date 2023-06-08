<script setup lang="ts">
import { ref } from 'vue'
import router from '@/router'
interface Menu {
  menuName: string
  routePath: string
}
const menuList = ref<Menu[]>([
  { menuName: '我的信息', routePath: '/user/profile' },
  { menuName: 'GPT设置', routePath: '/user/gpt' }
])
const activeMenu = ref(0)
const changeActive = (menu: Menu, index: number) => {
  router.push(menu.routePath)
  activeMenu.value = index
}
</script>

<template>
  <div class="setting-view">
    <div class="setting-content">
      <div class="title">个人设置</div>
      <el-card class="setting-panel-wrapper">
        <div class="setting-panel">
          <div class="side-menu">
            <div
              :class="['menu-item', index === activeMenu ? 'active' : '']"
              v-for="(menu, index) in menuList"
              :key="menu.menuName"
              @click="changeActive(menu, index)"
            >
              {{ menu.menuName }}
            </div>
          </div>
          <div class="right-panel">
            <router-view />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>
<style scoped lang="scss">
.setting-view {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  .setting-content {
    padding: 20px;
    width: 50vw;
    .title {
      font-size: 28px;
      margin-bottom: 20px;
    }
    .setting-panel {
      display: flex;
      .side-menu {
        margin-right: 20px;
        width: 150px;
        border-right: 1px solid rgba(black, 0.08);
        .menu-item {
          transition: all 0.3s ease-out;
          font-size: 14px;
          text-align: center;
          width: 100px;
          margin-top: 20px;
          height: 40px;
          line-height: 40px;
          border-radius: 20px;
          &.active {
            color: rgb(75, 133, 245);
            background: rgb(233, 240, 255);
          }
        }
      }
    }
  }
}
</style>
