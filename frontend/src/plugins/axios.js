import axios from 'axios';
import NProgress from 'nprogress';
import { ctx } from './router';
import { autoRefreshToken } from '../utils/userUtils';

const instance = axios.create({
  baseURL: '/app', // 最终打包进后端后的服务器上下文路径
  timeout: 15000
});

const appendAccessTokenToUrl = (config) => {
  const token = localStorage.getItem('access_token');
  if (token && !(window.location.pathname === `${ctx}/login`)) {
    if (config['params']) config['params']['access_token'] = token;
    else config['params'] = { access_token: token };
    return config;
  }
};

instance.interceptors.request.use(config => {
  // 在发送 HTTP 请求前
  NProgress.start();
  autoRefreshToken(config.url);
  // 自动拼接 access token
  appendAccessTokenToUrl(config);
  return config
},error => {
  // 发送 HTTP 请求失败
  NProgress.done();
  return Promise.reject(error)
});

const handleErrors = (error) => {
  if (error.response) {
    // 请求完成，并且接收到服务器返回的 HTTP 状态码（非 2xx 范围内）及响应结果
    const data = error.response.data;
    console.log(data);

    const { error_code: errorCode } = data;

    if (errorCode === 1004 || errorCode === 1005) {
      // 当 access token 已失效或已过期时
      // 取消前端登录缓存
      localStorage.removeItem('access_token');

      // 若当前页不是登录页，则重定向回登录页
      if (!(window.location.pathname === `${ctx}/login`))
        window.location.replace(`${ctx}/login`);
    }
  } else if (error.request) {
    // 请求完成，但没有接收到来自服务器的响应
    console.log(error.request);
  } else {
    // 在建立请求时发生了错误
    console.log('Error', error.message);
  }
};

instance.interceptors.response.use(response => {
  // HTTP 状态码 2xx
  NProgress.done();
  return response;
}, error => {
  // HTTP 状态码不是 2xx
  handleErrors(error);
  NProgress.done();
  return Promise.reject(error);
});

export default instance;
