package com.database.tool.controller;

import com.database.tool.service.DbService;
import com.database.tool.vo.Node;
import com.database.tool.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DbController {

    @Autowired
    private DbService dbService;

    @RequestMapping("/view")
    public String view() {
        return "/view";
    }

    @ResponseBody
    @RequestMapping("/tables")
    public List<?> data() {
        return dbService.findAllNodes();
    }

    @RequestMapping("/query")
    @ResponseBody
    public Result query(String sql) {
        return dbService.query(sql);
    }

    @RequestMapping("/getColumns")
    @ResponseBody
    public List<?> getColumns(String tableName) {
        return dbService.getColumns(tableName);
    }

    @RequestMapping("/refresh")
    @ResponseBody
    public List<?> refresh(Node node) {
        return dbService.refresh(node);
    }
}
