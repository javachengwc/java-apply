import get from 'lodash/get';
import { message } from 'antd';

import { reqSuccess } from '../utils/utils';
import { queryAllRole } from '../services/role';

export default {
  namespace: 'role',

  state: {
    roles: {
      // 全部角色
      data: [],
    }
  },

  effects: {
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
  },

  reducers: {
    saveRoles(state, action) {
      return {
        ...state,
        roles: action.payload,
      };
    },
  },
};
