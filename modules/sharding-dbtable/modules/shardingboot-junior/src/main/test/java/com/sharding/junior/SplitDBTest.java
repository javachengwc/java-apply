package com.sharding.junior;

import com.sharding.junior.model.entity.Order;
import com.sharding.junior.model.entity.User;
import com.sharding.junior.model.vo.OrderVo;
import com.sharding.junior.model.vo.UserVo;
import com.sharding.junior.service.OrderService;
import com.sharding.junior.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SplitDBTest {

    private static Logger logger = LoggerFactory.getLogger(SplitDBTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Test
    public void testAddUserAndOrder() {
        UserVo userVo = new UserVo();
        userVo.setUserName("ccc");
        User user =userService.addUser(userVo);
        logger.info("user={}",user);

        OrderVo orderVo = new OrderVo();
        orderVo.setUserId(user.getId());
        orderVo.setUserName(user.getUserName());
        orderVo.setShopId(1L);
        orderVo.setShopName("官方");
        Order order=orderService.createOrder(orderVo);
        logger.info("order={}",order);
    }
}
