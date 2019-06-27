package com.bootmp.controller.code;

import com.bootmp.generator.action.config.WebGeneratorConfig;
import com.bootmp.generator.action.model.GenQo;
import com.bootmp.model.req.GenReq;
import com.bootmp.service.code.TableService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Api("生成代码接口")
@RestController
@RequestMapping("/code")
public class CodeController {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @ApiOperation("自动生成代码接口")
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ResponseBody
    public Resp<Void> generate(@RequestBody Req<GenReq> req ) {

        GenReq genReq = req.getData();
        genReq.setUrl(dbUrl);
        genReq.setUserName(dbUserName);
        genReq.setPassword(dbPassword);
        GenQo genQo = new GenQo();
        BeanUtils.copyProperties(genReq, genQo);
        WebGeneratorConfig webGeneratorConfig = new WebGeneratorConfig(genQo);
        webGeneratorConfig.doMpGeneration();
        webGeneratorConfig.doGunsGeneration();
        return Resp.success(null);
    }
//    样例参
//    {
//        "data": {
//            "author": "cwc",
//            "daoSwitch": true,
//            "entitySwitch": true,
//            "ignoreTabelPrefix": "",
//            "moduleName": "tmodule",
//            "projectPackage": "com.test",
//            "projectPath": "E:\\\\tmp",
//            "serviceSwitch": true,
//            "tableName": "user"
//        }
//    }
}
