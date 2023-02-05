import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 完全引入 ant design vue
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

// 配置axios 的请求域
axios.defaults.baseURL = process.env.VUE_APP_SERVER;

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app')

// 全局使用图标
const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

console.log("当前环境：", process.env.NODE_ENV);
console.log("当前服务器：", process.env.VUE_APP_SERVER);
