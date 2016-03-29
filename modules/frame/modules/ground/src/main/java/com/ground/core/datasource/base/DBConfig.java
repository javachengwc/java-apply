package com.ground.core.datasource.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ground.core.datasource.DBServerInfo;
import com.ground.entity.RegistEntity;
import com.ground.exception.GroundException;
import com.ground.util.XmlUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;


public class DBConfig
{
	private static Logger m_logger = Logger.getLogger(DBConfig.class);
	
	private  static Element root = null;

	public static List<DBServerInfo> mutiServerInfo = new ArrayList<DBServerInfo>();
	
	/**
	 * 读取数据库配置文件 cherroot.xml文件
	 * 
	 * @param filePath  cherroot.xml文件路径
	 * @throws Exception
	 */
	public static void loadConfig(String filePath) throws Exception
	{
    	Document doc =null;
        try{

            doc=XmlUtil.parse(new File(filePath));
            root = doc.getRootElement();

        }catch(Exception e)
        {
            m_logger.error("DBConfig loadConfig error,filePath="+filePath,e);
            return;
        }
        if(root!=null)
        {
            scanPackage();
            loadDBInfo();
        }else
        {
            m_logger.error("DBConfig loadConfig parse root is null,filePath="+filePath);
        }
	}
	
	/**
	 *  扫描数据对应的对模型类
	 *  初使化entity类的sql信息
	 */
	private static void scanPackage(){
		Element element = (Element)root.selectSingleNode("//ground/scanPackage");
		String scanPackage = element.getText();
		RegistEntity.registPakageEntity(scanPackage);
	}
	
	/**
	 * 加载数据库节点的相关信息
	 */
	private static void loadDBInfo() throws Exception{
		 Element cherroot = (Element)root.selectSingleNode("//ground/datasource-mapping");
		 List<?> datasource = cherroot.elements(); // 得到datasource元素下的子元素集合
		 if(datasource == null){
			 throw new GroundException("Ground DBConfig loadDBInfo error, datasource is null!");
		 }
		 if(datasource.size()>1){
			 m_logger.warn("Ground DBConfig loadDBInfo datasource size gt one is not select halb-proxy! so cherroot select first datasource!");
		 }
	    // 循环遍历集合中的每一个元素 
	    for (Object obj : datasource) 
	    {
	    	Element element = (Element) obj;
	    	Node index =element.selectSingleNode("index");
	    	if(index == null){
	    		throw new Exception("DBConfig loadDBInfo index is null!");
	    	}
	    	int indexText = Integer.valueOf(index.getText().trim());
	    	
	    	Node url =element.selectSingleNode("url");
	    	if(url == null){
	    		throw new Exception("DBConfig loadDBInfo url is null!");
	    	}
	    	String urlText = url.getText().trim();
	    	
	    	Node user =element.selectSingleNode("user");
	    	if(user == null){
	    		throw new Exception("DBConfig loadDBInfo user is null!");
	    	}
	    	String userText = user.getText().trim();
	    	
	    	Node passwd =element.selectSingleNode("password");
	    	if(passwd == null){
	    		throw new Exception("DBConfig loadDBInfo password is null!");
	    	}
	    	String passwdText = passwd.getText().trim();
	    	
	    	Node maxActive =element.selectSingleNode("maxActive");
	    	if(maxActive == null){
	    		throw new Exception("DBConfig loadDBInfo maxActive is null!");
	    	}
	    	int maxActiveText = Integer.valueOf(maxActive.getText().trim());
	    	
	    	Node minIdle =element.selectSingleNode("minIdle");
	    	if(minIdle == null){
	    		throw new Exception("DBConfig loadDBInfo minIdle is null!");
	    	}
	    	int minIdleText = Integer.valueOf(minIdle.getText().trim());
	    	
	    	Node maxIdle =element.selectSingleNode("maxIdle");
	    	if(maxIdle == null){
	    		throw new Exception("DBConfig loadDBInfo maxIdle is null!");
	    	}
	    	int maxIdleText = Integer.valueOf(maxIdle.getText().trim());
	    	DBServerInfo dbServerInfo = new DBServerInfo(indexText, urlText, userText, passwdText, maxActiveText, minIdleText,maxIdleText);
	    	mutiServerInfo.add(dbServerInfo);
	    }	
	}
}
