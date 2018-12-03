package com.shop.book.manage.service.rdbc;

import com.shop.book.manage.model.pojo.User;
import com.shop.book.manage.model.vo.UserQueryVo;
import com.shop.book.manage.model.vo.UserVo;

import java.util.List;
import java.util.Set;

public interface UserService {

    public User getById(Long userId);

    public User queryByMobile(String mobile);

    public User addUser(User user);

    public Integer uptUser(User user);

    //禁用
    public boolean disable(Long userId);

    //启用
    public boolean enable(Long userId);

    public List<UserVo> queryPage(UserQueryVo queryVo);

    //查询用户权限
    public Set<String> queryUserPerms(Long userId);
}
