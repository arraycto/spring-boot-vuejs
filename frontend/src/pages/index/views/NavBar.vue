<template>
  <a-breadcrumb separator=" " class="nav-bar">
    <a-breadcrumb-item><router-link to="/">主页</router-link></a-breadcrumb-item>
    <a-breadcrumb-item><router-link to="/hello">Hello</router-link></a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script>
  import Vue from 'vue';
  import { Breadcrumb } from 'ant-design-vue';

  Vue.use(Breadcrumb);

  export default {
    name: 'NavBar'
  }
</script>

<style scoped>
  .nav-bar {
    height: 40px;
    border: 1px solid #bdc3c7;
    background-color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
