import Vue from 'vue'
import VueRouter from 'vue-router'
import Ago from '@/components/Ago'
import Test from '@/components/Test'

Vue.use(VueRouter)

const routes = [
  { path: '/test', name: 'test', component: Test },
  { path: '/ago', name: 'ago', component: Ago }

]


const router = new VueRouter({
    routes,
    mode: 'history'
})

  export default router