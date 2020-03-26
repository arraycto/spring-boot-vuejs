import axios from 'axios';
import NProgress from "nprogress";

const instance = axios.create({
  baseURL: '/api',
  timeout: 15000
});

instance.interceptors.request.use(config => {
  NProgress.start();
  return config
},error => {
  NProgress.start();
  return Promise.reject(error)
});

instance.interceptors.response.use(response => {
  NProgress.done();
  return response;
}, error => {
  NProgress.done();
  return Promise.reject(error);
});

export default instance;
