import request, { catchError } from '../utils/request';

export async function query() {
  return catchError(request('/api/users'));
}

export async function queryCurrent() {
  return request('/api/currentUser');
}
/**
 * 登录获取验证码
 */
export async function getCaptcha(params) {
  return catchError(
    request('/getCaptcha', {
      method: 'POST',
      body: params,
    })
  );
}
/**
 * 登录
 */
export async function loginIn(params) {
  return catchError(
    request('/login', {
      method: 'POST',
      body: params,
    })
  );
}

/**
 * 退出
 */
export async function loginOut() {
  return catchError(
    request('/logout', {
      method: 'POST',
    })
  );
}

/**
 * 分页查询用户
 */
export async function queryUsers(params) {
  return catchError(
    request('/user/queryUsers', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

/**
 * 新增用户
 */
export async function addUser(params) {
  return catchError(
    request('/user/add', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

/**
 * 修改用户
 */
export async function updateUser(params) {
  return catchError(
    request('/user/update', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}
/**
 * 根据id查询用户
 */
export async function queryUserById(params) {
  return catchError(
    request('/user/queryById', {
      method: 'POST',
      body: params,
    })
  );
}
/**
 * 查询角色
 */
export async function queryRoles() {
  return catchError(
    request('/role/queryAll', {
      method: 'POST',
    })
  );
}

/**
 * 查询城市
 */
export async function queryCities() {
  return catchError(
    request('/city/queryTrees', {
      method: 'POST',
    })
  );
}

/**
 * 禁用用户
 */
export async function disable(params) {
  return catchError(
    request('/user/disable', {
      method: 'POST',
      body: params,
    })
  );
}

/**
 * 启用用户
 */
export async function enable(params) {
  return catchError(
    request('/user/enable', {
      method: 'POST',
      body: params,
    })
  );
}
