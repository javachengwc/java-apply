import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
  queryBrandPage,
  queryBrandById,
} from '../services/brand';

export default {
  namespace: 'brand',

  state: {
    pageList: {
      //品牌列表
      list: [],
      totalCount: 0,
    },
    itemDetail: {}, // 品牌详情
    queryParams: {
      // 查询参数
      pageNum: 1,
      pageSize: 10,
    },
    showAddForm: false,
  },

  effects: {

    // 分页查询品牌
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.brand.queryParams);
      const response = yield call(queryBrandPage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'localPageList',
          payload: response.data,
        });
      }
    },

    // 查询品牌详情
    *getOne({ data }, { put, call }) {
      const response = yield call(queryBrandById, data);
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
