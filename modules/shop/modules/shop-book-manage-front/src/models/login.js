import { routerRedux } from 'dva/router';
import get from 'lodash/get';
import { getCaptcha, loginIn, loginOut, queryCities } from '../services/user';
import { setAuthority } from '../utils/authority';
import { getMenu } from '../utils/menu';

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
      if (get(response, 'header.code') === 0) {
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
        let path = '';
        //菜单列表
        alert("yaya!!!!")
        const menuData = getMenu();
        if (menuData && menuData.length > 0) {
          path += `/${menuData[0].path}/`;
          alert(path);
          if (menuData[0].children && menuData[0].children.length > 0) {
            path += menuData[0].children[0].path;
          }
        }
        if (localStorage.getItem('myuser')) {
          alert("login after will tiao to "+path);
          yield put(routerRedux.push(path));
        }
      }
      function getNew(citys) {
        let newData = [];
        let pro = {};
        if (citys != null && citys.length > 0) {
          citys.map(city => {
            for (let key in city) {
              if (key == 'id') {
                pro['value'] = city[key];
              }
              if (key == 'name') {
                pro['label'] = city[key];
              }
              if (key === 'children') {
                if (city[key] != null && city[key].length > 0) {
                  // pro['children'] = getNew(city[key]);
                  let array = [];
                  for (let i = 0; i < city[key].length; i++) {
                    let obj = city[key][i];
                    array.push({ value: obj.id, label: obj.name, children: [] });
                  }
                  pro['children'] = array;
                }
              }
            }
            newData.push(pro);
            pro = {};
          });
        }
        return newData;
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
      if (get(response, 'header.code') === 0) {
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
