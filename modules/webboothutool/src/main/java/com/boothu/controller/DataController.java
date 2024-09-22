package com.boothu.controller;

import com.boothu.model.vo.DataVo;
import com.boothu.service.DataService;
import com.model.base.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 新增数据
     *
     * @param file excel文件
     * @return
     */
    @PostMapping("/addData")
    public Resp<List<DataVo>> addData(@RequestParam(value = "file") MultipartFile file) {
        List<DataVo> list = dataService.addData(file);
        return Resp.data(list==null? Collections.EMPTY_LIST: list);
    }

}
