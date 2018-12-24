import fetch from 'dva/fetch';
import get from 'lodash/get';
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import store from '../index';
import { downLoadFile } from './utils';
import * as config from './config';

const codeMessage = {
  400: '请求参数错误',
  401: '未登陆',
  403: '权限不足',
  404: '请求的地址不存在',
  500: '接口错误',
};

const convertObjectToQueryString = (obj = {}) => {
  const temp = [];
  Object.keys(obj).forEach(key => {
    if (obj[key]) {
      temp.push(`${key}=${obj[key]}`);
    }
  });

  return temp.join('&');
};

export function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }
  const errortext = codeMessage[response.status] || response.statusText;
  const error = new Error(errortext);
  error.name = response.status;
  error.response = response;
  throw error;
}
const getFileName = fileHeader => {
  const fileQuery = fileHeader.split(';')[1].trim();
  return decodeURIComponent(fileQuery.split('=')[1].replace(/"|'/g, ''));
};

/**
 * Requests a URL, returning a promise.
 *
 * @param  {string} url       The URL we want to request
 * @param  {object} [options] The options we want to pass to "fetch"
 * @return {object}           An object containing either "data" or "err"
 */
export default function request(link, options) {
  let url = `${config.REQUEST_PREFIX}${link}`;
  const defaultOptions = {
    credentials: 'include',
  };

  const newOptions = { ...defaultOptions, ...options };
  if (newOptions.method === 'POST' || newOptions.method === 'PUT') {
    newOptions.headers = {
      Accept: 'application/json',
      'Content-Type': 'application/json; charset=utf-8',
      ...newOptions.headers,
    };
    const header = {
      app: ' ',
      gps: ' ',
      os: ' ',
      ver: ' ',
      token: localStorage.getItem('token'),
    };
    if (newOptions.body) {
      newOptions.body = {
        data: newOptions.body,
        header,
      };
    } else {
      newOptions.body = {
        header,
      };
    }
    newOptions.body = JSON.stringify(newOptions.body);
  } else if (newOptions.method === 'GET') {
    newOptions.headers = {
      Accept: 'application/json',
      token: localStorage.getItem('token'),
      ...newOptions.headers,
    };

    url = `${url}?${convertObjectToQueryString(newOptions.body)}`;
    delete newOptions.body;
  }

  return fetch(url, newOptions)
    .then(checkStatus)
    .then(response => {
      if (response.headers.get('content-disposition')) {
        const filename = getFileName(response.headers.get('content-disposition'));
        const file = response.blob();
        file.then(f => {
          downLoadFile(filename, f);
        });
        return {};
      }
      if (
        newOptions.method === 'DELETE' ||
        response.status === 204 ||
        response.headers.get('content-length') === '0'
      ) {
        return response.text();
      }
      return response.json();
    })
    .catch(e => {
      let msg = e.message;
      if (msg === 'Failed to fetch') {
        msg = '未知错误';
      }
      message.error(msg);
      return e;
    });
}

export function catchError(request) {
  return request.then(data => {
    if (get(data, 'header.ret') === 'F') {
      message.error(get(data, 'header.msg[0]') || '未知错误');

      switch (+data.header.code) {
        // 登录过期
        case 1002:
        case 1003:
          localStorage.removeItem('token');
          store.dispatch(routerRedux.replace('/user/login'));
          break;
        default:
          break;
      }
    }
    return data;
  });
}
