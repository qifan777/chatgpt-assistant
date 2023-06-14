import { LoginResponse, Page, QueryRequest, User } from 'typings'
import requestWithToken from '../utils/request'

export const queryUser = (query: QueryRequest<User>) => {
  return requestWithToken<Page<User>>('/user/query', 'POST', query)
}

export const saveUser = (data: Partial<User>) => {
  if (data.id) {
    return requestWithToken<string>(`/user/${data.id}/update`, 'POST', data)
  }
  return requestWithToken<string>('/user/create', 'POST', data)
}
export const findUserById = (id: string) => {
  return requestWithToken<User>('/user/' + id, 'GET', {})
}

export const invalidUser = (id: string) => {
  return requestWithToken<boolean>(`/user/${id}/invalid`, 'POST', {})
}
export const validUser = (id: string) => {
  return requestWithToken<boolean>(`/user/${id}/valid`, 'POST', {})
}
export const deleteUser = (ids: string[]) => {
  return requestWithToken<boolean>(`/user/delete`, 'POST', ids)
}
export const loginUser = (user: Partial<User>) => {
  return requestWithToken<LoginResponse>('/user/login', 'POST', user)
}
export const getUserInfo = () => {
  return requestWithToken<User>('/user/info', 'GET')
}
