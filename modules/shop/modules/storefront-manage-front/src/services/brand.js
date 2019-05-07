import request, { catchError } from '../utils/request';

//分页查询品牌
export async function queryBrandPage(params) {
  return catchError(
    request('/brand/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询品牌
export async function queryBrandById(params) {
  return catchError(
    request('/brand/getById', {
      method: 'POST',
      body: params,
    })
  );
}
