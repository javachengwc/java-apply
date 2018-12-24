export function getMenu() {
  let menuData = [];
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
  localStorage.setItem('menuData', JSON.stringify(menuData));
  return menuData;
}
