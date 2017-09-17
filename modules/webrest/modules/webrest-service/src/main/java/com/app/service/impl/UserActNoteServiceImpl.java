package com.app.service.impl;

import com.app.dao.tkmapper.UserActNoteDao;
import com.app.entity.UserActNote;
import com.app.server.config.Config;
import com.app.service.UserActNoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserActNoteServiceImpl implements UserActNoteService {

    private static Logger logger = LoggerFactory.getLogger(UserActNoteServiceImpl.class);

    @Autowired
    private UserActNoteDao userActNoteDao;

    public UserActNote getById(Integer id) {
        logger.info("UserActNoteServiceImpl getById start ,id={}",id);
        return userActNoteDao.selectByPrimaryKey(id);
    }

    public UserActNote queryByName(String name) {
        logger.info("UserActNoteServiceImpl queryByName start ,name={}",name);
        return userActNoteDao.queryByName(name);
    }

    @Transactional(Config.TRANSACTION_USER)
    public void uptByName(UserActNote userActNote) {
        logger.info("UserActNoteServiceImpl uptByName start ,userActNote={}",userActNote);
        userActNoteDao.uptByName(userActNote);
    }
}
