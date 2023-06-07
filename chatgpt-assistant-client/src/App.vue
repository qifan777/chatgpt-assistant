<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <router-view v-slot="{ Component }">
    <transition name="slide">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<style>
body {
  margin: 0;
}
.slide-leave-active,
.slide-enter-active {
  transition: all 0.5s ease-out;
}

.slide-enter-to,
.slide-leave-to {
  opacity: 0;
  transform: translateX(-100vw);
}

.slide-enter-from,
.slide-leave-from {
  opacity: 1;
  transform: translateX(0);
}
</style>
