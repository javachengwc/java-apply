package com.micro.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.user.dao.ext.UserWalletDao;
import com.micro.user.dao.mapper.UserWalletMapper;
import com.micro.user.dao.plus.UserWalletPlusMapper;
import com.micro.user.model.pojo.UserWallet;
import com.micro.user.model.pojo.UserWalletExample;
import com.micro.user.model.req.UserWalletReq;
import com.micro.user.service.UserWalletService;
import org.dromara.hmily.annotation.Hmily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletPlusMapper, UserWallet> implements UserWalletService {

    private static Logger logger= LoggerFactory.getLogger(UserWalletServiceImpl.class);

    @Autowired
    private UserWalletDao userWalletDao;

    @Autowired
    private UserWalletMapper userWalletMapper;

    public UserWallet queryByUser(Long userId) {
        UserWalletExample example = new UserWalletExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserWallet> list = userWalletMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    //confirmDec,cancelDec方法定义必须与decreaseAmount一致，不然tcc事务会报错
    @Hmily(confirmMethod = "confirmDec", cancelMethod = "cancelDec")
    public boolean decreaseAmount(UserWalletReq userWalletReq) {
        Long userId = userWalletReq.getUserId();
        logger.info("UserWalletServiceImpl decreaseAmount start,userId={},userWalletReq={}",userId,userWalletReq);
        userWalletDao.decreaseAmount(userId,userWalletReq.getChangeAmount());
        UserWallet userWallet =this.queryByUser(userId);
        logger.info("UserWalletServiceImpl decreaseAmount 操作后用户钱包为,amount={},freezeAmount={}",
                userWallet.getAmount(),userWallet.getFreezeAmount());
        return true;
//      throw new RuntimeException();//模拟抛业务异常
//        try {
//            //模拟超时,当前线程暂停10秒
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public boolean confirmDec(UserWalletReq userWalletReq) {
        Long userId = userWalletReq.getUserId();
        logger.info("UserWalletServiceImpl confirmDec start,userId={},userWalletReq={}",userId,userWalletReq);
        userWalletDao.decLockAmount(userId,userWalletReq.getChangeAmount());
        UserWallet userWallet =this.queryByUser(userId);
        logger.info("UserWalletServiceImpl confirmDec 操作后用户钱包为,amount={},freezeAmount={}",
                userWallet.getAmount(),userWallet.getFreezeAmount());
        return true;
    }

    public boolean cancelDec(UserWalletReq userWalletReq) {
        Long userId = userWalletReq.getUserId();
        logger.info("UserWalletServiceImpl cancelDec start,userId={},userWalletReq={}",userId,userWalletReq);
        userWalletDao.backAmount(userId,userWalletReq.getChangeAmount());
        UserWallet userWallet =this.queryByUser(userId);
        logger.info("UserWalletServiceImpl cancelDec 操作后用户钱包为,amount={},freezeAmount={}",
                userWallet.getAmount(),userWallet.getFreezeAmount());
        return true;
    }
}
