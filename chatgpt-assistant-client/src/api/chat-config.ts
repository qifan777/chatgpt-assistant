import { ChatConfig, Page, QueryRequest } from 'typings'
import requestWithToken from '../utils/request'

export const queryChatConfig = (query: QueryRequest<ChatConfig>) => {
  return requestWithToken<Page<ChatConfig>>('/chatConfig/query', 'POST', query)
}

export const saveChatConfig = (data: Partial<ChatConfig>) => {
  if (data.id) {
    return requestWithToken<string>(`/chatConfig/${data.id}/update`, 'POST', data)
  }
  return requestWithToken<string>('/chatConfig/create', 'POST', data)
}
export const findChatConfigById = (id: string) => {
  return requestWithToken<ChatConfig>('/chatConfig/' + id, 'GET', {})
}

export const invalidChatConfig = (id: string) => {
  return requestWithToken<boolean>(`/chatConfig/${id}/invalid`, 'POST', {})
}
export const validChatConfig = (id: string) => {
  return requestWithToken<boolean>(`/chatConfig/${id}/valid`, 'POST', {})
}
export const deleteChatConfig = (ids: string[]) => {
  return requestWithToken<boolean>(`/chatConfig/delete`, 'POST', ids)
}
export const getUserChatConfig = () => {
  return requestWithToken<ChatConfig>('/chatConfig/user', 'GET')
}
