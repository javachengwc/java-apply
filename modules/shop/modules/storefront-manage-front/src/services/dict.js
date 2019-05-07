import request, { catchError } from '../utils/request';

//分页查询字典
export async function queryDictPage(params) {
  return catchError(
    request('/dict/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//新增字典
export async function addDict(params) {
  return catchError(
    request('/dict/add', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//修改字典
export async function updateDict(params) {
  return catchError(
    request('/dict/update', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询字典
export async function queryDictById(params) {
  return catchError(
    request('/dict/getById', {
      method: 'POST',
      body: params,
    })
  );
}

//不启用字典
export async function disableDict(params) {
  return catchError(
    request('/dict/unUse', {
      method: 'POST',
      body: params,
    })
  );
}

//启用字典
export async function enableDict(params) {
  return catchError(
    request('/dict/use', {
      method: 'POST',
      body: params,
    })
  );
}
