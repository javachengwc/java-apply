import { routerRedux } from 'dva/router';
import get from 'lodash/get';
import { getCaptcha, login, logout, queryCities } from '../services/user';
import { setAuthority } from '../utils/authority';
import { getUserMenu } from '../utils/menu';
import { reqSuccess,startWith,endWith } from '../utils/utils';

//alert("me model login is 5");

export default {
  namespace: 'login',

  state: {
    status: undefined,
    code: undefined,
  },

  effects: {
    *login(param, { call, put, select }) {
      //这里将会调用services中对应的loginIn方法
      const response = yield call(login, param.data);
      //登录成功
       if (reqSuccess(response)) {

          //用户权限
          const permissions = {};
           const resolve = ((menus) => {
             menus.forEach((menu) => {
               permissions[menu.perms] = true;
               if (menu.children) {
                 resolve(menu.children);
               }
             });
           });
           resolve(response.data.menus);
           response.data.permissions = permissions;

          //myuser--当前登录用户
          localStorage.setItem('myuser', JSON.stringify(response.data));
          //token--当前登录用户的token
          const token =response.data.token;
          localStorage.setItem('token',token);

          alert("login success.....")

          //菜单列表
          const menuData = getUserMenu();
          let path = '';

          if (menuData && menuData.length > 0) {
            path= menuData[0].path;
            if (menuData[0].children && menuData[0].children.length > 0) {
                path =menuData[0].children[0].path;
            }
            if(!startWith(path,'/')) {
                 path = '/'+path;
            }
            if(endWith(path,'/')) {
                path = path.substring(0,path.length-1);
            }
          }
          if (localStorage.getItem('myuser')) {
            //这里routerRedux.push中的path,是在router中定义的路由路径
            yield put(routerRedux.push(path));
          }
      }
    },
    *getCode(param, { put, call }) {
      const response = yield call(getCaptcha, param.data);
      yield put({
        type: 'saveCode',
        payload: response,
      });
    },
    *logout(_, { call, put }) {
      const response = yield call(logout);
      if (reqSuccess(response)) {
        localStorage.removeItem('token');
        yield put(routerRedux.push('/user/login'));
      }
    },
  },

  reducers: {
    changeLoginStatus(state, { payload }) {
      setAuthority(payload.currentAuthority);
      return {
        ...state,
        status: payload.status,
        type: payload.type,
      };
    },
    saveCode(state, res) {
      return {
        ...state,
        code: res,
      };
    },
  },
};
