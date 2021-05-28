package com.manage.rbac.provider;

import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.UserDTO;
import com.model.base.PageVo;
import com.model.base.Resp;

import java.util.List;

public interface IUserProvider {

    //分页查询账号
    public Resp<PageVo<UserDTO>> listUserPage(String user, Integer orgId, String orgName, Integer pageIndex, Integer pageSize);

    //查询账号详情
    public Resp<UserDTO> getUserDetail(Integer id);

    //根据uid查询用户信息
    public Resp<UserDTO> getUserByUid(Long id);

    //根据手机号查询用户信息
    public Resp<UserDTO> getUserByMobile(String mobile);

    //根据uid查询数量
    public Resp<Long> countByUid(Long uid);

    //增加账号
    public Resp<Void> addUser(UserDTO userDTO);

    //修改账号
    public Resp<Void> updateUser(UserDTO userDTO);

    //启用账号
    public Resp<Void> enableUser(Integer id, OperatorDTO operator);

    //禁用账号
    public Resp<Void> disableUser(Integer id,OperatorDTO operator);

    //删除账号
    public Resp<Void> deleteUser(Integer id,OperatorDTO operator);

    //是否超级用户
    public Resp<Boolean> isSuperUser(Integer id);

    //根据id列表查询用户信息
    public Resp<List<UserDTO>> listUserByIds(List<Integer> ids);

    //根据网名查询数量
    public Resp<Long> countByNickname(String nickname);

    public Resp<UserDTO> getById(Integer id);

    //根据手机号或网名查询账号
    public Resp<List<UserDTO>> listUserByMobileOrNickname(String condition,Integer orgId);
}