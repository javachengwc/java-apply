import { isUrl } from '../utils/utils';

let menuData = [];
function getMenu() {
  let roleVos;
  try {
    roleVos = JSON.parse(localStorage.getItem('myuser') || '{}').roleVos || [];
  } catch (e) {}
  if (roleVos && roleVos.length > 0) {
    menuData = [
      {
        name: '账户管理',
        icon: 'setting',
        path: 'manage/user',
        children: [],
      },
    ];
  }
  return menuData;
}
function formatter(data, parentPath = '/', parentAuthority) {
  return data.map(item => {
    let { path } = item;
    if (!isUrl(path)) {
      path = parentPath + item.path;
    }
    const result = {
      ...item,
      path,
      authority: item.authority || parentAuthority,
    };
    if (item.children) {
      result.children = formatter(item.children, `${parentPath}${item.path}/`, item.authority);
    }
    return result;
  });
}

export const getMenuData = () => formatter(getMenu());
