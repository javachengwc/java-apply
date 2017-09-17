package com.app.controller;

import com.app.entity.UserActNote;
import com.app.service.UserActNoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user-act")
public class UserActController {

    private static Logger logger = LoggerFactory.getLogger(UserActController.class);

    @Autowired
    private UserActNoteService userActNoteService;

    @ResponseBody
    @RequestMapping("getActNote")
    public UserActNote getActNote(@RequestParam(value = "id",required = false) Integer id,
                                  @RequestParam(value = "name",required = false) String name) {
        logger.info("UserActController getActNote start,id={},name={}",id,name);
        UserActNote userActNote=null;
        if(id!=null) {
            userActNote=userActNoteService.getById(id);
        }else {
            userActNote=userActNoteService.queryByName(name);
        }
        return userActNote;
    }

    @ResponseBody
    @RequestMapping("uptActNote")
    public Boolean uptActNote(UserActNote userActNote) {
        logger.info("UserActController uptActNote start,userActNote={}",userActNote);
        userActNoteService.uptByName(userActNote);
        return Boolean.TRUE;
    }
}
