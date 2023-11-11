<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ChatConfig } from '../../../../typings'
import { getUserChatConfig, saveChatConfig } from '@/api/chat-config'
import { assertSuccess } from '@/utils/common'

const models = [
  { label: 'gpt-3.5-turbo', value: 0 },
  { label: 'gpt-4', value: 1 },
  { label: 'gpt-4-1106-preview', value: 2 }
]
const chatConfig = ref<ChatConfig>({
  temperature: 0,
  maxTokens: 2000,
  presencePenalty: 0,
  model: 0
} as ChatConfig)
onMounted(() => {
  getUserChatConfig().then((res) => {
    if (res.result) {
      chatConfig.value = res.result
    }
  })
})
const submit = () => {
  saveChatConfig(chatConfig.value).then((res) => {
    assertSuccess(res).then((res) => {
      chatConfig.value.id = res.result
    })
  })
}
</script>

<template>
  <div class="gpt-view">
    <div class="title">GPT页面</div>
    <el-form label-position="top">
      <el-form-item label="GPT模型">
        <el-select v-model="chatConfig.model">
          <el-option
            v-for="model in models"
            :key="model.value"
            :value="model.value"
            :label="model.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="话题随机性">
        <el-input-number
          :max="1"
          :min="0"
          :step="0.1"
          v-model="chatConfig.temperature"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="单词回复Token">
        <el-input-number
          :max="4000"
          :min="0"
          :step="1"
          v-model="chatConfig.maxTokens"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="话题新鲜度">
        <el-input-number
          :max="2"
          :min="-2"
          :step="0.1"
          v-model="chatConfig.presencePenalty"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="API Key">
        <el-input v-model="chatConfig.apiKey" type="password"></el-input>
      </el-form-item>
    </el-form>
    <el-button type="success" @click="submit">提交保存</el-button>
  </div>
</template>

<style scoped lang="scss">
.gpt-view {
  .title {
    font-size: 18px;
    margin-bottom: 20px;
  }
}
</style>
