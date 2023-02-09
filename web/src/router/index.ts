import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/homeView.vue'
import AdminEbook from '../views/admin/adminEbook.vue'
import AdminUser from '../views/admin/adminUser.vue'
import AdminCategory from '../views/admin/adminCategory.vue'
import AdminDoc from '../views/admin/adminDoc.vue'
import DocView from '../views/docView.vue'
import store from "@/store";
import {Tool} from "@/util/tool";


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
    component: AdminUser,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/ebook',
    name: 'ebook',
    component: AdminEbook,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'category',
    component: AdminCategory,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'doc',
    component: AdminDoc,
    meta: {
      loginRequire: true
    }
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

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
