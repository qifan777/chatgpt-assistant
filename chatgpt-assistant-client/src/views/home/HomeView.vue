<script lang="ts" setup>
import { Client } from '@stomp/stompjs'
import { ref } from 'vue'

const result = ref('')
const prompt = ref('')
const client = new Client({
  brokerURL: 'ws://localhost:8080/handshake',
  onConnect: () => {
    client.subscribe('/user/queue/chatMessage/receive', (message) => (result.value += message.body))
  }
})
client.activate()
const handleSend = () => {
  client.publish({
    destination: '/socket/chatMessage/send',
    body: JSON.stringify({
      session: { id: '6495a20647fbac571764c984' },
      content: prompt.value,
      role: 'user'
    })
  })
  result.value = ''
  prompt.value = ''
}
</script>
<template>
  <div>
    <el-form>
      <el-form-item label="结果">
        <el-input v-model="result" type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="提问">
        <el-input v-model="prompt"></el-input>
      </el-form-item>
      <el-button type="primary" @click="handleSend">发送</el-button>
    </el-form>
  </div>
</template>
