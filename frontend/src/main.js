// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import Notification from 'vue-notification'
import axios from './custom/axios.custom'

Vue.use(Notification)
Vue.prototype.axios = axios
Vue.config.productionTip = false

/* eslint-disable no-new */
const app = new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})
export default app
