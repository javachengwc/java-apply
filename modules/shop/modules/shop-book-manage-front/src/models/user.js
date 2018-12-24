import get from 'lodash/get';
import { message } from 'antd';
import {
  queryUsers,
  queryRoles,
  queryUserById,
  updateUser,
  addUser,
  disable,
  enable,
} from '../services/user';

export default {
  namespace: 'user',

  state: {
    currentUser: {}, // 当前对象的信息存储
    userList: {
      // 用户列表
      data: [],
      totalCount: 0,
    },
    roles: {
      // 全部角色
      data: [],
    },
    itemDetail: {}, // 用户详情
    getListParams: {
      // 查询参数
      pageNo: 1,
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
    *queryUsers(_, { put, call, select }) {
      const data = yield select(state => state.user.getListParams);
      const response = yield call(queryUsers, data);
      if (get(response, 'header.ret') === 'S') {
        yield put({
          type: 'saveUserList',
          payload: response,
        });
      }
    },
    // 查询角色
    *queryRoles(_, { put, call }) {
      const response = yield call(queryRoles);
      if (get(response, 'header.ret') === 'S') {
        yield put({
          type: 'saveRoles',
          payload: response,
        });
      }
    },
    // 查询单个用户详情
    *getOne({ data }, { put, call }) {
      const res = yield call(queryUserById, data);
      if (get(res, 'header.ret') === 'S') {
        yield put({
          type: 'itemDetail',
          data: res.data,
        });
        yield put({
          type: 'showForm',
          show: true,
        });
      }
    },
    // 更新用户
    *update({ data }, { put, call }) {
      const res = yield call(updateUser, data);
      if (get(res, 'header.ret') === 'S') {
        message.success('操作成功！');
        yield put({
          type: 'showForm',
          show: false,
        });
        yield put({
          type: 'queryUsers',
        });
      }
    },
    // 添加用户
    *add({ data }, { put, call }) {
      const response = yield call(addUser, data);
      if (get(response, 'header.ret') === 'S') {
        message.success('操作成功！');
        yield put({
          type: 'showForm',
          show: false,
        });
        yield put({
          type: 'queryUsers',
        });
      }
    },
    //禁用用户
    *disableUser({ data }, { put, call }) {
      const response = yield call(disable, data);
      if (get(response, 'header.ret') === 'S') {
        message.success('操作成功！');
        yield put({
          type: 'queryUsers',
        });
      }
    },
    //启用用户
    *enableUser({ data }, { put, call }) {
      const response = yield call(enable, data);
      if (get(response, 'header.ret') === 'S') {
        message.success('操作成功！');
        yield put({
          type: 'queryUsers',
        });
      }
    },
    *showAddForm(_, { put }) {
      yield put({
        type: 'queryRoles',
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
