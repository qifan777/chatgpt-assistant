<script lang="ts" setup>
import { CircleClose } from '@element-plus/icons-vue'
import { ChatSession } from '../../../../typings'
import { deleteChatSession } from '@/api/chat-session'

const prop = defineProps<{ active: boolean; session: ChatSession }>()
const emit = defineEmits<{
  delete: [session: ChatSession]
}>()

const handleDeleteSession = () => {
  deleteChatSession([prop.session.id]).then((res) => {
    if (res.success) {
      emit('delete', prop.session)
    }
  })
}
</script>

<template>
  <div :class="['session-item', active ? 'active' : '']">
    <div class="name">{{ session.topic }}</div>

    <div class="count-time">
      <div class="count">{{ session.messages ? session.messages.length : 0 }}条对话</div>
      <div class="time">{{ session.createdAt }}</div>
    </div>
    <div class="mask"></div>

    <div class="btn-wrapper">
      <el-icon :size="15" class="close">
        <el-popconfirm title="是否确认永久删除该聊天会话？" @confirm="handleDeleteSession">
          <template #reference>
            <CircleClose />
          </template>
        </el-popconfirm>
      </el-icon>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.session-item {
  padding: 12px;
  background-color: white;
  border-radius: 10px;
  width: 250px;
  position: relative;
  cursor: grab;
  overflow: hidden;

  .name {
    font-size: 14px;
    font-weight: 600;
    width: 200px;
    word-spacing: 3px;
    color: rgba(black, 0.8);
  }

  .count-time {
    margin-top: 10px;
    font-size: 10px;
    color: rgba(black, 0.5);
    display: flex;
    justify-content: space-between;
  }

  &.active {
    border: 2px solid #1d93ab;
  }

  &:hover {
    .mask {
      opacity: 1;
    }

    .btn-wrapper {
      &:hover {
        cursor: pointer;
      }

      opacity: 1;
      right: 20px;
    }
  }

  .mask {
    transition: all 0.2s ease-out;
    position: absolute;
    background-color: rgba(black, 0.05);
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    opacity: 0;
  }

  .btn-wrapper {
    color: rgba(black, 0.5);
    transition: all 0.2s ease-out;
    position: absolute;
    top: 10px;
    right: -20px;
    z-index: 10;
    opacity: 0;

    .edit {
      margin-right: 5px;
    }
  }
}
</style>
