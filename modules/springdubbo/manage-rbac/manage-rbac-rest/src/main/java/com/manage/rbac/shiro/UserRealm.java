package com.manage.rbac.shiro;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.model.enums.EnableEnum;
import com.model.base.Resp;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

//获取用户的角色和权限信息
public class UserRealm extends AuthorizingRealm {

    private static Logger logger= LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private DubboFactory dubboFactory;

    //AuthorizationInfo代表了角色的权限信息集合
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDTO user = (UserDTO) principalCollection.getPrimaryPrincipal();
        Set<String> perms = new HashSet<String>();
        //userProvider.queryUserPerms(user.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(perms);
        return info;
    }

    //提供账户信息返回认证信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("UserRealm doGetAuthenticationInfo start,authenticationToken={}",authenticationToken);
        String mobile = (String) authenticationToken.getPrincipal();
        Resp<UserDTO> rt= dubboFactory.getUserProvider().getUserByMobile(mobile);
        UserDTO user =rt.getData();
        if (user == null) {
            throw new UnknownAccountException("账号不正确");
        }

        String passwd = new String((char[]) authenticationToken.getCredentials());
        logger.info("UserRealm doGetAuthenticationInfo mobile={},passwd={}",mobile,passwd);
        if ( user.getState()== EnableEnum.FORBID.getValue() ) {
            throw new LockedAccountException("账号被禁用");
        }
        if ( user.getDisable()!=null && user.getDisable()== 1 ) {
            throw new LockedAccountException("账号已删除");
        }
        return new SimpleAuthenticationInfo(user, passwd, getName());
    }
}
