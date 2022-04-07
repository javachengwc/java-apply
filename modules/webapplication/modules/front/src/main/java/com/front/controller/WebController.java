package com.front.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;

@Controller
@RequestMapping("web")
public class WebController {

    @ResponseBody
    @RequestMapping("getWebInfo")
    public JSONObject getWebInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        JSONObject json = new JSONObject();

        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //		request.getContextPath();/mll
        //		request.getRequestURI(); /mll/web/getWebInfo.do
        //		request.getRequestURL(); http://localhost:8081/mll/web/getWebInfo.do
        //		request.getServletPath(); /web/getWebInfo.do

        json.put("contextPath",request.getContextPath());
        json.put("requestURI",request.getRequestURI());
        json.put("requestURL",request.getRequestURL().toString());
        json.put("servletPath",request.getServletPath());

        //webapp的path F:\workproject\java-application\modules\webapp\modules\app-z7z8\src\main\webapp
        String url = session.getServletContext().getRealPath("/");
        json.put("webappRootPath",url);

        return json;
    }

    @RequestMapping("webappResDownload")
    @ResponseBody
    public String webappResDownload(HttpServletRequest request, HttpServletResponse response, HttpSession session,String resPath) {

        response.setCharacterEncoding("UTF-8");
        InputStream input = null;
        try {
            //获取文件的路径
            String url = session.getServletContext().getRealPath("/") + File.separator+resPath;
            System.out.println(url);
            File file = new File(url);
            input = FileUtils.openInputStream(file);
            byte[] data = IOUtils.toByteArray(input);
            String downLoadName=resPath.substring(resPath.lastIndexOf( File.separator)+1);
            System.out.println("downLoadName:"+downLoadName);

            //设置响应的报头信息(中文问题解决办法)
            downLoadName= new String(downLoadName.getBytes(), "iso-8859-1");
            response.setHeader("content-disposition", "attachment;fileName=" +downLoadName);
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("text/plain; charset=UTF-8");

            IOUtils.write(data, response.getOutputStream());
        } catch (Exception e) {
            if (input != null) {
                IOUtils.closeQuietly(input);
            }
        }
        return null;
    }

}
