package com.commonservice.invoke.util;

import com.commonservice.invoke.constant.Constant;
import com.commonservice.invoke.model.entity.system.Menu;
import com.commonservice.invoke.model.vo.MetaVo;
import com.commonservice.invoke.model.vo.RouterVo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MenuRouterUtil {

    public static List<RouterVo> buildMenuRouter(List<Menu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (Menu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setName(getRouterName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());

            boolean hidden = (menu.getVisible()==null || 0 == menu.getVisible())? true: false;
            router.setHidden(hidden);

            String link = isHttp(menu.getPath()) ? menu.getPath() :"";
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon(), link));

            List<Menu> chList = menu.getChildren();
            if (chList!=null && chList.size() > 0 && Constant.TYPE_DIR == menu.getType()) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenuRouter(chList));
            } else if (isMenuFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setQuery(menu.getQuery());
                children.setMeta(new MetaVo(menu.getName(), menu.getIcon(), link));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    //获取路由名称
    public static String getRouterName(Menu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 目录
        if (isMenuFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    public static boolean isMenuFrame(Menu menu)
    {
        return menu.getParentId().intValue() == 0 && Constant.TYPE_MENU == menu.getType()
                && Constant.OUTLINK_NO==menu.getOutLink();
    }

    //获取路由地址
    public static String getRouterPath(Menu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && Constant.TYPE_DIR == menu.getType()
                && Constant.OUTLINK_NO == menu.getOutLink())
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    //获取组件信息
    public static String getComponent(Menu menu)
    {
        String component = Constant.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = Constant.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = Constant.PARENT_VIEW;
        }
        return component;
    }

    //是否为内链组件
    public static boolean isInnerLink(Menu menu) {
        return Constant.OUTLINK_NO==menu.getOutLink() && isHttp(menu.getPath());
    }

    //是否为parentview组件
    public static boolean isParentView(Menu menu)
    {
        return menu.getParentId().intValue() != 0 && Constant.TYPE_DIR==menu.getType();
    }

    public static boolean isHttp(String link)
    {
        return StringUtils.startsWithAny(link, Constant.HTTP, Constant.HTTPS);
    }

    //内链特殊字符替换
    public static String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(path, new String[] { Constant.HTTP, Constant.HTTPS }, new String[] { "", "" });
    }
}
