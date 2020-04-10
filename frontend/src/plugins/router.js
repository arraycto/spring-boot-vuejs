import Vue from 'vue';
import VueRouter from 'vue-router';
import NProgress from 'nprogress';
import { isLoggedIn } from '../utils/userUtils';

Vue.use(VueRouter);

// 若 `webpackHotUpdate` 对象存在，则说明当前环境为测试环境
const isDevelopment = typeof webpackHotUpdate !== 'undefined';
// 最终打包进后端后的服务器上下文路径
const ctx = isDevelopment ? '' : '/app';

const getRouter = (routes, indexStore) => {
  const router = new VueRouter({
    routes
  });

  // 保护需要权限才可访问的路由
  const protectRoute = (to, from, next) => {
    // 鉴权
    if (to.matched.some(record => record.meta.requiresAuth)) {
      // 只有登录后，才可以访问这些受保护的路由
      // 否则，类似于 HTTP 重定向的行为来跳转到登录页
      if (!isLoggedIn()) window.location.replace(`${ctx}/login`);
      else next();
    } else next();
  };

  // 更新网页标题
  const updatePageTitle = (to) => {
    // 从最后一个开始，找到最近一个带有标题的路由
    // 例如，如果存在路由 `/some/deep/nested/route`，
    // 且 `/some`、`/deep` 和 `/nested` 都有标题（`title`），则将选择 `nested`
    const nearestWithTitle = to.matched.slice().reverse().find(r => r.meta && r.meta.title);

    // 如果找到带有标题的路由，请将文档（页面）标题设置为该值
    if(nearestWithTitle) document.title = nearestWithTitle.meta.title;
  };

  // 此回调在每次路由更改之前运行，包括页面加载
  router.beforeEach((to, from, next) => {
    NProgress.start();
    indexStore.commit('loading', { spinning: true });
    protectRoute(to, from, next);
    updatePageTitle(to);
  });

  router.afterEach(() => {
    NProgress.done();
    indexStore.commit('loading', { spinning: false });
  });

  return router;
};

// 当用户在已登录状态再访问登录页时，自动重定向到首页
if ((window.location.pathname === `${ctx}/login`) && (isLoggedIn())) window.location.replace(`${ctx}/`);

export { ctx, getRouter };
