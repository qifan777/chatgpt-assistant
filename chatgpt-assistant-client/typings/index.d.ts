export interface User extends BaseEntity {
  avatar: string
  nickname: string
  username: string
  password: string
}
export class LoginResponse {
  tokenName: string
  tokenValue: string
  loginId: string
}

export type EditMode = 'CREATE' | 'EDIT'

export interface MyFile {
  name: string
  path: string
  status: 'ready' | 'uploading' | 'finish'
  file?: File
}

export interface QueryRequest<T> {
  pageNum: number
  pageSize: number
  keyword: string
  query?: Partial<T>
}

export interface Page<T> {
  list: T[]
  total: number
  pageSize: number
  pageNum: number
  totalPages: number
}

export interface Result<T> {
  code: number
  success?: boolean
  msg: string
  result: T
}

export interface BaseEntity {
  id: string
  updatedAt: string
  createdAt: string
  validStatus: 'VALID' | 'INVALID'
}
