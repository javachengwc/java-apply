import { routerRedux } from 'dva/router';
import get from 'lodash/get';
import { getCaptcha, loginIn, loginOut, queryCities } from '../services/user';
import { setAuthority } from '../utils/authority';
import { getMenu } from '../routes/User/menu';

export default {
  namespace: 'login',

  state: {
    status: undefined,
    code: undefined,
  },

  effects: {
    *login(param, { call, put, select }) {
      const response = yield call(loginIn, param.data);
      localStorage.removeItem('carDealer');
      // Login successfully
      if (get(response, 'header.ret') === 'S') {
        localStorage.setItem('myuser', JSON.stringify(response.data));
        localStorage.setItem('token', response.data.token);
        yield put.resolve({
          type: 'global/fetchCityTree',
        });
        const cityTree = yield select(state => state.global.cityTree);
        localStorage.setItem('gjCity', JSON.stringify(getNew(cityTree)));
        let path = '';
        //菜单列表
        const menuData = getMenu();
        if (menuData && menuData.length > 0) {
          path += `/${menuData[0].path}/`;
          if (menuData[0].children && menuData[0].children.length > 0) {
            path += menuData[0].children[0].path;
          }
        }
        if (localStorage.getItem('myuser')) {
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
      const response = yield call(loginOut);
      if (get(response, 'header.ret') === 'S') {
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
