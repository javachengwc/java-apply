import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
  queryMenuPage,
  queryMenuById,
  queryMenuTree,
  addMenu,
  batchDelMenu,
  updateMenu
} from '../services/menu';

export default {
  namespace: 'menu',

  state: {
    pageList: {
      //菜单列表
      list: [],
      totalCount: 0,
    },
    itemDetail: {}, // 菜单详情
    queryParams: {
      // 查询参数
      pageNum: 1,
      pageSize: 10,
    },
    treeListData: [],
    showAddForm: false,
  },

  effects: {

    // 分页查询菜单
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.menu.queryParams);
      const response = yield call(queryMenuPage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'localPageList',
          payload: response.data,
        });
      }
    },

    // 查询单个菜单详情
    *getOne({ data }, { put, call }) {
      const response = yield call(queryMenuById, data);
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

    //查询菜单树
    *queryTree(_, { call, put }) {
      const response = yield call(queryMenuTree);
      if (reqSuccess(response)) {
        yield put({
          type: 'treeListData',
          data: response.data,
        });
      }
    },

    //增加菜单
    *add({ data }, { call, put }) {
      const response = yield call(addMenu, data);
      if (reqSuccess(response)) {
        message.success('添加成功');
        yield put({
          type: 'showForm',
          data: false,
        });
        yield put({
          type: 'queryTree',
        });
      }
    },

    //删除菜单
    *del({ data }, { call, put }) {
      const res = yield call(batchDelMenu, data);
      if (reqSuccess(response)) {
        message.success('删除成功');
        yield put({
          type: 'queryTree',
        });
      }
    },

    //修改菜单
    *update({ data }, { call, put }) {
      const res = yield call(updateMenu, data);
      if (reqSuccess(response)) {
        message.success('修改成功');
        yield put({
          type: 'showForm',
          data: false,
        });
        yield put({
          type: 'queryTree',
        });
      }
    },

    //更新查询条件
    *updateQueryData({ data }, { put }) {
      yield put({
        type: 'localParams',
        data,
      });
      yield put({
        type: 'queryPage',
      });
    },

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
    treeListData(state, { data }) {
      return {
        ...state,
        treeListData: data,
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
