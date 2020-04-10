import { ctx } from '../../../plugins/router';
import api from '../../../api/loginApi';
import jwt_decode from 'jwt-decode';

export default {
  namespaced: true,
  state() {
    return {
      user: {
        userName: null,
        password: null
      },
      loading: false,
      error: null
    };
  },
  mutations: {
    loading(state, { loading }) {
      state.loading = loading;
    },
    loginSuccess(state, { accessToken }) {
      state.error = null;
      state.loading = false;

      // 解析 JWT
      const decoded = jwt_decode(accessToken);
      const { user_name: userName, roles, exp: expiresIn } = decoded;

      localStorage.setItem('user_name', userName);
      localStorage.setItem('roles', roles);
      // 前端 token 的刷新机制：
      // 当 `expires_in` 减去当前 UTC 时间的毫秒值小于 10 分钟时刷新 token
      localStorage.setItem('access_token', accessToken);
      // 过期时间于，相对 UTC 时间的毫秒值
      localStorage.setItem('expires_in', expiresIn);

      // 跳转至首页
      window.location.replace(`${ctx}/`);
    },
    loginError(state, { error }) {
      state.error = error;
      state.loading = false;
    }
  },
  actions: {
    login({ commit }, { userName, password }) {
      commit('loading', { loading: true });
      return api.getToken(userName, password)
        .then(({ data }) => {
          // 请求成功
          const { access_token: accessToken } = data.result;
          commit('loginSuccess', { accessToken });
        })
        .catch(error => {
          // 请求失败
          if (error.response) {
            // 请求完成，并且接收到服务器返回的 HTTP 状态码（非 `2xx` 范围内）及响应结果
            commit('loginError', { error: error.response.data['message'] });
          }
        });
    }
  },
  getters: {
    user: state => state.user,
    loading: state => state.loading,
    error: state => state.error
  }
};
