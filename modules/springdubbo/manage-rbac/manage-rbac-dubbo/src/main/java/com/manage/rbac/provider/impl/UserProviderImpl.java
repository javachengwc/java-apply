package com.manage.rbac.provider.impl;

import com.manage.rbac.service.IUserService;
import com.model.base.PageVo;
import com.model.base.Resp;
import com.manage.rbac.model.common.Constant;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.provider.IUserProvider;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.entity.User;

import com.util.TransUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * User相关dubbo服务实现类.
 */
@Service(version = Constant.DUBBO_API_VERSION)
public class UserProviderImpl implements IUserProvider {

    @Autowired
    private IUserService service;

    //分页查询账号
    @Override
    public Resp<PageVo<UserDTO>> listUserPage(String user, Integer orgId, String orgName, Integer pageIndex, Integer pageSize) {
        PageVo<UserDTO> pageData = service.listUserPage(user, orgId, orgName, pageIndex, pageSize);
        return Resp.data(pageData);
    }

    //根据uid查询用户信息
    @Override
    public Resp<UserDTO> getUserByUid(Long uid) {
        UserDTO userDTO = service.getUserByUid(uid);
        return Resp.data(userDTO);
    }

    //根据手机号查询用户信息
    @Override
    public Resp<UserDTO> getUserByMobile(String mobile) {
        UserDTO userDTO = service.getUserByMobile(mobile);
        return Resp.data(userDTO);
    }

    //查询账号详情
    @Override
    public Resp<UserDTO> getUserDetail(Integer id) {
        UserDTO userDTO = service.getUserDetail(id);
        return Resp.data(userDTO);
    }

    //根据网名查询数量
    @Override
    public Resp<Long> countByNickname(String nickname) {
        long count = service.countByNickname(nickname);
        return Resp.data(count);
    }

    //根据uid查询数量
    @Override
    public Resp<Long> countByUid(Long uid) {
        long count = service.countByUid(uid);
        return Resp.data(count);
    }

    //增加账号
    @Override
    public Resp<Void> addUser(UserDTO userDTO) {
        service.addUser(userDTO);
        return Resp.success();
    }

    //修改账号
    @Override
    public Resp<Void> updateUser(UserDTO userDTO) {
        service.updateUser(userDTO);
        return Resp.success();
    }

    //启用账号
    @Override
    public Resp<Void> enableUser(Integer id, OperatorDTO operator) {
        service.enableUser(id, operator);
        return Resp.success();
    }

    //禁用账号
    @Override
    public Resp<Void> disableUser(Integer id, OperatorDTO operator) {
        service.disableUser(id, operator);
        return Resp.success();
    }

    //删除账号
    @Override
    public Resp<Void> deleteUser(Integer id, OperatorDTO operator) {
        service.deleteUser(id, operator);
        return Resp.success();
    }

    //是否超级用户
    @Override
    public Resp<Boolean> isSuperUser(Integer id) {
        boolean isSuper = service.isSuperUser(id);
        return Resp.data(isSuper);
    }

    public Resp<List<UserDTO>> listUserByNameOrNickName(String name) {
        if(StringUtils.isBlank(name)) {
            return Resp.data(Collections.emptyList());
        }
        List<User> list = service.listUserByNameOrNickName(name);
        List<UserDTO> rtList = TransUtil.transList(list, UserDTO.class);
        return Resp.data(rtList);
    }

    @Override
    public Resp<List<UserDTO>> listUserByIds(List<Integer> ids) {
        List<User> list = service.listUserByIds(ids);
        List<UserDTO> rtList = TransUtil.transList(list, UserDTO.class);
        return Resp.data(rtList);
    }

    @Override
    public Resp<UserDTO> getById(Integer id) {
        UserDTO userDTO = service.getUserDetail(id);
        return Resp.data(userDTO);
    }

    @Override
    public Resp<List<UserDTO>> listUserByMobileOrNickname(String condition,Integer orgId) {
        List<User> list = service.listUserByMobileOrNickname(condition, orgId);
        List<UserDTO> rtList = TransUtil.transList(list, UserDTO.class);
        return Resp.data(rtList);
    }
}