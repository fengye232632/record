import request from '@/utils/request'

// 用户登录
export function login(data) {
  console.log('发送登录请求：', data)
  return request({
    url: '/api/auth/login',
    method: 'post',
    data: data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/api/auth/info',
    method: 'get'
  })
}

// 退出登录
export function logout() {
  return request({
    url: '/api/auth/logout',
    method: 'post'
  })
}

// 修改密码
export function updatePassword(data) {
  return request({
    url: '/api/auth/password',
    method: 'put',
    data: data
  })
}

// 更新用户信息
export function updateUserInfo(data) {
  return request({
    url: '/api/auth/info',
    method: 'put',
    data: data
  })
}

// 上传头像
export function uploadAvatar(data) {
  return request({
    url: '/api/auth/avatar',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取用户列表
export function getUserList(query) {
  return request({
    url: '/api/user/list',
    method: 'get',
    params: query
  })
}

// 获取用户详情
export function getUserDetail(id) {
  return request({
    url: '/api/user/' + id,
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/api/user',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/api/user',
    method: 'put',
    data: data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: '/api/user/' + id,
    method: 'delete'
  })
} 