import request, { catchError } from '../utils/request';

//分页查询书籍
export async function queryBookPage(params) {
  return catchError(
    request('/book/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询书籍
export async function queryBookById(params) {
  return catchError(
    request('/book/getById', {
      method: 'POST',
      body: params,
    })
  );
}
