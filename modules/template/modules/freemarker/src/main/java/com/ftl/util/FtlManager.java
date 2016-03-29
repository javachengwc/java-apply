package com.ftl.util;

import com.util.CharsetUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FtlManager
{
	private static FtlManager m_ins = new FtlManager();

    private Configuration cfg;

	private Map<String, Template> m_temps = new HashMap<String, Template>();
	
	public static FtlManager getInstance()
	{
		return m_ins;
	}
	
	public void init(String templateRootPath) throws Exception
	{
		// 创建一个合适的configuration
		cfg = new Configuration();
		// 设置编码
		cfg.setDefaultEncoding(CharsetUtil.UTF8);
		// 设置模版的根目录
		cfg.setDirectoryForTemplateLoading(new File(templateRootPath));
		//设置异常处理器(跳过错误的异常)
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		// 指定模版如何查看数据模型
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}
	
	public Template get(String name) throws IOException
	{
		return cfg.getTemplate(name);
	}
}
