import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { MdPreview, MdCatalog } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'

const app = createApp(App)

app.use(ElementPlus).use(createPinia()).use(router).component('MdPreview', MdPreview)

app.mount('#app')
