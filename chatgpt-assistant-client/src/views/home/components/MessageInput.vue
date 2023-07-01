<script lang="ts" setup>
import { ref } from 'vue'
import { Position } from '@element-plus/icons-vue'

// 发送消息消息事件
const emit = defineEmits<{
  send: [message: string]
}>()
// 输入框内的消息
const message = ref('')
const sendMessage = () => {
  emit('send', message.value)
  // 发送完清除
  message.value = ''
}
</script>

<template>
  <div class="message-input">
    <div class="input-wrapper">
      <!-- 按回车键发送，输入框高度三行 -->
      <el-input
        v-model="message"
        :autosize="false"
        :rows="3"
        class="input"
        resize="none"
        type="textarea"
        @keydown.enter="sendMessage"
      >
      </el-input>
      <div class="button-wrapper">
        <el-button type="primary" @click="sendMessage">
          <el-icon class="el-icon--left">
            <Position />
          </el-icon>
          发送
        </el-button>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.message-input {
  padding: 20px;
  border-top: 1px solid rgba(black, 0.07);
  border-left: 1px solid rgba(black, 0.07);
  border-right: 1px solid rgba(black, 0.07);
  border-top-right-radius: 5px;
  border-top-left-radius: 5px;
}

.button-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
