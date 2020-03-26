import { getRouter } from '../../../plugins/router';
import store from '../store';

// 默认情况下，Vue CLI 会将 `@` 作为 `<projectRoot>/src` 的别名
const routes = [
  {
    path: '/',
    name: 'Home',
    // 路由级代码拆分
    // 这将为该路由生成一个单独的块（`home.[hash].js`）
    // 延迟加载，只有在访问该路由时才会加载
    component: () => import(/* webpackChunkName: "home" */ '../views/Home'),
    meta: {
      requiresAuth: true,
      title: '主页 - Spring Boot & Vue.js'
    }
  },
  {
    path: '/hello',
    name: 'Hello',
    component: () => import(/* webpackChunkName: "hello" */ '../views/Hello'),
    meta: {
      requiresAuth: true,
      title: 'Hello - Spring Boot & Vue.js'
    }
  }
];

const router = getRouter(routes, store);

export default router;
