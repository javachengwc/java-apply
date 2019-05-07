import React, { Component } from 'react';
import PropTypes from 'prop-types';

let authorization = {};
const getAuthorization = () => {
  try {
    authorization = JSON.parse(localStorage.getItem('myuser') || '{}').permissions || {};
  } catch (e) {}
};

getAuthorization();

//权限组件，根据登录获取的权限判断是否显示
export default class Auth extends Component {
  static propTypes = {
    auth: PropTypes.string.isRequired,
  };

  render() {
    if (!authorization) {
      throw new Error('Auth组件未初始化');
    }
    const { children, auth, forbidden = null, ...otherProps } = this.props;
    const permissionResolver = new Function('p', 's', `return ${auth.replace(/[^|&()]+/g, (a) => {
      return `p["${a.trim()}"]`;
    })}`);
    if (permissionResolver(authorization, auth)) {
      return React.Children.map(children, (child) => {
        return React.cloneElement(child, otherProps);
      });
    }
    return forbidden;
  }
}

Auth.getAuthorization = getAuthorization;
