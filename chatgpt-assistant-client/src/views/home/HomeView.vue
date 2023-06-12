<script lang="ts" setup>
import { Client } from '@stomp/stompjs'

const client = new Client({
  brokerURL: 'ws://localhost:8080/handshake',
  onConnect: () => {
    client.subscribe('/user/queue/chatMessage/receive', (message) =>
      console.log(`Received: ${message.body}`)
    )
    // client.publish({
    //   destination: '/socket/message/chat',
    //   body: JSON.stringify({
    //     sessionId: '6485d1cbd1fce714aa872686',
    //     content: '你好啊！',
    //     role: 'user'
    //   } as Message)
    // })
  }
})

client.activate()
</script>
<template>
  <div class="home-viw">
    <el-card class="chat-panel-wrapper">
      <div class="chat-panel">
        <div></div>
      </div>
    </el-card>
  </div>
</template>
