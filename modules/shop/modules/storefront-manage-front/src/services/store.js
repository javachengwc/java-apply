import request, { catchError } from '../utils/request';

//分页查询门店
export async function queryStorePage(params) {
  return catchError(
    request('/store/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询门店
export async function queryStoreById(params) {
  return catchError(
    request('/store/getById', {
      method: 'POST',
      body: params,
    })
  );
}
