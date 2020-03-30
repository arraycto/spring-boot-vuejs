<template>
  <h3>{{ message }}</h3>
</template>

<script>
  export default {
    name: 'Hello',
    computed: {
      message() {
        return this.$store.getters['hello/message'];
      }
    },
    created() {
      this.$store.dispatch('hello/hello');
    }
  }
</script>
