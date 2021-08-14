<template>
<div>
  dddddddddddddddddddddddddddddd
</div>
</template>

<script>
export default {
  name: 'oauth2redirect',
  created () {
    console.log('create oauth2redirect')
    const token = this.$route.query.token
    if (token) {
      this.$store.commit('setToken', token)
      this.$emit('getUserDetails')
      this.$router.replace('/profile')
    } else {
      this.$router.replace('/')
    }
  }
}
</script>

<style scoped>

</style>
