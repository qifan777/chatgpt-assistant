<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { User } from '../../../typings'
import { loginUser } from '@/api/user'
import router from '@/router'

const loginForm = reactive<Partial<User>>({ username: '', password: '' })
// 登录面板过渡效果
const showPanel = ref(false)
onMounted(() =>
  setTimeout(() => {
    showPanel.value = true
  }, 500)
)
const login = () => {
  loginUser(loginForm).then((res) => {
    if (res.success) {
      router.replace({ path: '/' })
      localStorage.setItem('token', res.result.tokenValue)
      console.log(res)
    }
  })
}
</script>
<template>
  <div>
    <img alt="背景图片" class="background" src="@/assets/background.jpg" />
    <div class="panel-wrapper">
      <Transition>
        <el-card v-if="showPanel" class="panel">
          <div class="content">
            <div class="panel-left">
              <img alt="logo" class="logo" src="@/assets/logo.svg" />
              <div class="title">搭建属于你自己的ChatGPT助手</div>
              <div class="description">让ChatGPT提高你的学习效率</div>
            </div>
            <div class="panel-right">
              <div class="title">快速开始</div>
              <div class="description">创建你的账号</div>
              <el-form
                :model="loginForm"
                class="form"
                label-position="top"
                label-width="100px"
                style="max-width: 460px"
              >
                <el-form-item label="用户名">
                  <el-input v-model="loginForm.username" size="large" style="width: 400px" />
                </el-form-item>
                <el-form-item label="密码">
                  <el-input
                    v-model="loginForm.password"
                    size="large"
                    style="width: 400px"
                    type="password"
                  />
                </el-form-item>
              </el-form>
              <div class="button-wrapper">
                <el-button size="large" style="width: 300px" type="primary" @click="login"
                  >登录/注册
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </Transition>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}

.background {
  position: fixed;
  width: 100vw;
  height: auto;
  z-index: -10;
}

.panel-wrapper {
  display: flex;
  height: 100vh;
  align-items: center;
  justify-content: center;

  .panel {
    width: 40vw;

    .content {
      display: flex;

      .title {
        font-size: 24px;
        margin-top: 60px;
        font-weight: bold;
      }

      .description {
        margin-top: 20px;
        font-size: 18px;
        color: rgba(black, 0.5);
      }

      .panel-left {
        box-sizing: border-box;
        padding: 30px;
        background-color: rgb(243, 245, 249);
        height: 700px;
        width: 50%;
        border-radius: 5px;

        .logo {
          width: 30px;
          height: auto;
          margin-top: 20px;
        }
      }

      .panel-right {
        padding: 30px;
        width: 50%;

        .form {
          margin-top: 30px;
        }

        .button-wrapper {
          margin-top: 40px;
          display: flex;
          justify-content: center;
        }
      }
    }
  }
}
</style>
