export function getMenu() {
  let menuData = [];
  let roleList;
  try {
    roleList = JSON.parse(localStorage.getItem('myuser') || '{}').roles || [];
  } catch (e) {}
  if (roleList && roleList.length > 0) {
    menuData = [
      {
        name: '公共模块',
        icon: 'paper-clip',
        path: '',
        children: [{
            name: '字典列表',
            path: '/dict/list',
          },
          {
            name: '广告列表',
            path: '/advert/list',
          },
          {
            name: '问答列表',
            path: '/qa/list',
          }],
      },
      {
        name: '书籍管理',
        icon: 'paper-clip',
        path: '',
        children: [{
          name: '书籍列表',
          path: '/book/list',
        }],
      },
      {
        name: '权限管理',
        icon: 'setting',
        path: '',
        children: [{
          name: '账号管理',
          path: '/rdbc/user',
        }],
      }
    ];
  }
  localStorage.setItem('menuData', JSON.stringify(menuData));
  return menuData;
}
