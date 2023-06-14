<script lang="ts" setup>
import { Position } from '@element-plus/icons-vue'
import { ref } from 'vue'

const emit = defineEmits<{
  send: [message: string]
}>()
const message = ref('')
const sendMessage = () => {
  emit('send', message.value)
  message.value = ''
}
</script>

<template>
  <div class="message-input">
    <div class="input-wrapper">
      <el-input
        v-model="message"
        :autosize="false"
        :rows="3"
        class="input"
        resize="none"
        type="textarea"
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
