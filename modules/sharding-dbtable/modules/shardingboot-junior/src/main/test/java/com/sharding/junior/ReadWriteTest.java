package com.sharding.junior;

import com.sharding.junior.dao.mapper.UserMapper;
import com.sharding.junior.model.entity.User;
import com.sharding.junior.model.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class ReadWriteTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserAdd() {
        Date now = new Date();
        User user = new User();
        user.setUserName("xxx");
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userMapper.insert(user);
    }
}
