import api from "./api";

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
    loginSuccess(state, { userName, accessToken, expiresIn }) {
      state.error = null;
      state.loading = false;

      localStorage.setItem('user_name', userName);
      // 前端 token 的刷新机制：
      // 当 `expires_in` 减去当前 UTC 时间的毫秒值小于 10 分钟时刷新 token
      localStorage.setItem('access_token', accessToken);
      // 过期时间于，相对 UTC 时间的毫秒值
      localStorage.setItem('expires_in', expiresIn);
    },
    loginError(state, { error }) {
      state.error = error;
      state.loading = false;
    }
  },
  actions: {
    login({ commit }, { userName, password }) {
      commit('loading', { loading: true });
      return api.login(userName, password)
      .then(({ data }) => {
        // 登录正常
        const { error, message, result } = data;
        if (error === 0) {
          // 登录成功
          const { accessToken, expiresIn } = result;
          commit('loginSuccess', { userName, accessToken, expiresIn });
          window.location.replace('/');
        } else {
          // 登录失败
          commit('loginError', { error: message });
        }
      })
      .catch(error => {
        // 登录异常
        commit('loginError', { error: error.toString() });
      });
    }
  },
  getters: {
    user: state => state.user,
    loading: state => state.loading,
    error: state => state.error
  }
};
