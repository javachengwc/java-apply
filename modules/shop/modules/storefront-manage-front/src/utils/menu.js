import { isUrl,startWith,endWith } from './utils';

export function getUserMenu() {
  let menuData = [];
  let userMenu;
  try {
    userMenu = JSON.parse(localStorage.getItem('myuser') || '{}').menus || [];
  } catch (e) {}
  if (userMenu && userMenu.length > 0) {
    let transData= [];
    transData = transMenu(userMenu);
    resolve(transData, menuData);
    menuData=formatterMenu(menuData);
//    menuData = [
//       {
//       		name: '公共模块',
//       		icon: 'home',
//       		path: 'common',
//       		children: [
//       			{
//       				name: '字典列表',
//       				path: 'dict',
//       			},
//       			{
//       				name: '广告列表',
//       				path: 'advert',
//       			},
//             {
//       				name: '问答列表',
//       				path: 'qa',
//       			}
//       		],
//       },
//       {
//       	name: '书籍管理',
//       	icon: 'paper-clip',
//       	path: 'book',
//       	children: [
//           {
//       			name: '书籍列表',
//       			path: 'booklist',
//       		}
//         ],
//       },
//       {
//       	name: '权限管理',
//       	icon: 'setting',
//       	path: 'rdbc',
//       	children: [
//           {
//       			name: '账号管理',
//       			path: 'user',
//       		}
//         ],
//       }
//    ];
  }
  localStorage.setItem('menuData', JSON.stringify(menuData));
  return menuData;
}

//转换菜单，把从接口返回的菜单数据转换成页面
export function transMenu(orglMenu) {
    let data = orglMenu || [];
    if (data && data.length > 0) {
        data.forEach(per => {
            per.path =per.url;
            if (per.children && per.children.length > 0) {
                data.forEach(child => {
                     child.path= child.url;
                });
            }
        });
    }
    return data;
}

export function formatterMenu(data, parentPath = '/', parentAuthority) {
  return data.map(item => {
    let { path } = item;
    if (!isUrl(path)) {
      if(startWith(item.path,'/')) {
        path =parentPath + item.path.substring(1);
      }else {
        path = parentPath + item.path;
      }
    }
    const result = {
      ...item,
      path,
      authority: item.authority || parentAuthority,
    };
    if (item.children) {
      result.children = formatterMenu(item.children, `${parentPath}${item.path}/`, item.authority);
    }
    return result;
  });
}

 function resolve(menus, parent) {
   for (let i = 0; i < menus.length; i++) {
     const menu = menus[i];
     if (menu.type !== 2 && menu.nav) {
       const children = [];
       if (menu.children && menu.children.length) {
         resolve(menu.children, children);
       }
       parent.push({
         name: menu.name,
         path: menu.url,
         icon: menu.type !== 2 ? (menu.icon ? menu.icon : null) : null,
         children,
       });
     }
   }
 }
