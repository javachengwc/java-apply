import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
  queryQaPage,
  queryQaById,
} from '../services/qa';

export default {
  namespace: 'qa',

  state: {
    pageList: {
      //问答列表
      list: [],
      totalCount: 0,
    },
    itemDetail: {}, //问答详情
    queryParams: {
      //查询参数
      pageNum: 1,
      pageSize: 10,
    },
    showAddForm: false,
  },

  effects: {

    // 分页查询问答
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.qa.queryParams);
      const response = yield call(queryQaPage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'localPageList',
          payload: response.data,
        });
      }
    },

    // 查询问答详情
    *getOne({ data }, { put, call }) {
      const response = yield call(queryQaById, data);
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
