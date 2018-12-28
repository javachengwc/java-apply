import request, { catchError } from '../utils/request';

//分页查询问答
export async function queryQaPage(params) {
  return catchError(
    request('/qa/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询问答
export async function queryQaById(params) {
  return catchError(
    request('/qa/getById', {
      method: 'POST',
      body: params,
    })
  );
}
