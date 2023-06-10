<script setup lang="ts">
import { ref } from 'vue'
import router from '@/router'

interface Menu {
  id: string
  name: string
  path: string
}

const menuList = ref<Menu[]>([
  { id: '0', name: '我的信息', path: '/user/profile' },
  { id: '1', name: 'CPT设置', path: '/user/gpt' }
])
const activeMenu = ref('0')
const handleMenuChange = (menu: Menu) => {
  activeMenu.value = menu.id
  router.push({ path: menu.path })
}
</script>

<template>
  <div class="setting-view-wrapper">
    <div class="setting-view">
      <div class="title">个人设置</div>
      <el-card class="setting-panel-wrapper">
        <div class="setting-panel">
          <div class="side-menu">
            <div
              :class="['menu-item', menu.id === activeMenu ? 'active' : '']"
              v-for="menu in menuList"
              :key="menu.id"
              @click="handleMenuChange(menu)"
            >
              {{ menu.name }}
            </div>
          </div>
          <div class="content-panel">
            <router-view />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped lang="scss">
.setting-view-wrapper {
  display: flex;
  justify-content: center;
  width: 100vw;

  .setting-view {
    .title {
      font-size: 22px;
      margin: 40px 0 20px 0;
    }

    .setting-panel-wrapper {
      width: 40vw;
      .setting-panel {
        display: flex;
        .side-menu {
          width: 150px;
          .menu-item:nth-child(1) {
            margin-top: 0;
          }
          .menu-item {
            text-align: center;
            margin-top: 20px;
            height: 40px;
            line-height: 40px;
            border-radius: 20px;
            width: 100px;
            &.active {
              color: rgb(75, 133, 245);
              background: rgb(233, 240, 255);
            }
          }
        }
      }
    }
  }
}
</style>
