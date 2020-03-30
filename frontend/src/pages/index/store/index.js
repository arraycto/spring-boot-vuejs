import Vue from 'vue';
import Vuex from 'vuex';
import login from "../../login/store/loginStore";
import hello from './helloStore';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    // 存放状态
    spinning: true
  },
  mutations: {
    // 存放同步方法，用于更改状态
    loading(state, { spinning }) {
      state.spinning = spinning;
    }
  },
  actions: {
    // 存放异步方法，用于调用 `mutations` 方法
  },
  getters: {
    // 存入获取状态的方法
    loading: state => state.spinning
  },
  modules: {
    // 存放模块
    login,
    hello
  }
});
