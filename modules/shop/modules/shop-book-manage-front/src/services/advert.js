import request, { catchError } from '../utils/request';

//分页查询广告
export async function queryAdvertPage(params) {
  return catchError(
    request('/advert/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询广告
export async function queryAdvertById(params) {
  return catchError(
    request('/advert/getById', {
      method: 'POST',
      body: params,
    })
  );
}
