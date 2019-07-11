package com.micro.user.service.impl;

import com.micro.user.model.pojo.User;
import com.micro.user.model.req.PwdReq;
import com.micro.user.service.UserAccountService;
import com.micro.user.service.UserService;
import com.micro.user.util.PasswdUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private static Logger logger= LoggerFactory.getLogger(UserAccountServiceImpl.class);

    @Autowired
    private UserService userService;

    //修改密码
    public boolean modifyPasswd(User user,PwdReq pwdReq) {
        logger.info("UserAccountServiceImpl modifyPasswd start,userId={},mobile={}",user.getId(),user.getMobile());
        String newPasswd = pwdReq.getPasswd();

        PasswdUtil.PasswdPair passwdPair = PasswdUtil.passwdEncrypt(newPasswd);
        User userUpt = new User();
        userUpt.setId(user.getId());
        userUpt.setPasswd(passwdPair.getPasswd());
        userUpt.setSalt(passwdPair.getSalt());
        userUpt.setModifiedTime(new Date());
        boolean rt = userService.updateById(userUpt);
        return rt;
    }

    //验证账号密码
    public boolean checkAccount(User user, String passwd) {
        String orglPasswd = user.getPasswd();
        String salt = user.getSalt();
        String inPwd = PasswdUtil.passwdEncrypt(passwd, salt);
        if (StringUtils.isBlank(orglPasswd) || !orglPasswd.equals(inPwd)) {
            return false;
        }
        return true;
    }
}
