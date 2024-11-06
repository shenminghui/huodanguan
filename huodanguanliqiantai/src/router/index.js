import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../layout/Layout.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue')
      },
      {
        path: 'delivery-orders',
        name: 'DeliveryOrders',
        component: () => import('@/views/delivery-order/index.vue')
      },
      {
        path: 'material-details',
        name: 'MaterialDetails',
        component: () => import('@/views/material-detail/index.vue')
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router 