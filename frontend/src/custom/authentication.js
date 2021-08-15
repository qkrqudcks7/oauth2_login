import vue from '../main'
import store from '../store'

export default {
  isAuthenticated: (to, from, next) => {
    if (store.state.authenticated === null) {
      if (vue) {
        vue.$notify({
          group: 'noti',
          type: 'error',
          text: '접근 권한이 업습니다.'
        })
      }
      next('/')
    }
    next()
  }
}
