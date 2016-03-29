package com.manageplat.service;

import com.util.BlankUtil;
import com.util.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 权限管理
 */
public class AuthManager {

    private static Logger logger = LoggerFactory.getLogger(AuthManager.class);

    //所有页面功能  map中key是主菜单_子菜单的组合 value是菜单下的功能数组
    private static List<Map<String, String[]>> pageMenuFunctions = new LinkedList<Map<String, String[]>>();

    //所有公共功能
    private static Set<String> commonFunctions = new HashSet<String>();

    //分配菜单 外围map key是用户 value是菜单列表  内部map key是主菜单 value是子菜单，只支持到2级菜单，切内部每个map的key只有一个
    private static Map<String,List<Map<String, List<String>>>> assgnMenus = new HashMap<String,List<Map<String, List<String>>>>();

    //分配功能 key是用户 value是 用户所拥有的所有功能
    private static Map<String, Set<String>> assgnFunctions =  new HashMap<String, Set<String>>();

    //用户菜单权限配置
    private final static String authPath="user-auth.properties";

    //菜单功能权限配置
    private final static String authFunc="auth-function.xml";

    private Properties authProp=null;

    private static AuthManager authManager = new AuthManager();

    public static AuthManager getInstance()
    {
        return authManager;
    }

    private AuthManager()
    {
        init();
    }

    public  void init()
    {
        authProp= PropertiesLoader.load(authPath);

        try {
            Properties configuration = new Properties();
            configuration.loadFromXML(Thread.currentThread().getContextClassLoader().getResourceAsStream(authFunc));

            String functions = configuration.getProperty("menuFunctions").trim();

            //各行菜单功能组成的数组
            String[] t_funs = functions.split("\n");

            for (String u : t_funs) {
                if (StringUtils.isBlank(u)) {
                    continue;
                }

                String[] tmp = u.split(":");
                if(tmp.length<=1)
                {
                    continue;
                }

                String menu_key = tmp[0].trim();//menu_key表示菜单

                String[] functionList = tmp[1].trim().split(",");//functionList表示功能列表

                Map<String, String[]> funMap = new HashMap<String, String[]>();

                funMap.put(menu_key, functionList);

                pageMenuFunctions.add(funMap);

                logger.info("init manager menu: " + menu_key + "   functionList: " + tmp[1]);
            }


            String commonFuns = configuration.getProperty("commonFunctions");
            if(!StringUtils.isBlank(commonFuns) &&  StringUtils.isBlank(commonFuns.trim()))
            {
                commonFuns=commonFuns.trim();
                String[] common_funs = commonFuns.split(",");
                for (String u : common_funs) {
                    commonFunctions.add(u);
                }
            }

        }catch(Exception e)
        {
            logger.error("-------AuthManager init loadFromXml "+authFunc+" error,",e);
        }

        System.out.println("----------pageMenuFunctions.size="+pageMenuFunctions.size());

        logger.info("----------commonFunctions.size="+commonFunctions.size());
    }




    public void assignAuth(String userName)
    {
        String menuIndexs = (authProp.get(userName)==null)?"":authProp.get(userName).toString();
        if(BlankUtil.isBlank(menuIndexs))
        {
            return;
        }

        String[] allMenuIndex = menuIndexs.split(",");

        Set<String> funlist= new HashSet<String>();

        //key是主菜单 value--是子菜单列表
        Map<String, List<String>> menuMap = new LinkedHashMap<String, List<String>>();

        //提取用户所拥有的 所有主-子菜单数据
        for(String index:allMenuIndex){
            int i = Integer.parseInt(index);

            //funMap表示一个菜单与功能数组的记录
            Map<String, String[]> funMap = pageMenuFunctions.get(i);

            String key = funMap.keySet().iterator().next();
            String[] values = funMap.get(key);
            for(String v:values){
                funlist.add(v);
            }

            //主--子菜单
            String[]menuKeys = key.split("_");


            List<String> menuArry = menuMap.get(menuKeys[0]);
            if(BlankUtil.isBlank(menuArry)){
                menuArry = new ArrayList<String>();
                menuMap.put(menuKeys[0], menuArry);
            }
            menuArry.add(menuKeys[1]);
        }

        //提取用户所拥有的所有功能
        funlist.addAll(commonFunctions);
        assgnFunctions.put(userName, funlist);


        //组合用户所拥有的菜单
        List<Map<String, List<String>>> menuList = new ArrayList<Map<String,List<String>>>();

        for(String key:menuMap.keySet()){
            Map<String, List<String>> menuM = new HashMap<String, List<String>>();
            menuM.put(key, menuMap.get(key));

            menuList.add(menuM);
        }

        //用户所拥有的菜单
        assgnMenus.put(userName, menuList);
    }

    /**获取用户菜单**/
    public List<Map<String, List<String>>> getUserMenus(String userName)
    {
        return assgnMenus.get(userName);
    }
}
