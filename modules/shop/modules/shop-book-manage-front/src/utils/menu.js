export function getMenu() {
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
  localStorage.setItem('menuData', JSON.stringify(menuData));
  return menuData;
}
