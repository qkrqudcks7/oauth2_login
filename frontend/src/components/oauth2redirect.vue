<template>
  <div class="signup-container">
    <div class="signup-content">
      <div class="or-separator">
        <span class="or-text">나머지 회원가입을 해주세요!</span>
      </div>
      <form @submit.prevent="signup">
        <div class="form-item">
          <input type="text" name="name"
                 class="form-control" placeholder="Name"
                 v-model="this.$store.getters.user.name" readonly/>
        </div>
        <div class="form-item">
          <input type="email" name="email"
                 class="form-control" placeholder="Email"
                 v-model="this.$store.getters.user.email" readonly/>
        </div>
        <div class="form-item">
          <input type="password" name="password"
                 class="form-control" placeholder="Password"
                 v-model="user.password"/>
        </div>
        <div class="form-item">
          <button type="submit" class="btn btn-block btn-primary">Sign Up</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import notification from '../custom/notification'

export default {
  name: 'oauth2redirect',
  data () {
    return {
      user: {
        name: '',
        email: '',
        password: ''
      }
    }
  },
  methods: {
    async signup () {
      this.user.name = this.$store.getters.user.name
      this.user.email = this.$store.getters.user.email
      console.log(this.user)
      try {
        const response = await this.axios.post(`/auth/oauth2signup/${this.user.email}`, this.user)
        notification.success(response, '회원가입 성공', () => {
          this.$router.push('/login')
        })
      } catch (err) {
        this.$notify({
          group: 'noti',
          type: 'error',
          duration: 6000,
          title: err.response.state,
          text: err.response.data.message
        })
      }
    }
  },
  created () {
    console.log('create oauth2redirect')
    const token = this.$route.query.token
    if (token) {
      this.$store.commit('setToken', token)
      this.$emit('getUserDetails')
      // push를 사용할 경우 history에 남아서 back을 누를 경우 이 페이지로 돌아올 수 있다
      // this.$router.replace('/profile')
    } else {
      this.$router.replace('/')
    }
  }
}
</script>

<style scoped>
.login-container {
  text-align: center;
}

.login-content {
  background: #fff;
  box-shadow: 0 1px 11px rgba(0, 0, 0, 0.27);
  border-radius: 2px;
  width: 500px;
  display: inline-block;
  margin-top: 30px;
  vertical-align: middle;
  position: relative;
  padding: 35px;
}

.social-btn {
  margin-bottom: 15px;
  font-weight: 400;
  font-size: 16px;
}

.social-btn img {
  height: 32px;
  float: left;
  margin-top: 10px;
}

.social-btn.google {
  margin-top: 7px;
}

.social-btn.facebook img {
  height: 24px;
  margin-left: 3px;
}

.social-btn.github img {
  height: 24px;
  margin-left: 3px;
}

.signup-link {
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
}

.login-title {
  font-size: 1.5em;
  font-weight: 500;
  margin-top: 0;
  margin-bottom: 30px;
  color: rgba(0, 0, 0, 0.65);
}
</style>
