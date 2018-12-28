package com.shop.book.manage.dao.manage;

import com.shop.book.manage.model.vo.UserQueryVo;
import com.shop.book.manage.model.vo.UserVo;

import java.util.List;

public interface UserDao {

    public List<UserVo> queryPage(UserQueryVo queryVo);

    public List<String> queryUserPerms(Long userId);

}
