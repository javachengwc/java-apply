package com.ftl.util;

import com.util.CharsetUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * 基于freemarker的html生成器
 */
public class HtmlTemplateGenerator
{

    private static Logger logger = LoggerFactory.getLogger(HtmlTemplateGenerator.class);
	/**
	 * 生成静态文件
	 * @param ftlTemplate ftl模版文件
	 * @param contents ftl要用到的动态内容
	 * @param savePath 文件保存路径
	 * @param saveFilename 保存文件名
	 */
	public static void generate(String ftlTemplate, Map contents,String savePath, String saveFilename) throws Exception
	{
		// 获取或创建一个模版
		Template temp = FtlManager.getInstance().get(ftlTemplate);
		// 检查保存路径是否存在
		File file = new File(savePath);
		if( !file.exists())
			file.mkdirs();
		// 合并数据模型和模版
		Writer out = new OutputStreamWriter(new FileOutputStream(savePath + "/"
			+ saveFilename), CharsetUtil.UTF8);
		temp.process(contents, out);
		out.flush();
	}
	
	/**
	 * 生成静态文件
	 * @param ftlTemplate ftl模版文件
	 * @param contents ftl要用到的动态内容
	 * @param savePath 文件保存路径
	 */
	public static void generate(String ftlTemplate, Map contents,String savePath) throws Exception
	{
		// 获取或创建一个模版
		Template temp = FtlManager.getInstance().get(ftlTemplate);
		// 检查保存路径是否存在
		File file = new File(savePath);
		if( !file.exists())
			file.createNewFile();
		// 合并数据模型和模版
		Writer out = new OutputStreamWriter(new FileOutputStream(savePath),CharsetUtil.UTF8);
		temp.process(contents, out);
		out.flush();
	}
	
	/**
	 * 返回reponse信息
	 */
	public static void reponseInfo(String ftlTemplate, Map contents, Writer w) throws Exception
	{
		// 获取或创建一个模版
		Template temp = FtlManager.getInstance().get(ftlTemplate);
		temp.process(contents, w);
		w.flush();
	}

    /**
     * 传入模版,生成html内容
     */
    public static String build(String ftlTemplate, Map<String, Object> params) {
        Configuration cfg = new Configuration();
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        String tmpId = System.currentTimeMillis() + "";
        templateLoader.putTemplate(tmpId, ftlTemplate);
        cfg.setTemplateLoader(templateLoader);
        String content = "";
        try {
            Template template = cfg.getTemplate(tmpId);
            StringWriter writer = new StringWriter();
            template.process(params, writer);
            content = writer.toString();
        } catch (IOException e) {
            logger.error("通过模板文件生成html内容出错", e);
        } catch (TemplateException e) {
            logger.error("模板文件出错", e);
        }
        return content;
    }

    /**
     * 从文件读取为字符串模板
     *
     * @param fileName
     * @return
     */
    public static String readTemplate(String fileName) {
        FileReader fr = null;
        BufferedReader br = null;
        String line = null;
        StringBuffer sb = new StringBuffer();
        InputStreamReader in=null;
        try {
            in = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), "UTF-8");
            br = new BufferedReader(in);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            logger.error("读取ftl模板文件出错", e);
            return null;
        }finally
        {
            try {
                if (in != null) {
                    in.close();
                }
            }catch(Exception ee)
            {
                logger.error("inputsteam close error,", ee);
            }
        }
    }
}