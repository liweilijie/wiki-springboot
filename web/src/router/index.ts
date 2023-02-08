import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/homeView.vue'
import AdminEbook from '../views/admin/adminEbook.vue'
import AdminUser from '../views/admin/adminUser.vue'
import AdminCategory from '../views/admin/adminCategory.vue'
import AdminDoc from '../views/admin/adminDoc.vue'
import DocView from '../views/docView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/aboutView.vue')
  },
  {
    path: '/admin/user',
    name: 'user',
    component: AdminUser
  },
  {
    path: '/admin/ebook',
    name: 'ebook',
    component: AdminEbook
  },
  {
    path: '/admin/category',
    name: 'category',
    component: AdminCategory
  },
  {
    path: '/admin/doc',
    name: 'doc',
    component: AdminDoc
  },
  {
    path: '/doc',
    name: 'docView',
    component: DocView
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
