import request, { catchError } from '../utils/request';

//分页查询行业
export async function queryIndustryPage(params) {
  return catchError(
    request('/industry/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询行业
export async function queryIndustryById(params) {
  return catchError(
    request('/industry/getById', {
      method: 'POST',
      body: params,
    })
  );
}
