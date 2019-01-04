//引入services目录下的组件，就会先加载它
import { queryNotices } from '../services/api';
import { queryCities } from '../services/user';
import { queryOnlyMenuList } from '../services/menu';
import { reqSuccess } from '../utils/utils';
import { transMenu,formatterMenu } from '../utils/menu';

//alert("me global is 3");

export default {
  namespace: 'global',

  state: {
    collapsed: false,
    notices: [],
    cityTree: [],
    platMenus:[], //后台菜单
  },

  //effects中定义的函数method，外部就能通过yield put({type: 'namespace/method',...})调用它。
  effects: {
    *fetchNotices(_, { call, put }) {
      const data = yield call(queryNotices);
      yield put({
        type: 'saveNotices',
        payload: data,
      });
      yield put({
        type: 'user/changeNotifyCount',
        payload: data.length,
      });
    },
    *clearNotices({ payload }, { put, select }) {
      yield put({
        type: 'saveClearedNotices',
        payload,
      });
      const count = yield select(state => state.global.notices.length);
      yield put({
        type: 'user/changeNotifyCount',
        payload: count,
      });
    },
    *fetchCityTree(_, { put, call, select }) {
      let cityTree = [];
      const hasFetched = yield select(state => state.global.cityTree);
      if (hasFetched.length) {
        yield put({
          type: 'cityTree',
          data: hasFetched,
        });
        return;
      }
      try {
        cityTree = JSON.parse(localStorage.getItem('cityTree'));
      } catch (e) {}
      if (!cityTree || !cityTree.length) {
        const res = yield call(queryCities);
        if (reqSuccess(res)) {
          cityTree = res.data;
          localStorage.setItem('cityTree', JSON.stringify(cityTree));
        }
      }
      yield put({
        type: 'cityTree',
        data: cityTree,
      });
    },
    *queryPlatMenu(_, { call, put }) {
        //查询平台菜单
        const response = yield call(queryOnlyMenuList);
        let menuData = [];
        let resultData = [];
        if (reqSuccess(response)) {
            resultData = response.data || [];
         }
        if (resultData && resultData.length > 0) {
            let transData= [];
            transData = transMenu(resultData);
            resolve(transData, menuData);
            menuData=formatterMenu(menuData);
         }
        yield put({
          type: 'localPlatMenu',
          payload: menuData,
        });
    },
  },

  reducers: {
    changeLayoutCollapsed(state, { payload }) {
      return {
        ...state,
        collapsed: payload,
      };
    },
    saveNotices(state, { payload }) {
      return {
        ...state,
        notices: payload,
      };
    },
    saveClearedNotices(state, { payload }) {
      return {
        ...state,
        notices: state.notices.filter(item => item.type !== payload),
      };
    },
    cityTree(state, { data }) {
      return {
        ...state,
        cityTree: data,
      };
    },
    localPlatMenu(state, { payload }) {
      return {
        ...state,
        platMenus: payload,
      };
    },
  },

  subscriptions: {
    setup({ history }) {
      // Subscribe history(url) change, trigger `load` action if pathname is `/`
      return history.listen(({ pathname, search }) => {
        if (typeof window.ga !== 'undefined') {
          window.ga('send', 'pageview', pathname + search);
        }
      });
    },
  },
};
