import axios from 'axios'

const http = axios.create({
  baseURL: process.env.VUE_APP_API
})

export const setHeader = (accessToken) => {
  http.defaults.headers.contentType = 'application/json'
  http.defaults.headers.Authorization = accessToken === null ? null : `Bearer ${accessToken}`
}

export default http
