import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import HelloWorld from './components/HelloWorld'
import Test from './components/Test'
import axios from 'axios'

Vue.config.productionTip = false

Vue.use(VueRouter)

const routes = [
  { path: '/test', component: Test },
  { path: '/', component: HelloWorld },

]

const router = new VueRouter({
  routes 
})



Vue.prototype.$axios=axios

new Vue({
  render: h => h(App),
  router: router
}).$mount('#app')
