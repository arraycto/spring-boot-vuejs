<template>
  <a-form-model layout="horizontal" :model="form" @submit.native.prevent="onSubmit" class="login-form" :rules="rules" ref="ruleForm">
    <a-form-model-item v-if="error">
      <a-alert :message="error" type="error" :showIcon="true" />
    </a-form-model-item>
    <a-form-model-item prop="user">
      <a-input v-model="form.user" placeholder="账户">
        <a-icon slot="prefix" type="user" style="color: rgba(0, 0, 0, .25);" />
      </a-input>
    </a-form-model-item>
    <a-form-model-item prop="password">
      <a-input v-model="form.password" type="password" placeholder="密码">
        <a-icon slot="prefix" type="lock" style="color: rgba(0, 0, 0, .25);" />
      </a-input>
    </a-form-model-item>
    <a-form-model-item>
      <a-button type="primary" html-type="submit" :disabled="loading" :loading="loading" block>
        登录
      </a-button>
    </a-form-model-item>
  </a-form-model>
</template>

<script>
  import Vue from 'vue';
  import { FormModel, Input, Icon, Button, Alert } from 'ant-design-vue';

  Vue.use(FormModel);
  Vue.use(Input);
  Vue.use(Icon);
  Vue.use(Button);
  Vue.use(Alert);

  export default {
    name: 'Login',
    data() {
      return {
        rules: {
          user: [
            { required: true, message: '请输入帐户名', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ]
        }
      };
    },
    computed: {
      form() {
        return this.$store.getters['login/user'];
      },
      loading() {
        return this.$store.getters['login/loading'];
      },
      error() {
        return this.$store.getters['login/error'];
      }
    },
    methods: {
      onSubmit() {
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            this.$store.dispatch('login/login', {
              userName: this.form.user,
              password: this.form.password
            });
          } else return false;
        });
      },
    },
  }
</script>

<style scoped>
 .login-form {
    min-width: 260px;
    max-width: 368px;
    margin: 0 auto;
  }
</style>
