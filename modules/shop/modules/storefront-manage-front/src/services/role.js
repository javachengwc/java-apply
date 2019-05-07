import request, { catchError } from '../utils/request';

//分页查询角色
export async function queryRolePage(params) {
  return catchError(
    request('/role/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//查询所有角色
export async function queryAllRole() {
  return catchError(
    request('/role/queryAll', {
      method: 'POST',
    })
  );
}

//查询角色详情
export async function queryRoleById(data) {
  return catchError(request('/role/getById', {
    method: 'POST',
    body: data,
  }));
}

//增加角色
export async function addRole(data) {
  return catchError(request('/role/add', {
    method: 'POST',
    body: data,
  }));
}

//批量删除角色
export async function batchDelRole(data) {
  return catchError(request('/role/batchDel', {
    method: 'POST',
    body: data,
  }));
}

//更新角色
export async function updateRole(data) {
  return catchError(request('/role/update', {
    method: 'POST',
    body: data,
  }));
}
