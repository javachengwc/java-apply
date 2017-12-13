package com.app.controller;

import com.app.entity.UserActNote;
import com.app.service.UserActNoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user-act")
@Api(value = "UserActController相关接口")
public class UserActController {

    private static Logger logger = LoggerFactory.getLogger(UserActController.class);

    @Autowired
    private UserActNoteService userActNoteService;

    @ResponseBody
    @RequestMapping(value="/getActNote",method = RequestMethod.GET)
    @ApiOperation(value = "getActNote", notes = "getActNote")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名字", required = false, dataType = "String", paramType = "query")
    })
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
    @RequestMapping(value="/uptActNote",method = RequestMethod.POST)
    @ApiOperation(value = "uptActNote", notes = "uptActNote")
    public Boolean uptActNote(UserActNote userActNote) {
        logger.info("UserActController uptActNote start,userActNote={}",userActNote);
        userActNoteService.uptByName(userActNote);
        return Boolean.TRUE;
    }
}
