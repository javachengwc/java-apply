package com.manage.rbac.service;

import com.manage.rbac.entity.User;
import com.manage.rbac.entity.ext.OrgPersonCountDO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.UserDTO;
import com.model.base.PageVo;

import java.util.List;

/**
 * User的服务接口
 */
public interface IUserService {

    //分页查询账号
    public PageVo<UserDTO> listUserPage(String user, Integer orgId, String orgName, Integer pageIndex, Integer pageSize);

    //查询账号详情
    public UserDTO getUserDetail(Integer id);

    //根据uid查询用户信息
    public UserDTO getUserByUid(Long uid);

    //根据手机号查询用户信息
    public UserDTO getUserByMobile(String mobile);

    //根据网名查询数量
    public long countByNickname(String nickname);

    //根据uid查询数量
    public long countByUid(Long uid);

    //增加账号
    public boolean addUser(UserDTO userDTO);

    //修改账号
    public boolean updateUser(UserDTO userDTO);

    //启用账号
    public boolean enableUser(Integer id, OperatorDTO operator);

    //禁用账号
    public boolean disableUser(Integer id, OperatorDTO operator);

    //删除账号
    public boolean deleteUser(Integer id, OperatorDTO operator);

    //根据手机号或网名查询账号
    public List<User> listUserByMobileOrNickname(String condition, Integer orgId);

    //查询多个机构人数
    public List<OrgPersonCountDO> listOrgPersonCount(List<Integer> orgIdList);

    //是否超级用户
    public boolean isSuperUser(Integer id);

    public List<User> listUserByNameOrNickName(String name);

    public List<User> listUserByIds(List<Integer> ids);

    //是否机构管理员
    public boolean isOrgAdmin(Integer id);
}
