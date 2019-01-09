import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
    queryAllRole,
    queryRolePage,
    queryRoleById,
    addRole,
    batchDelRole,
    updateRole
 } from '../services/role';

export default {
  namespace: 'role',

  state: {
    pageList: {
      //角色列表
      list: [],
      totalCount: 0,
    },
    itemDetail: {}, // 角色详情
    queryParams: {
      // 查询参数
      pageNum: 1,
      pageSize: 10,
    },
    showAddForm: false,
    shouldResetAddForm: false,
  },

  effects: {
    //分页查询角色
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.role.queryParams);
      const response = yield call(queryRolePage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'localPageList',
          payload: response.data,
        });
      }
    },

    //添加角色
    *add({ data }, { put }) {
      const response = yield addRole(data);
      if (reqSuccess(response)) {
        message.success('添加成功');
        yield put({
          type: 'showForm',
          show: false,
        });
        yield put({
          type: 'shouldResetAddForm',
          data: true,
        });
        yield put({
          type: 'queryPage',
        });
      }
    },

    //删除角色
    *del({ data }, { put }) {
      const response = yield batchDelRole(data);
      if (reqSuccess(response)) {
        message.success('删除成功');
        yield put({
          type: 'queryPage',
        });
      }
    },

    //查询角色详情
    *getOne({ data }, { put }) {
      const response = yield queryRoleById(data);
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

    //更新角色
    *update({ data }, { put }) {
      const response = yield updateRole(data);
      if (reqSuccess(response)) {
        message.success('修改成功');
        yield put({
          type: 'showForm',
          show: false,
        });
        yield put({
          type: 'shouldResetAddForm',
          data: true,
        });
        yield put({
          type: 'queryPage',
        });
      }
    },

    //显示角色增改表单
    *showAddForm(_, { put }) {
      yield put({
        type: 'menu/queryTree',
      });
      yield put({
        type: 'showForm',
        show: true,
      });
    },

    //修改角色
    *modify({ data }, { put }) {
      yield put({
        type: 'menu/queryTree',
      });
      yield put({
        type: 'getOne',
        data,
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
    shouldResetAddForm(state, { data }) {
      return {
        ...state,
        shouldResetAddForm: data,
      };
    },
  },

};
