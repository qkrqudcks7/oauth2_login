'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  VUE_APP_ORIGIN: '"http://localhost"',
  VUE_APP_API: '"http://localhost:8080"',
  VUE_APP_OAUTH2_REDIRECT_URI: '"/oauth2/redirect"'
})
