import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
  queryAdvertPage,
  queryAdvertById,
} from '../services/advert';

export default {
  namespace: 'advert',

  state: {
    pageList: {
      //广告列表
      list: [],
      totalCount: 0,
    },
    itemDetail: {}, // 广告详情
    queryParams: {
      // 查询参数
      pageNum: 1,
      pageSize: 10,
    },
    showAddForm: false,
  },

  effects: {

    // 分页查询广告
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.advert.queryParams);
      const response = yield call(queryAdvertPage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'localPageList',
          payload: response.data,
        });
      }
    },

    // 查询单个广告详情
    *getOne({ data }, { put, call }) {
      const response = yield call(queryAdvertById, data);
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
