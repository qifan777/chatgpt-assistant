<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { ChatMessage, ChatSession } from '../../../typings'
import { findChatSessionById, queryChatSession, saveChatSession } from '@/api/chat-session'
import SessionItem from '@/views/home/components/SessionItem.vue'
import { CirclePlus, Close, EditPen } from '@element-plus/icons-vue'
import MessageRow from '@/views/home/components/MessageRow.vue'
import MessageInput from '@/views/home/components/MessageInput.vue'
import { Client } from '@stomp/stompjs'
import dayjs from 'dayjs'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'

const isEdit = ref(false)
const activeSession = ref<Pick<ChatSession, 'id' | 'statistic' | 'messages' | 'topic'>>({
  id: '',
  topic: '',
  statistic: { tokenCount: 0, wordCount: 0, chatCount: 0 },
  messages: []
})
const sessionList = ref([] as ChatSession[])
onMounted(() => {
  // 查询自己的聊天会话
  queryChatSession({ pageSize: 1000, pageNum: 1, query: {} }).then((res) => {
    // 讲会话添加到列表中
    sessionList.value.push(...res.result.list)
    // 默认选中的聊天会话是第一个
    if (sessionList.value.length > 0) {
      activeSession.value = sessionList.value[0]
    }
  })
})
// 切换会话
const handleSessionSwitch = (session: ChatSession) => {
  activeSession.value = session
}
// 从会话列表中删除会话
const handleDeleteSession = (session: ChatSession) => {
  const index = sessionList.value.findIndex((value) => {
    return value.id === session.id
  })
  sessionList.value.splice(index, 1)
}
// 新增会话
const handleCreateSession = async () => {
  const res = await saveChatSession({ topic: '新的聊天' })
  sessionList.value.unshift((await findChatSessionById(res.result)).result)
}
const handleUpdateSession = () => {
  saveChatSession(activeSession.value)
  isEdit.value = false
}

const client = new Client({
  brokerURL: 'ws://localhost:8080/handshake',
  connectHeaders: {
    token: localStorage.getItem('token') || ''
  },
  onConnect: () => {
    // 连接成功后订阅ChatGPT回复地址
    client.subscribe('/user/queue/chatMessage/receive', (message) => {
      // 将每次回复的结果追加到回复结果中
      responseMessage.value.content += message.body
      console.log(message.body)
    })
  }
})
// 发起连接
client.activate()
// ChatGPT的回复
const responseMessage = ref({} as ChatMessage)
const handleSendMessage = (message: string) => {
  // 新建一个ChatGPT回复对象，不能重复使用同一个对象。
  responseMessage.value = {
    role: 'assistant',
    content: '',
    // 因为回复的消息没有id，所以统一将创建时间+index当作key
    createdAt: dayjs().format('YYYY-MM-DD HH:mm:ss')
  } as ChatMessage
  // 用户的提问
  const chatMessage = {
    session: Object.assign({}, activeSession.value),
    content: message,
    role: 'user',
    createdAt: dayjs().format('YYYY-MM-DD HH:mm:ss')
  } as ChatMessage
  // 防止循环依赖，会导致json序列化失败
  chatMessage.session.messages = []
  client.publish({
    destination: '/socket/chatMessage/send',
    body: JSON.stringify(chatMessage)
  })
  // 将两条消息显示在页面中
  activeSession.value.messages.push(...[chatMessage, responseMessage.value])
}
const { userInfo } = storeToRefs(useUserStore())
</script>
<template>
  <!-- 最外层页面于窗口同宽，使聊天面板居中 -->
  <div class="home-view">
    <!-- 整个聊天面板 -->
    <div class="chat-panel">
      <!-- 左侧的会话列表 -->
      <div class="session-panel">
        <div class="title">ChatGPT助手</div>
        <div class="description">构建你的AI助手</div>
        <div class="session-list">
          <!-- for循环遍历会话列表用会话组件显示，并监听点击事件和删除事件。点击时切换到被点击的会话，删除时从会话列表中提出被删除的会话。 -->
          <!--  -->
          <SessionItem
            v-for="(session, index) in sessionList"
            :key="session.id"
            :active="session.id === activeSession.id"
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
      <!-- 右侧的消息记录 -->
      <div class="message-panel">
        <!-- 会话名称 -->
        <div class="header">
          <div class="front">
            <!-- 如果处于编辑状态则显示输入框让用户去修改 -->
            <div v-if="isEdit" class="title">
              <!-- 按回车代表确认修改 -->
              <el-input
                v-model="activeSession.topic"
                @keydown.enter="handleUpdateSession"
              ></el-input>
            </div>
            <!-- 否则正常显示标题 -->
            <div v-else class="title">{{ activeSession.topic }}</div>
            <div class="description">与ChatGPT的{{ activeSession.messages.length }}条对话</div>
          </div>
          <!-- 尾部的编辑按钮 -->
          <div class="rear">
            <el-icon :size="20">
              <!-- 不处于编辑状态显示编辑按钮 -->
              <EditPen v-if="!isEdit" @click="isEdit = true" />
              <!-- 处于编辑状态显示取消编辑按钮 -->
              <Close v-else @click="isEdit = false"></Close>
            </el-icon>
          </div>
        </div>
        <el-divider :border-style="'solid'" />
        <div class="message-list">
          <!-- 过渡效果 -->
          <transition-group name="list">
            <message-row
              v-for="(message, index) in activeSession.messages"
              :key="message.createdAt + index"
              :avatar="userInfo.avatar"
              :message="message"
            ></message-row>
          </transition-group>
        </div>
        <!-- 监听发送事件 -->
        <message-input @send="handleSendMessage"></message-input>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.home-view {
  width: 100vw;
  display: flex;
  justify-content: center;

  .chat-panel {
    display: flex;
    border-radius: 20px;
    background-color: white;
    box-shadow: 0 0 20px 20px rgba(black, 0.05);
    margin-top: 70px;

    .session-panel {
      width: 300px;
      border-top-left-radius: 20px;
      border-bottom-left-radius: 20px;
      padding: 20px;
      position: relative;
      border-right: 1px solid rgba(black, 0.07);
      background-color: rgb(231, 248, 255);
      /* 标题 */
      .title {
        margin-top: 20px;
        font-size: 20px;
      }

      /* 描述*/
      .description {
        color: rgba(black, 0.7);
        font-size: 14px;
        margin-top: 10px;
      }

      .session-list {
        .session {
          /* 每个会话之间留一些间距 */
          margin-top: 20px;
        }
      }

      .button-wrapper {
        /* session-panel是相对布局，这边的button-wrapper是相对它绝对布局 */
        position: absolute;
        bottom: 20px;
        left: 0;
        display: flex;
        /* 让内部的按钮显示在右侧 */
        justify-content: flex-end;
        /* 宽度和session-panel一样宽*/
        width: 100%;

        /* 按钮于右侧边界留一些距离 */
        .new-session {
          margin-right: 20px;
        }
      }
    }

    /* 右侧消息记录面板*/
    .message-panel {
      width: 700px;

      .header {
        padding: 20px 20px 0 20px;
        display: flex;
        /* 会话名称和编辑按钮在水平方向上分布左右两边 */
        justify-content: space-between;

        /* 前部的标题和消息条数 */
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

        /* 尾部的编辑和取消编辑按钮 */
        .rear {
          display: flex;
          align-items: center;
        }
      }

      .message-list {
        height: 700px;
        padding: 15px;
        // 消息条数太多时，溢出部分滚动
        overflow-y: scroll;
        // 当切换聊天会话时，消息记录也随之切换的过渡效果
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
</style>
