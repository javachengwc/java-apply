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
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.imageio.ImageIO;

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


    @RequestMapping("/showPicWithPath")
    public void showPic(HttpServletResponse response,String path) throws IOException {

        logger.info("FileController showPicWithPath start,path={}",path);
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

    /**
     * @RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
     * 上传文件
     */
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

                //String path="E:/"+new Date().getTime()+file.getOriginalFilename();
                //File newFile=new File(path);
                //通过CommonsMultipartFile的方法直接写文件
                //file.transferTo(newFile);
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

    @RequestMapping(value = "/uploadPicHandle")
    public void uploadPicHandle(HttpServletRequest request,HttpServletResponse response) throws Exception{

        logger.info("FileController uploadPicHandle start");
        //ServletContext context = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        //在web.xml中有 org.springframework.web.context.request.RequestContextListener  监听器才能这样获取request
        //这样获取request在转换(MultipartHttpServletRequest)request的时候会报类型转换错误
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String,Object> map = uploadSinglePic(request);
        logger.debug("FileController upload pic ,result:" + map);
        if(null == map ||map.size()<=0) {
            JSONObject error = new JSONObject();
            error.put("result", 1);
            error.put("msg", "图片上传,处理失败");
            HttpRenderUtil.renderJSON(error.toString(), response);
            return;
        } else if(map.containsKey("result")) {
            JSONObject info = new JSONObject();
            for(Map.Entry<String,Object> entry:map.entrySet()) {
                info.put(entry.getKey(),entry.getValue());
            }
            HttpRenderUtil.renderJSON(info.toString(), response);
        }
    }

    public static  Map<String,Object>  uploadSinglePic(HttpServletRequest request) throws Exception{
        Map<String,Object> rtMap = new HashMap<String,Object>();
        try {
            //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver( request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if(multipartResolver.isMultipart(request))
            {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
                //获取multiRequest 中所有的文件名
                Iterator iter=multiRequest.getFileNames();
                while(iter.hasNext())
                {
                    //一次遍历所有文件
                    MultipartFile file=multiRequest.getFile(iter.next().toString());
                    String fileName =file.getOriginalFilename();
                    long fileSize =file.getSize();
                    String contentType=file.getContentType();
                    String fileExtName =fileName.substring(fileName.lastIndexOf(".") + 1);
                    logger.info("FileController uploadSinglePic file name={},size={},contentType={}",fileName,""+fileSize);
                    if(file!=null)
                    {
                        InputStream input = file.getInputStream();
                        BufferedImage bis = ImageIO.read(input);
                        int w = bis.getWidth();
                        int h = bis.getHeight();
                        if(file.getSize()>1024*1024*10){
                            rtMap.put("msg", "图片尺寸过大");
                            rtMap.put("result", 3);
                            return rtMap;
                        }
                        String path = AttachManager.getInstance().saveImage(bis,fileName,fileExtName);
                        rtMap.put("path", path);
                        rtMap.put("result", 0);
                    }
                }
            }
        } catch(Exception e) {
            logger.error("FileController uploadSinglePic error,",e);
        }
        return rtMap;
    }


}
