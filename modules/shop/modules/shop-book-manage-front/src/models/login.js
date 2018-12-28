import { routerRedux } from 'dva/router';
import get from 'lodash/get';
import { getCaptcha, loginIn, loginOut, queryCities } from '../services/user';
import { setAuthority } from '../utils/authority';
import { getMenu } from '../utils/menu';
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
      const response = yield call(loginIn, param.data);
      //登录成功
       if (reqSuccess(response)) {
        //myuser--当前登录用户
        localStorage.setItem('myuser', JSON.stringify(response.data));
        //token--当前登录用户的token
        const token =response.data.token;
        alert(token);
        localStorage.setItem('token',token);
//        yield put.resolve({
//          type: 'global/fetchCityTree',
//        });
//        const cityTree = yield select(state => state.global.cityTree);
//        localStorage.setItem('gjCity', JSON.stringify(getNew(cityTree)));

        alert("login success.....")

        //菜单列表
        const menuData = getMenu();
        let path = '';

        if (menuData && menuData.length > 0) {
          path += `${menuData[0].path}`;
          if(endWith(path,'/')) {
              path = path.substring(0,path.length-1);
          }
          if (menuData[0].children && menuData[0].children.length > 0) {
              let childPath =menuData[0].children[0].path;
              if(startWith(childPath,'/')) {
                path +=childPath;
              }else {
                path += '/'+childPath;
              }
          }
        }
        if (localStorage.getItem('myuser')) {
          alert("login after will tiao to "+path);
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
    *loginOut(_, { call, put }) {
      alert("models *loginOut invoked...")
      const response = yield call(loginOut);
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
