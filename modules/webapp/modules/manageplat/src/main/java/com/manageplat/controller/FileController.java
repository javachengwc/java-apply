package com.manageplat.controller;

import com.manageplat.model.pojo.FileRecord;
import com.manageplat.service.AttachManager;
import com.manageplat.service.FileService;
import com.util.web.HttpRenderUtil;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件处理入口
 * COS是Java HTTP文件上传组件，简单实用
 */
@Controller
@RequestMapping(value = "/file")
public class FileController {

    public static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;


    /**分页查询**/
    @RequestMapping(value = "/queryPage")
    public void queryPage(HttpServletResponse response,Integer type,Integer pageNo,Integer pageSize)
    {

        if(pageNo==null)
        {
            pageNo=1;
        }
        if(pageSize==null)
        {
            pageSize=20;
        }
        int start = (pageNo-1)*pageSize;
        if(start<0)
        {
            start=0;
        }
        int count = fileService.count(type);
        List<FileRecord> list = fileService.queryPage(type,start,pageSize);

        JSONObject map = new JSONObject();
        map.put("error", 0);
        map.put("count", count);
        map.put("list", list);
        map.put("now", System.currentTimeMillis());

        HttpRenderUtil.renderJSON(map.toString(), response);
    }

    @RequestMapping("/showPic")
    public void showPic(HttpServletResponse response,Integer id) throws IOException {

        System.out.println("FileController showPic start");
        FileRecord fileRecord =fileService.get(id);
        String path=null;
        if(fileRecord!=null) {
            path=fileRecord.getPath();
        }
        if(StringUtils.isBlank(path))
        {
            return;
        }

        File file  =new File(path);
        if(!file.exists())
        {
            return ;
        }
        output(file,response);
    }

    public void output( File file,HttpServletResponse response)
    {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ServletOutputStream sos = null;
        FileInputStream ios= null;

        try {
            ios=new FileInputStream(file);
            sos = response.getOutputStream();
            IOUtils.copy(ios, sos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(ios!=null)
                {
                    ios.close();
                }
                sos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**添加**/
    @RequestMapping(value = "/addFile")
    public void addFile(HttpServletResponse response,@RequestParam(value = "file", required = false) MultipartFile file,
                        String name,String linkAddress,Integer type )
    {

        logger.info("FileController addFile start");
        JSONObject map = new JSONObject();
        String path=null;
        //文件处理
        try
        {
            if(file==null)
            {
                logger.error("FileController addFile  file is null");
                map.put("error", 1);
                map.put("msg", "文件为空");
                map.put("now", System.currentTimeMillis());
                HttpRenderUtil.renderJSON(map.toString(), response);
                return;
            }else
            {
                String filename = file.getOriginalFilename();
                logger.error("FileController addFile filename="+filename);
                String defaultSuffix=".jpg";
                InputStream input = file.getInputStream();
                path = AttachManager.getInstance().saveFile(input,filename,defaultSuffix);
            }
        }catch(Exception e)
        {
            logger.error("FileController addFile deal file error",e);
            map.put("error", 1);
            map.put("msg", "文件处理失败");
            map.put("now", System.currentTimeMillis());
            HttpRenderUtil.renderJSON(map.toString(), response);
            return;
        }
        try
        {
            FileRecord fileRecord = new FileRecord();
            fileRecord.setName(name);
            fileRecord.setLink(linkAddress);
            fileRecord.setCreateTime(new Date());
            if(type!=null) {
                fileRecord.setType(type);
            }

            fileRecord.setPath(path);

            fileService.addFile(fileRecord);

            map.put("error", 0);  //标示成功
            map.put("now", System.currentTimeMillis());
            HttpRenderUtil.renderJSON(map.toString(), response);
        }catch(Exception e)
        {
            map.put("error", 1);
            map.put("msg", "程序应用异常");
            map.put("now", System.currentTimeMillis());
            HttpRenderUtil.renderJSON(map.toString(), response);
        }
    }

    @RequestMapping(value = "/delFile")
    public void delFile(HttpServletResponse response,Integer id)
    {
        FileRecord fileRecord =fileService.get(id);
        if(fileRecord!=null) {

            if(!StringUtils.isBlank(fileRecord.getPath())) {
                AttachManager.getInstance().delFiles(fileRecord.getPath());
            }
            fileService.delete(id);
        }
        JSONObject map = new JSONObject();
        map.put("error", 0);
        map.put("now", System.currentTimeMillis());
        HttpRenderUtil.renderJSON(map.toString(), response);
    }

}
