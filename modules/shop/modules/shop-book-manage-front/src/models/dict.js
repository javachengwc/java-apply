import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
  queryDictPage,
  queryDictById,
  addDict,
  updateDict,
  disableDict,
  enableDict,
} from '../services/dict';

export default {
  namespace: 'dict',

  state: {
    dictList: {
      //字典列表
      list: [],
      totalCount: 0,
    },
    itemDetail: {}, // 字典详情
    queryParams: {
      // 查询参数
      pageNum: 1,
      pageSize: 10,
    },
    showAddForm: false,
  },

  effects: {

    // 分页查询字典
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.dict.queryParams);
      const response = yield call(queryDictPage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'localDictList',
          payload: response.data,
        });
      }
    },

    // 查询单个字典详情
    *getOne({ data }, { put, call }) {
      const response = yield call(queryDictById, data);
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
    },
    // 更新字典
    *update({ data }, { put, call }) {
      const response = yield call(updateDict, data);
      if (reqSuccess(response)) {
        message.success('操作成功！');
        yield put({
          type: 'showForm',
          show: false,
        });
        yield put({
          type: 'queryPage',
        });
      }
    },
    // 添加字典
    *add({ data }, { put, call }) {
      const response = yield call(addDict, data);
      if (reqSuccess(response)) {
        message.success('操作成功！');
        yield put({
          type: 'showForm',
          show: false,
        });
        yield put({
          type: 'queryPage',
        });
      }
    },
    //不启用字典
    *unUseDict({ data }, { put, call }) {
      const response = yield call(disableDict, data);
      if (reqSuccess(response)) {
        message.success('操作成功！');
        yield put({
          type: 'queryPage',
        });
      }
    },
    //启用字典
    *useDict({ data }, { put, call }) {
      const response = yield call(enableDict, data);
      if (reqSuccess(response)) {
        message.success('操作成功！');
        yield put({
          type: 'queryPage',
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
    localDictList(state, action) {
      return {
        ...state,
        dictList: action.payload,
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
