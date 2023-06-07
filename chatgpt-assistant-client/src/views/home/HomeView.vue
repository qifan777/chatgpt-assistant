<script lang="ts" setup>
import { onMounted } from 'vue'
import { findUserById } from '@/api/user'
import router from '@/router'

onMounted(() => {
  // findUserById('1')
  setTimeout(() => router.push('/login'), 1000)
})
</script>
<template>
  <div>hello</div>
</template>
