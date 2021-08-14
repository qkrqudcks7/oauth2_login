import Vue from 'vue'
import Vuex from 'vuex'

import {setHeader} from '../custom/axios.custom'

Vue.use(Vuex)

const initUser = (store) => {
  const {ACCESS_TOKEN} = localStorage
  if (ACCESS_TOKEN) {
    store.commit('setToken', ACCESS_TOKEN)
  }
}

export default new Vuex.Store({
  // 추가작업 가능한 요소
  plugins: [initUser],
  state: {
    authenticated: false,
    token: null,
    currentUser: null
  },
  getters: {
    token (state) {
      return state.token
    },
    user (state) {
      return state.currentUser
    },
    authenticated (state) {
      return state.authenticated
    }
  },
  mutations: {
    setToken (state, accessToken) {
      state.token = accessToken
      localStorage.ACCESS_TOKEN = accessToken
      setHeader(accessToken)
    },
    setUserDetail (state, payload) {
      state.currentUser = payload
      state.authenticated = payload !== null
    }
  },
  actions: {
    setToken ({commit}, payload) {
      commit('setToken', payload)
    },
    setUserDetail ({commit}, payload) {
      commit('setUserDetail', payload)
    }
  }
})
