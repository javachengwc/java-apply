import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import {
  queryUserPage,
  queryUserById,
  updateUser,
  addUser,
  disableUser,
  enableUser,
} from '../services/user';

import { queryAllRole } from '../services/role';

export default {
  namespace: 'user',

  state: {
    currentUser: {}, // 当前对象的信息存储
    userList: {
      // 用户列表
      list: [],
      totalCount: 0,
    },
    roles: {
      // 全部角色
      data: [],
    },
    itemDetail: {}, // 用户详情
    getListParams: {
      // 查询参数
      pageNum: 1,
      pageSize: 10,
    },
    showAddForm: false,
    shouldResetAddForm: false,
  },

  effects: {
    // 获取localStorag里面的用户信息
    *fetchCurrent(_, { put }) {
      yield put({
        type: 'saveCurrentUser',
        payload: JSON.parse(localStorage.getItem('myuser')),
      });
    },
    // 分页查询用户
    *queryPage(_, { put, call, select }) {
      const data = yield select(state => state.user.getListParams);
      const response = yield call(queryUserPage, data);
      if (reqSuccess(response)) {
        yield put({
          type: 'saveUserList',
          payload: response.data,
        });
      }
    },
    // 查询角色
    *queryAllRole(_, { put, call }) {
      const response = yield call(queryAllRole);
      if (reqSuccess(response)) {
       //把所有角色保存到本地
        yield put({
          type: 'saveRoles',
          payload: response,
        });
      }
    },
    // 查询单个用户详情
    *getOne({ data }, { put, call }) {
      const response = yield call(queryUserById, data);
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
    // 更新用户
    *update({ data }, { put, call }) {
      const response = yield call(updateUser, data);
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
    // 添加用户
    *add({ data }, { put, call }) {
      const response = yield call(addUser, data);
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
    //冻结用户
    *disableUser({ data }, { put, call }) {
      const response = yield call(disableUser, data);
      if (reqSuccess(response)) {
        message.success('操作成功！');
        yield put({
          type: 'queryPage',
        });
      }
    },
    //启用用户
    *enableUser({ data }, { put, call }) {
      const response = yield call(enableUser, data);
      if (reqSuccess(response)) {
        message.success('操作成功！');
        yield put({
          type: 'queryPage',
        });
      }
    },
    *showAddForm(_, { put }) {
      yield put({
        type: 'queryAllRole',
      });
      yield put({
        type: 'showForm',
        show: true,
      });
    },
  },

  reducers: {
    getListParams(state, { data }) {
      return {
        ...state,
        getListParams: data,
      };
    },
    saveUserList(state, action) {
      return {
        ...state,
        userList: action.payload,
      };
    },
    saveCurrentUser(state, action) {
      return {
        ...state,
        currentUser: action.payload,
      };
    },
    saveRoles(state, action) {
      return {
        ...state,
        roles: action.payload,
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
    changeNotifyCount(state, action) {
      return {
        ...state,
        currentUser: {
          ...state.currentUser,
          notifyCount: action.payload,
        },
      };
    },
  },
};
