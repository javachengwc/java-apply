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

//查询菜单树
export async function queryMenuTree() {
  return catchError(request('/menu/queryMenuTree', {
    method: 'POST',
  }));
}

//添加菜单
export async function addMenu(data) {
  return catchError(request('/menu/add', {
    method: 'POST',
    body: data,
  }));
}

//删除菜单
export async function batchDelMenu(data) {
  return catchError(request('/menu/batchDel', {
    method: 'POST',
    body: data,
  }));
}

//修改菜单
export async function updateMenu(data) {
  return catchError(request('/menu/update', {
    method: 'POST',
    body: data,
  }));
}


