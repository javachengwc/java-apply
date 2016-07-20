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
		// 设置模版的根目录,从目录加载模板
		cfg.setDirectoryForTemplateLoading(new File(templateRootPath));

        //加载器从Web应用目录开始加载模板 参数为Web应用的上下文和一个基路径，这个基路径是Web应用根路径（WEB-INF目录的上级目录）的相对路径。
        //setServletContextForTemplateLoading(Object servletContext, String path);

		//设置异常处理器(跳过错误的异常)
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		// 指定模版如何查看数据模型
		cfg.setObjectWrapper(new DefaultObjectWrapper());

        //设置所有模板可共享的变量
        //cfg.setSharedVariable("company", "cc Inc.");

        //从多个位置加载模板
        //FileTemplateLoader ftl1 = new FileTemplateLoader(new File("/tmp/templates"));
        //FileTemplateLoader ftl2 = new FileTemplateLoader(new File("/usr/data/templates"));
        //ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "");
        //TemplateLoader[] loaders = new TemplateLoader[] { ftl1, ftl2, ctl };
        //MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        //cfg.setTemplateLoader(mtl);

        //设置缓存策略
        //cfg.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20, soft:250");

        //设置缓存更新检测时间间隔,10秒
        //cfg.setSetting(Configuration.TEMPLATE_UPDATE_DELAY_KEY, "10");
    }
	
	public Template get(String name) throws IOException
	{
		return cfg.getTemplate(name);
	}
}
