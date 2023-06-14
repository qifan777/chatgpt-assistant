<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { ChatConfig } from '../../../../typings'
import { getUserChatConfig, saveChatConfig } from '@/api/chat-config'
import { assertSuccess } from '@/utils/common'

const models = [
  { value: 0, label: 'gpt-3.5-turbo' },
  { value: 1, label: 'gpt-4' }
]
const chatConfig = ref<Partial<ChatConfig>>({
  temperature: 0,
  maxTokens: 2000,
  presencePenalty: 0,
  model: 0
})
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
    <div class="title">GPT设置</div>
    <el-form label-position="top">
      <el-form-item label="模型">
        <el-select v-model="chatConfig.model">
          <el-option
            v-for="model in models"
            :key="model.value"
            :label="model.label"
            :value="model.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="随机性">
        <el-input-number
          v-model="chatConfig.temperature"
          :max="1"
          :min="0"
          :precision="1"
          :step="0.1"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="单次回复token限制">
        <el-input-number v-model="chatConfig.maxTokens" :max="4000" :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="话题新鲜度">
        <el-input-number
          v-model="chatConfig.presencePenalty"
          :max="2"
          :min="-2"
          :precision="1"
          :step="0.1"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="API Key">
        <el-input v-model="chatConfig.apiKey" type="password"></el-input>
      </el-form-item>
    </el-form>
    <el-button type="success" @click="submit">提交保存</el-button>
  </div>
</template>

<style lang="scss" scoped>
.gpt-view {
  .title {
    font-size: 18px;
    margin-bottom: 20px;
  }
}
</style>
