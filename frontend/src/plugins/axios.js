import axios from 'axios';
import NProgress from "nprogress";

const instance = axios.create({
  baseURL: '/api',
  timeout: 15000
});

instance.interceptors.request.use(config => {
  // 在发送 HTTP 请求前
  NProgress.start();
  return config
},error => {
  // 发送 HTTP 请求失败
  NProgress.done();
  return Promise.reject(error)
});

instance.interceptors.response.use(response => {
  // HTTP 状态码 2xx
  NProgress.done();
  return response;
}, error => {
  // HTTP 状态码不是 2xx
  NProgress.done();
  return Promise.reject(error);
});

export default instance;
