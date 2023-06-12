import { ChatMessage, Page, QueryRequest } from 'typings'
import requestWithToken from '../utils/request'

export const queryChatMessage = (query: QueryRequest<ChatMessage>) => {
  return requestWithToken<Page<ChatMessage>>('/chatMessage/query', 'POST', query)
}

export const saveChatMessage = (data: Partial<ChatMessage>) => {
  if (data.id) {
    return requestWithToken<string>(`/chatMessage/${data.id}/update`, 'POST', data)
  }
  return requestWithToken<string>('/chatMessage/create', 'POST', data)
}
export const findChatMessageById = (id: string) => {
  return requestWithToken<ChatMessage>('/chatMessage/' + id, 'GET', {})
}

export const invalidChatMessage = (id: string) => {
  return requestWithToken<boolean>(`/chatMessage/${id}/invalid`, 'POST', {})
}
export const validChatMessage = (id: string) => {
  return requestWithToken<boolean>(`/chatMessage/${id}/valid`, 'POST', {})
}
export const deleteChatMessage = (ids: string[]) => {
  return requestWithToken<boolean>(`/chatMessage/delete`, 'POST', ids)
}
