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
       		icon: 'home',
       		path: 'common',
       		children: [
       			{
       				name: '字典列表',
       				path: 'dict',
       			},
       			{
       				name: '广告列表',
       				path: 'advert',
       			},
             {
       				name: '问答列表',
       				path: 'qa',
       			}
       		],
       },
       {
       	name: '书籍管理',
       	icon: 'paper-clip',
       	path: 'book',
       	children: [
           {
       			name: '书籍列表',
       			path: 'booklist',
       		}
         ],
       },
       {
       	name: '权限管理',
       	icon: 'setting',
       	path: 'rdbc',
       	children: [
           {
       			name: '账号管理',
       			path: 'user',
       		}
         ],
       }
    ];
  }
  localStorage.setItem('menuData', JSON.stringify(menuData));
  return menuData;
}
