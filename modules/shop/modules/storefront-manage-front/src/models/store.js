import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
  queryStorePage,
  queryStoreById,
} from '../services/store';

export default {
  namespace: 'store',

  state: {
    pageList: {
      //门店列表
      list: [],
      totalCount: 0,
    },
    itemDetail: {}, // 门店详情
    queryParams: {
      // 查询参数
      pageNum: 1,
      pageSize: 10,
    },
    showAddForm: false,
  },

  effects: {

    // 分页查询门店
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.store.queryParams);
      const response = yield call(queryStorePage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'localPageList',
          payload: response.data,
        });
      }
    },

    // 查询门店详情
    *getOne({ data }, { put, call }) {
      const response = yield call(queryStoreById, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'itemDetail',
          data: response.data,
        });
        yield put({
          type: 'showForm',
          show: true,
        });
      }
    }
  },

  reducers: {
    localParams(state, { data }) {
      return {
        ...state,
        queryParams: data,
      };
    },
    localPageList(state, action) {
      return {
        ...state,
        pageList: action.payload,
      };
    },
    itemDetail(state, { data }) {
      return {
        ...state,
        itemDetail: data,
      };
    },
    showForm(state, { show }) {
      return {
        ...state,
        showAddForm: show,
      };
    },
  },
};
