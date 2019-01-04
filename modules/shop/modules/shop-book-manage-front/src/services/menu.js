import request, { catchError } from '../utils/request';

//分页查询菜单
export async function queryMenuPage(params) {
  return catchError(
    request('/menu/queryPage', {
      method: 'POST',
      body: {
        ...params,
      },
    })
  );
}

//根据id查询菜单
export async function queryMenuById(params) {
  return catchError(
    request('/menu/getById', {
      method: 'POST',
      body: params,
    })
  );
}

//查询类型是菜单的菜单列表
export async function queryOnlyMenuList() {
  return catchError(
    request('/menu/queryOnlyMenuList', {
      method: 'POST',
    })
  );
}
