<script setup lang="ts">
import { toRefs } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

import type { UploadProps } from 'element-plus'
import { Result } from '../../../../typings'
import { saveUser } from '@/api/user'
import { useUserStore } from '@/stores/user'

const { userInfo } = toRefs(useUserStore())
const handleAvatarSuccess: UploadProps['onSuccess'] = (response: Result<{ url: string }>) => {
  userInfo.value.avatar = response.result.url
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Avatar picture must be JPG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}
const submit = () => {
  saveUser(userInfo.value).then((res) => {
    if (res.success) {
      ElMessage.success('提交成功')
    }
  })
}
</script>

<template>
  <div class="profile-view">
    <div class="title">我的信息</div>
    <el-form class="form" label-position="top">
      <el-form-item label="昵称">
        <el-input v-model="userInfo.nickname"></el-input>
      </el-form-item>
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="/api/upload/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <el-button type="success" @click="submit"> 提交修改</el-button>
  </div>
</template>

<style scoped lang="scss">
.profile-view {
  .title {
    font-size: 18px;
    margin-bottom: 20px;
  }
  .avatar-uploader .avatar {
    width: 120px;
    height: 120px;
    display: block;
  }
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
