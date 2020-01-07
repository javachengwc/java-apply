package com.shopstat.controller.aftersale;

import com.alibaba.fastjson.JSONObject;
import com.excel.JxlUtil;
import com.shopstat.controller.BaseController;
import com.shopstat.model.pojo.StatSafeguard;
import com.shopstat.model.vo.StatQueryVo;
import com.shopstat.service.aftersale.SafeguardStatService;
import com.excel.ExcelUtil;
import com.util.web.HttpRenderUtil;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 维权统计controller类
 */
@Controller
@RequestMapping(value = "/shopstat/aftersale")
public class SafeguardStatController extends BaseController {

    private static Logger logger= LoggerFactory.getLogger(SafeguardStatController.class);

    private static final String PREFIX = "/shopstat/aftersale/";

    @Autowired
    private SafeguardStatService safeguardStatService;

    //维权统计页面
    @RequestMapping(value = "/safeguardStat")
    public String safeguardStat()
    {
        return PREFIX + "safeguardStat";
    }

    //维权统计列表
    @RequestMapping(value = "/safeguardStatList")
    public void safeguardStatList(HttpServletResponse response,StatQueryVo queryVo) {

        queryVo.genDef();
        queryVo.genPage();

        List<StatSafeguard> list = safeguardStatService.queryPage(queryVo);
        int count = safeguardStatService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }


    private void makeExcel(List<Map> mapList, String[] fidleNameArray, String[] kayArray,HttpServletResponse response) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String filename = new String(( "xxx.xls").getBytes(), "iso-8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.setContentType("application/x-download");
            ExcelUtil.download(fidleNameArray, kayArray, mapList, outputStream);
        } catch (Exception e) {
            logger.info("makeExcel:", e);
        }
    }

    private void makeExcel2(List<String[]> data , String[] fidleNameArray, HttpServletResponse response) {
        try {
            OutputStream outputStream = response.getOutputStream(); // 取得输出流
            String filename = new String("xxx.xls".getBytes(), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" +filename ); // 设定输出文件头
            response.setContentType("application/msexcel"); // 定义输出类型
            JxlUtil.makeExcelWorkBook(outputStream, filename, fidleNameArray, data);
        } catch (Exception e) {
            logger.info("makeExcel:", e);
        }
    }

    private void makeCsv(List<Map> mapList,String[] fields, String[] keys, HttpServletResponse response) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String filename = new String("xxx.csv".getBytes(), "iso-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setContentType("application/x-download");
            ExcelUtil.downloadCsv(fields, keys, mapList, outputStream);
        } catch (Exception e) {
            logger.error("download error: ", e);
        }
    }
}