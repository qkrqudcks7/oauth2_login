import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import SignUp from '../components/signup'
import Oauth2redirect from '../components/oauth2redirect'
import Login from '../components/login'
import auth from '../custom/authentication'
import Profile from '../components/profile'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/signup',
      name: 'signUp',
      component: SignUp
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/profile',
      name: 'profile',
      component: Profile,
      beforeEnter: auth.isAuthenticated
    },
    {
      path: '/oauth2/redirect',
      name: 'oauth2redirect',
      component: Oauth2redirect,
      beforeEnter: auth.isAuthenticated
    }
  ]
})
