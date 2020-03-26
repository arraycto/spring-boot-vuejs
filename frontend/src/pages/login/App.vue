<template>
  <a-spin tip="加载中..." :spinning="spinning" size="large" class="h100">
    <div class="spin-content h100">
      <a-layout class="h100">
        <a-layout-content class="container">
          <div class="top">
            <div class="header">
              <img src="../../assets/images/spring-boot-vuejs-logo.png" alt="logo" class="logo">
              <span class="title"><span>Vue.js</span> <span class="xs-hidden">&</span> <span>Spring Boot</span></span>
            </div>
            <div class="desc">
              整合 Vue.js 与 Spring Boot 的 Demo 项目
            </div>
          </div>
          <Login />
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
  import Login from './views/Login';

  Vue.use(Layout);
  Vue.use(Spin);

  export default {
    name: 'App',
    computed: {
      spinning() {
        return this.$store.getters.loading;
      }
    },
    components: {
      Login
    }
  }
</script>

<style scoped>
  .container {
    padding: 110px 0 0;
  }

  .top {
    text-align: center;
    min-width: 260px;
  }

  .header {
    height: 100%;
  }

  .logo {
    height: 50px;
  }

  .title {
    font-size: 33px;
    color: rgba(0, 0, 0, .85);
    font-weight: 600;
    display: block;
  }

  .desc {
    color: rgba(0, 0, 0, .45);
    margin-top: 12px;
    margin-bottom: 40px;
  }

  @media (max-width: 540px) {
    .title span {
      display: block;
    }

    .title span.xs-hidden {
      display: none;
    }
  }
</style>
