<script lang="ts" setup>
import { PropType } from 'vue'
import { ChatMessage } from '../../../../typings'
import logo from '@/assets/logo.jpg'
import { MdPreview } from 'md-editor-v3'

defineProps({
  position: { type: String as PropType<'left' | 'right'>, default: 'left' },
  message: { type: Object as PropType<ChatMessage>, required: true },
  avatar: { type: String, default: 'https://www.jarcheng.top/images/logo.jpg' }
})
</script>

<template>
  <div :class="['message-row', position]">
    <div class="row">
      <div class="avatar-wrapper">
        <el-avatar v-if="message.role === 'user'" :src="avatar" class="avatar" shape="square" />
        <el-avatar v-else :src="logo" class="avatar" shape="square" />
      </div>
      <div class="message">
        <MdPreview
          :id="'preview-only'"
          :preview-theme="'smart-blue'"
          :model-value="message.content"
          :style="{
            backgroundColor: message.role === 'user' ? 'rgb(231, 248, 255)' : '#f4f4f5'
          }"
        ></MdPreview>
        <!--        {{ message.content || '...' }}-->
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.message-row {
  display: flex;

  &.right {
    justify-content: flex-end;

    .row {
      .avatar-wrapper {
        display: flex;
        justify-content: flex-end;
      }

      .message {
        background-color: rgb(231, 248, 255);
      }
    }
  }

  .row {
    .avatar-wrapper {
      .avatar {
        box-shadow: 20px 20px 20px 1px rgba(0, 0, 0, 0.02);
        margin-bottom: 20px;
      }
    }

    .message {
      font-size: 15px;
      display: inline-block;
      background-color: #f4f4f5;
      padding: 1.5px;
      max-width: 500px;
      border-radius: 7px;
      border: 1px solid rgba(black, 0.1);
      box-shadow: 20px 20px 20px 1px rgba(0, 0, 0, 0.01);
    }
  }
}
:deep(.md-editor-preview-wrapper) {
  padding: 0 10px;
}
</style>
