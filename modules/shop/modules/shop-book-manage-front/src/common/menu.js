import { isUrl,startWith } from '../utils/utils';
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
      		name: '公共模块',
      		icon: 'home',
      		path: '',
      		children: [
      			{
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
      			}
      		],
      },
      {
      	name: '书籍管理',
      	icon: 'paper-clip',
      	path: '',
      	children: [
          {
      			name: '书籍列表',
      			path: '/book/list',
      		}
        ],
      },
      {
      	name: '权限管理',
      	icon: 'setting',
      	path: '',
      	children: [
          {
      			name: '账号管理',
      			path: '/rdbc/user',
      		}
        ],
      }
    ];
  }
  return menuData;
}
function formatter(data, parentPath = '/', parentAuthority) {
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
      result.children = formatter(item.children, `${parentPath}${item.path}/`, item.authority);
    }
    return result;
  });
}

export const getMenuData = () => formatter(getMenu());
