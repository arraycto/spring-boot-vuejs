<template>
  <a-spin tip="加载中..." :spinning="spinning" size="large" class="h100">
    <div class="spin-content h100">
      <a-layout class="h100">
        <a-layout-content class="container">
          <NavBar />
          <router-view />
        </a-layout-content>
        <a-layout-footer class="footer">
          <div class="copyright">
            Copyright &copy; 2020 www.wuxianjie.net
          </div>
        </a-layout-footer>
      </a-layout>
    </div>
  </a-spin>
</template>

<script>
  import Vue from 'vue';
  import {Layout, Spin} from 'ant-design-vue';
  import NavBar from './views/NavBar';

  Vue.use(Layout);
  Vue.use(Spin);

  export default {
    name: 'Index',
    computed: {
      spinning() {
        return this.$store.getters.loading;
      }
    },
    components: {
      NavBar
    }
  }
</script>
