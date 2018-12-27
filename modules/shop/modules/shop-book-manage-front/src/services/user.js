import request, { catchError } from '../utils/request';

export async function query() {
  return catchError(request('/api/users'));
}

export async function queryCurrent() {
  return request('/api/currentUser');
}

//获取验证码
export async function getCaptcha(params) {
  return catchError(
    request('/sms/getCaptcha', {
      method: 'POST',
      body: params,
    })
  );
}

//登录
export async function loginIn(params) {
  alert("services user loginIn start");
  return catchError(
    request('/login', {
      method: 'POST',
      body: params,
    })
  );
}

//退出
export async function loginOut() {
alert("services user loginOut start");
  return catchError(
    request('/logout', {
      method: 'POST',
    })
  );
}

//分页查询用户
export async function queryUserPage(params) {
  return catchError(
    request('/user/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//新增用户
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

//修改用户
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

//根据id查询用户
export async function queryUserById(params) {
  return catchError(
    request('/user/getById', {
      method: 'POST',
      body: params,
    })
  );
}

//禁用用户
export async function disableUser(params) {
  return catchError(
    request('/user/disable', {
      method: 'POST',
      body: params,
    })
  );
}

//启用用户
export async function enableUser(params) {
  return catchError(
    request('/user/enable', {
      method: 'POST',
      body: params,
    })
  );
}
