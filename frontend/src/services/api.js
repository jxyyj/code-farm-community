import { apiClient } from '../stores/user'

// 用户管理API
export const userApi = {
  // 获取用户列表
  getUsers: (page, size) => apiClient.get(`/user/list?page=${page}&size=${size}`),
  
  // 获取用户详情
  getUserById: (id) => apiClient.get(`/user/get/${id}`),
  
  // 新增用户
  addUser: (user) => apiClient.post('/user/add', user),
  
  // 更新用户
  updateUser: (user) => apiClient.put('/user/update', user),
  
  // 删除用户
  deleteUser: (id) => apiClient.delete(`/user/delete/${id}`),
  
  // 批量删除用户
  deleteBatchUsers: (ids) => apiClient.delete('/user/delete/batch', { data: ids }),
  
  // 搜索用户
  searchUsers: (page, size, userName, status) => {
    let url = `/user/search?page=${page}&size=${size}`
    if (userName) url += `&userName=${userName}`
    if (status !== undefined) url += `&status=${status}`
    return apiClient.get(url)
  },
  
  // 分配角色
  assignRoles: (userId, roleIds) => apiClient.post(`/user/${userId}/roles`, roleIds),
  
  // 回收角色
  revokeRoles: (userId, roleIds) => apiClient.delete(`/user/${userId}/roles`, { data: roleIds }),
  
  // 获取用户角色
  getUserRoles: (userId) => apiClient.get(`/user/${userId}/roles`),
  
  // 获取用户权限
  getUserPermissions: (userId) => apiClient.get(`/user/${userId}/permissions`),
  
  // 统计用户总数
  countUsers: () => apiClient.get('/user/count'),
  
  // 修改密码
  changePassword: (userId, oldPassword, newPassword) => apiClient.put(`/user/${userId}/password?oldPassword=${oldPassword}&newPassword=${newPassword}`)
}

// 角色管理API
export const roleApi = {
  // 获取角色列表
  getRoles: () => apiClient.get('/role/list'),
  
  // 获取角色详情
  getRoleById: (id) => apiClient.get(`/role/get/${id}`),
  
  // 新增角色
  addRole: (role) => apiClient.post('/role/add', role),
  
  // 更新角色
  updateRole: (role) => apiClient.put('/role/update', role),
  
  // 删除角色
  deleteRole: (id) => apiClient.delete(`/role/delete/${id}`),
  
  // 批量删除角色
  deleteBatchRoles: (ids) => apiClient.delete('/role/delete/batch', { data: ids }),
  
  // 分配权限
  grantPermissions: (roleId, permissionIds) => apiClient.post(`/role/${roleId}/permissions`, permissionIds),
  
  // 回收权限
  revokePermissions: (roleId, permissionIds) => apiClient.delete(`/role/${roleId}/permissions`, { data: permissionIds }),
  
  // 获取角色权限
  getRolePermissions: (roleId) => apiClient.get(`/role/${roleId}/permissions`),
  
  // 统计角色总数
  countRoles: () => apiClient.get('/role/count')
}

// 权限管理API
export const permissionApi = {
  // 获取权限列表
  getPermissions: () => apiClient.get('/permission/list'),
  
  // 获取权限详情
  getPermissionById: (id) => apiClient.get(`/permission/get/${id}`),
  
  // 新增权限
  addPermission: (permission) => apiClient.post('/permission/add', permission),
  
  // 更新权限
  updatePermission: (permission) => apiClient.put('/permission/update', permission),
  
  // 删除权限
  deletePermission: (id) => apiClient.delete(`/permission/delete/${id}`),
  
  // 批量删除权限
  deleteBatchPermissions: (ids) => apiClient.delete('/permission/delete/batch', { data: ids }),
  
  // 统计权限总数
  countPermissions: () => apiClient.get('/permission/count')
}