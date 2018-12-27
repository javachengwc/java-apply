import { isUrl } from '../utils/utils';
//在router中引用了此menu

function getMenu() {
  let menuData = [];
  let roleList;
  try {
    roleList = JSON.parse(localStorage.getItem('myuser') || '{}').roles || [];
  } catch (e) {}
  if (roleList && roleList.length > 0) {
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
