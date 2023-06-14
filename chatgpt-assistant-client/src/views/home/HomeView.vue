<script lang="ts" setup>
import { Client } from '@stomp/stompjs'
import MessageRow from '@/views/home/components/MessageRow.vue'
import { onMounted, ref } from 'vue'
import { ChatMessage, ChatSession } from '../../../typings'
import MessageInput from '@/views/home/components/MessageInput.vue'
import SessionItem from '@/views/home/components/SessionItem.vue'
import { CirclePlus, EditPen } from '@element-plus/icons-vue'
import { findChatSessionById, queryChatSession, saveChatSession } from '@/api/chat-session'
import dayjs from 'dayjs'

const client = new Client({
  brokerURL: 'ws://localhost:8080/handshake',
  onConnect: () => {
    client.subscribe('/user/queue/chatMessage/receive', (message) => {
      responseMessage.value.content += message.body
      console.log(message.body)
    })
  }
})
client.activate()
const sessionList = ref([] as ChatSession[])
onMounted(async () => {
  const sessionRes = await queryChatSession({ pageSize: 1000, pageNum: 1, query: {} })
  sessionList.value.push(...sessionRes.result.list)
  activeSession.value = sessionList.value[0]
})
const isEdit = ref(false)
const activeSession = ref({ messages: [] } as ChatSession)
const handleSessionSwitch = (session: ChatSession) => {
  activeSession.value = session
}
const handleCreateSession = async () => {
  const res = await saveChatSession({ topic: '新的聊天' })
  sessionList.value.unshift((await findChatSessionById(res.result)).result)
}
const handleUpdateSession = () => {
  saveChatSession(activeSession.value)
  isEdit.value = false
}
const handleDeleteSession = (session: ChatSession) => {
  const index = sessionList.value.findIndex((value) => {
    return value.id === session.id
  })
  sessionList.value.splice(index, 1)
}
const responseMessage = ref({} as ChatMessage)
const handleSendMessage = (message: string) => {
  responseMessage.value = {
    role: 'assistant',
    content: '',
    createdAt: dayjs().format('YYYY-MM-DD HH:mm:ss')
  } as ChatMessage
  const chatMessage = {
    session: Object.assign({}, activeSession.value),
    content: message,
    role: 'user',
    createdAt: dayjs().format('YYYY-MM-DD HH:mm:ss')
  } as ChatMessage
  chatMessage.session.messages = []
  client.publish({
    destination: '/socket/chatMessage/send',
    body: JSON.stringify(chatMessage)
  })
  activeSession.value.messages.push(...[chatMessage, responseMessage.value])
}
</script>
<template>
  <div class="home-view">
    <div class="chat-panel-wrapper">
      <div class="chat-panel">
        <div class="session-panel">
          <div class="title">ChatGPT助手</div>
          <div class="description">构建你的AI助手</div>
          <div class="session-list">
            <SessionItem
              v-for="(session, index) in sessionList"
              :key="session.id"
              :active="session.createdAt === activeSession.createdAt"
              :session="sessionList[index]"
              class="session"
              @click="handleSessionSwitch(session)"
              @delete="handleDeleteSession"
            ></SessionItem>
          </div>
          <div class="button-wrapper">
            <div class="new-session">
              <el-button @click="handleCreateSession">
                <el-icon :size="15" class="el-icon--left">
                  <CirclePlus />
                </el-icon>
                新的聊天
              </el-button>
            </div>
          </div>
        </div>
        <div class="message-panel">
          <div class="header">
            <div class="front">
              <div v-if="isEdit" class="title">
                <el-input
                  v-model="activeSession.topic"
                  @keydown.enter="handleUpdateSession"
                ></el-input>
              </div>
              <div v-else class="title">{{ activeSession.topic }}</div>
              <div class="description">与ChatGPT的{{ activeSession.messages.length }}条对话</div>
            </div>
            <div class="rear">
              <el-icon :size="20" class="edit" @click="isEdit = true">
                <EditPen />
              </el-icon>
            </div>
          </div>
          <el-divider :border-style="'solid'" />
          <div class="message-list">
            <transition-group name="list">
              <message-row
                v-for="(message, index) in activeSession.messages"
                :key="message.createdAt + index"
                :avatar="activeSession.createdBy.avatar"
                :message="message"
                :position="message.role === 'user' ? 'right' : 'left'"
              ></message-row>
            </transition-group>
          </div>
          <message-input @send="handleSendMessage"></message-input>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.home-view {
  display: flex;
  width: 100vw;
  justify-content: center;

  .chat-panel-wrapper {
    border-radius: 20px;
    background-color: white;
    box-shadow: 0 0 20px 20px rgb(black, 0.05);
    margin-top: 70px;
    display: flex;

    .chat-panel {
      display: flex;

      .session-panel {
        background-color: rgb(231, 248, 255);
        width: 300px;
        border-top-left-radius: 20px;
        border-bottom-left-radius: 20px;
        padding: 20px;
        position: relative;
        border-right: 1px solid rgba(black, 0.07);

        .title {
          margin-top: 20px;
          font-size: 20px;
        }

        .description {
          color: rgba(black, 0.7);
          font-size: 10px;
          margin-top: 10px;
        }

        .session-list {
          .session {
            margin-top: 20px;
          }
        }

        .button-wrapper {
          position: absolute;
          bottom: 20px;
          left: 0;
          display: flex;
          justify-content: flex-end;
          width: 100%;

          .new-session {
            margin-right: 20px;
          }
        }
      }

      .message-panel {
        .header {
          padding: 20px 20px 0 20px;
          display: flex;
          justify-content: space-between;

          .front {
            .title {
              color: rgba(black, 0.7);
              font-size: 20px;
            }

            .description {
              margin-top: 10px;
              color: rgba(black, 0.5);
            }
          }

          .rear {
            display: flex;
            align-items: center;
          }
        }

        .message-list {
          padding: 20px;
          width: 700px;
          height: 800px;
          overflow-y: scroll;

          .list-enter-active,
          .list-leave-active {
            transition: all 0.5s ease;
          }

          .list-enter-from,
          .list-leave-to {
            opacity: 0;
            transform: translateX(30px);
          }
        }
      }
    }
  }
}
</style>
