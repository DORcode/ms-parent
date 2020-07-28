import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import HelloWorld from './components/HelloWorld'
import Test from './components/Test'
import axios from 'axios'

Vue.config.productionTip = false

Vue.use(VueRouter)

const routes = [
  { path: '/test', component: Test},
  { path: '/', component: HelloWorld},

]

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
const router = new VueRouter({
  routes // (缩写) 相当于 routes: routes
})



Vue.prototype.$axios=axios

new Vue({
  render: h => h(App),
  router: router
}).$mount('#app')
