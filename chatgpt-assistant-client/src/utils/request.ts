import axios, { Method } from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { Result } from '../../typings'

const baseUrl = '/api'
const http = axios.create({
  baseURL: baseUrl,
  timeout: 10000
})
http.interceptors.request.use((request) => {
  const token = window.localStorage.getItem('token')
  request.headers.set('token', token ?? '')
  return request
})
http.interceptors.response.use(
  (res) => {
    return res.data
  },
  ({ response }) => {
    if (response.data.code !== 1) {
      ElMessage.warning({ message: response.data.msg })
    }
    if (response.data.code === 1001007 || response.data.code === 1001008) {
      void router.push('/login')
    } else {
      /* empty */
    }
    return response.data
  }
)
const requestWithToken = async <T>(url: string, method: Method, data?: any): Promise<Result<T>> => {
  if (method === 'get' || method === 'GET') {
    return await http({ url, method, params: data })
  } else {
    return await http({ url, method, data })
  }
}
export default requestWithToken
