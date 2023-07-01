<script lang="ts" setup>
import { PropType } from 'vue'
import { ChatMessage } from '../../../../typings'
import logo from '@/assets/logo.jpg'
import TextLoading from '@/views/home/components/TextLoading.vue'
// message：接受消息对象，展示消息内容和头像，并且根据角色调整消息位置。
// avatar：用户头像，如果角色是 Assistant则使用 logo。
const props = defineProps({
  message: { type: Object as PropType<ChatMessage>, required: true },
  avatar: { type: String, default: 'https://www.jarcheng.top/images/logo.jpg' }
})
</script>

<!-- 整个div是用来调整内部消息的位置，每条消息占的空间都是一整行，然后根据right还是left来调整内部的消息是靠右边还是靠左边 -->
<template>
  <div :class="['message-row', message.role === 'user' ? 'right' : 'left']">
    <!-- 消息展示，分为上下，上面是头像，下面是消息 -->
    <div class="row">
      <!-- 头像， -->
      <div class="avatar-wrapper">
        <el-avatar v-if="message.role === 'user'" :src="avatar" class="avatar" shape="square" />
        <el-avatar v-else :src="logo" class="avatar" shape="square" />
      </div>
      <!-- 发送的消息或者回复的消息 -->
      <div class="message">
        <!-- 预览模式，用来展示markdown格式的消息 -->
        <MdPreview
          v-if="message.content"
          :id="'preview-only'"
          :model-value="message.content"
          :preview-theme="'smart-blue'"
          :style="{
            backgroundColor: message.role === 'user' ? 'rgb(231, 248, 255)' : '#f4f4f5'
          }"
        ></MdPreview>
        <!-- 如果消息的内容为空则显示加载动画 -->
        <TextLoading v-else></TextLoading>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.message-row {
  display: flex;

  &.right {
    // 消息显示在右侧
    justify-content: flex-end;

    .row {
      // 头像也要靠右侧
      .avatar-wrapper {
        display: flex;
        justify-content: flex-end;
      }

      // 用户回复的消息和ChatGPT回复的消息背景颜色做区分
      .message {
        background-color: rgb(231, 248, 255);
      }
    }
  }

  // 默认靠左边显示
  .row {
    .avatar-wrapper {
      .avatar {
        box-shadow: 20px 20px 20px 3px rgba(0, 0, 0, 0.03);
        margin-bottom: 20px;
      }
    }

    .message {
      font-size: 15px;
      padding: 1.5px;
      // 限制消息展示的最大宽度
      max-width: 500px;
      // 圆润一点
      border-radius: 7px;
      // 给消息框加一些描边，看起来更加实一些，要不然太扁了轻飘飘的。
      border: 1px solid rgba(black, 0.1);
      // 增加一些阴影看起来更加立体
      box-shadow: 20px 20px 20px 1px rgba(0, 0, 0, 0.01);
    }
  }
}

// 调整markdown组件的一些样式，deep可以修改组件内的样式，正常情况是scoped只能修改本组件的样式。
:deep(.md-editor-preview-wrapper) {
  padding: 0 10px;

  .smart-blue-theme p {
    line-height: unset;
  }
}
</style>
