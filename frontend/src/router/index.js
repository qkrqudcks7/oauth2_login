import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import SignUp from '../components/signup'
import Oauth2redirect from '../components/oauth2redirect'
// import auth from '../custom/authentication'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/signup',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/oauth2/redirect',
      name: 'Oauth2redirect',
      component: Oauth2redirect
    }
  ]
})
