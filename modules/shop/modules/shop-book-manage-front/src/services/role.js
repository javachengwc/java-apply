import request, { catchError } from '../utils/request';

//查询角色   queryRoles
export async function queryAllRole() {
  return catchError(
    request('/role/queryAll', {
      method: 'POST',
    })
  );
}
