import Vue from 'vue';
import router from './router';
import store from './store';
import App from './App';
import 'ant-design-vue/dist/antd.less'; // 定制主题需要加载 less，而非 css
import '../../plugins/nprogress';
import '../../assets/css/style.css';

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
