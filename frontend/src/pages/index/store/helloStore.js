import hello from '../../../api/helloApi';

export default {
  namespaced: true,
  state() {
    return {
      message: ''
    };
  },
  mutations: {
    updateMessage(state, { message }) {
      state.message = message;
    }
  },
  actions: {
    hello({ commit }) {
      return hello.hello()
        .then(({ data }) => {
          // 请求成功
          commit('updateMessage', { message: data.result });
        })
        .catch(error => {
          // 请求失败
          if (error.response) {
            // 请求完成，并且接收到服务器返回的 HTTP 状态码（非 `2xx` 范围内）及响应结果
            commit('updateMessage', { message: error.response.data['message'] });
          }
        });
    }
  },
  getters: {
    message: state => state.message
  }
};
