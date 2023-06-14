import { ChatSession, Page, QueryRequest } from 'typings'
import requestWithToken from '../utils/request'

export const queryChatSession = (query: QueryRequest<ChatSession>) => {
  return requestWithToken<Page<ChatSession>>('/chatSession/query', 'POST', query)
}

export const saveChatSession = (data: Partial<ChatSession>) => {
  if (data.id) {
    return requestWithToken<string>(`/chatSession/${data.id}/update`, 'POST', data)
  }
  return requestWithToken<string>('/chatSession/create', 'POST', data)
}
export const findChatSessionById = (id: string) => {
  return requestWithToken<ChatSession>('/chatSession/' + id, 'GET', {})
}

export const invalidChatSession = (id: string) => {
  return requestWithToken<boolean>(`/chatSession/${id}/invalid`, 'POST', {})
}
export const validChatSession = (id: string) => {
  return requestWithToken<boolean>(`/chatSession/${id}/valid`, 'POST', {})
}
export const deleteChatSession = (ids: string[]) => {
  return requestWithToken<boolean>(`/chatSession/delete`, 'POST', ids)
}
