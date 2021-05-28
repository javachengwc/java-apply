package com.manage.rbac.service.impl;

import com.manage.rbac.dao.MenuMapper;
import com.manage.rbac.dao.ext.MenuDao;
import com.manage.rbac.entity.MenuExample;
import com.manage.rbac.entity.ext.MenuDO;
import com.manage.rbac.model.dto.MenuDTO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.SystemDTO;
import com.manage.rbac.model.dto.SystemMenuDTO;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.service.ISystemService;
import com.util.TransUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.rbac.entity.Menu;
import com.manage.rbac.service.IMenuService;
import org.springframework.transaction.annotation.Transactional;

import com.manage.rbac.entity.System;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Menu的服务接口的实现类
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private ISystemService systemService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuMapper mapper;

    //查询菜单详情
    @Override
    public MenuDTO getMenuDetail(Integer id) {
        MenuDO menuDO = menuDao.getMenu(id);
        if(menuDO==null) {
            return null;
        }
        MenuDTO menuDTO = TransUtil.transEntity(menuDO,MenuDTO.class);
        return menuDTO;
    }

    //根据父级和名称查询数量
    @Override
    public long countByParentAndName(Integer parentId,String name) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        criteria.andNameEqualTo(name);
        long count = mapper.countByExample(example);
        return count;
    }

    //添加菜单
    @Override
    public boolean addMenu(MenuDTO menuDTO) {
        String name = menuDTO.getName();
        log.info("MenuServiceImpl addMenu start,name={}",name);
        Menu menu = TransUtil.transEntity(menuDTO,Menu.class);
        Integer parentId = menu.getParentId();
        if(parentId==null) {
            parentId=0;
            menu.setParentId(parentId);
        }
        if(parentId<=0) {
            menu.setLevel(1);
        } else {
            Menu parentMenu = mapper.selectByPrimaryKey(parentId);
            Integer parentLevel = parentMenu.getLevel();
            Integer curLevel = parentLevel == null ? 1 : (parentLevel + 1);
            menu.setLevel(curLevel);
        }

        OperatorDTO operator = menuDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        menu.setCreateTime(now);
        menu.setModifyTime(now);
        menu.setCreaterId(operatorId);
        menu.setCreaterNickname(operatorNickname);
        menu.setOperatorId(operatorId);
        menu.setOperatorNickname(operatorNickname);

        mapper.insertSelective(menu);
        return true;
    }

    //修改菜单
    @Transactional
    @Override
    public boolean updateMenu(MenuDTO menuDTO) {
        log.info("MenuServiceImpl addMenu start,menuId={}",menuDTO.getId());
        OperatorDTO operator = menuDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Menu menu = TransUtil.transEntity(menuDTO,Menu.class);
        Date now = new Date();
        menu.setModifyTime(now);
        menu.setOperatorId(operatorId);
        menu.setOperatorNickname(operatorNickname);

        mapper.updateByPrimaryKeySelective(menu);
        return true;
    }

    //查询子菜单数量
    @Override
    public long countChild(Integer id) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);

        long count = mapper.countByExample(example);
        return count;
    }

    //启用菜单
    @Override
    public boolean enableMenu(Integer id, OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateMenuState(id, EnableEnum.ENABLE.getValue(),operatorId,operatorNickname);
        return true;
    }

    //禁用菜单
    @Override
    public boolean disableMenu(Integer id, OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateMenuState(id, EnableEnum.FORBID.getValue(),operatorId,operatorNickname);
        return true;
    }

    private int updateMenuState(Integer id,Integer state,Integer operatorId ,String operatorNickname) {
        Menu menu = new Menu();
        menu.setId(id);
        //0--正常，1--禁用
        menu.setState(state);
        menu.setOperatorId(operatorId);
        menu.setOperatorNickname(operatorNickname);
        int rt =mapper.updateByPrimaryKeySelective(menu);
        return rt;
    }

    //删除菜单
    @Transactional
    @Override
    public boolean deleteMenu(Integer id) {
        mapper.deleteByPrimaryKey(id);
        menuDao.deleteRoleMenuByMenu(id);
        return true;
    }

    //查询系统菜单树
    @Override
    public List<MenuDTO> listMenuTreeBySystem(Integer systemId) {
        List<Menu> list = this.listMenuBySystem(systemId);
        List<MenuDTO> rtList = this.genTree(list);
        return  rtList;
    }

    //查询系统菜单
    public List<Menu> listMenuBySystem(Integer systemId) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andSystemIdEqualTo(systemId);
        example.setOrderByClause(" parent_id asc , sort asc ");
        List<Menu> list = mapper.selectByExample(example);
        return list;
    }

    //生成菜单树
    @Override
    public List<MenuDTO> genTree(List<Menu> list) {
        if(list==null || list.size()<=0) {
            return null;
        }
        List<MenuDTO> dtoList =TransUtil.transList(list,MenuDTO.class);
        List<MenuDTO> rtList = new ArrayList<MenuDTO>();
        for (MenuDTO menu : dtoList) {
            if (menu.getParentId() == null || menu.getParentId() <= 0) {
                rtList.add(this.appendChildren(menu, dtoList));
            }
        }
        return rtList;
    }

    private MenuDTO appendChildren(MenuDTO parentMenu, List<MenuDTO> list) {
        for (MenuDTO per : list) {
            if (per.getParentId() != null && per.getParentId().intValue() == parentMenu.getId()) {
                parentMenu.getChildren().add(this.appendChildren(per, list));
            }
        }
        return parentMenu;
    }

    //查询用户菜单
    @Override
    public List<SystemMenuDTO> listSystemMenuByUser(Integer userId) {
        List<Menu> list1 = menuDao.listMenuByUserCrowd(userId);
        List<Menu> list2 = menuDao.listMenuByUserPost(userId);
        List<Menu> allList = new ArrayList<Menu>();
        if(list1!=null) {
            allList.addAll(list1);
        }
        if(list2!=null) {
            allList.addAll(list2);
        }
        List<System> allSystem = systemService.listAbleSystem();
        List<SystemMenuDTO> rtList = this.genUserMenu(allSystem,allList);
        return  rtList;
    }

    //生成用户菜单
    private List<SystemMenuDTO> genUserMenu(List<System> sysList, List<Menu> menuList) {
        List<SystemMenuDTO> list = new ArrayList<SystemMenuDTO>();
        if(sysList==null || sysList.size()<=0) {
            return list;
        }
        Map<Integer,List<Menu>> systemMenuMap = this.transSystemMenuMap(menuList);

        for(System system: sysList) {
            Integer systemId = system.getId();
            List<Menu> perList = systemMenuMap.get(systemId);
            if(perList==null || perList.size()<=0) {
                continue;
            }
            SystemDTO systemDTO = TransUtil.transEntity(system,SystemDTO.class);
            List<MenuDTO>  dtoList = TransUtil.transList(perList,MenuDTO.class);
            SystemMenuDTO systemMenuDTO = new SystemMenuDTO(systemDTO,dtoList);
            list.add(systemMenuDTO);
        }
        return list;
    }

    //查询角色关联系统菜单
    @Override
    public List<SystemMenuDTO> listSystemMenuByRole(Integer roleId) {
        List<Menu> list = menuDao.listMenuByRole(roleId);
        List<System> allSystem = systemService.listAbleSystem();
        List<Menu> allMenu = menuDao.listAbleMenu();
        List<SystemMenuDTO> rtList = this.genSystemMenu(allSystem,allMenu,list);
        return  rtList;
    }

    //生成系统菜单树
    private List<SystemMenuDTO> genSystemMenu(List<System> sysList,  List<Menu> allMenu, List<Menu> menuList) {
        List<SystemMenuDTO> list = new ArrayList<SystemMenuDTO>();
        if(sysList==null || sysList.size()<=0) {
            return list;
        }
        Set<Integer> hasMenuSet = new HashSet<Integer>();
        if(menuList!=null) {
            hasMenuSet=menuList.stream().map(Menu::getId).collect(Collectors.toSet());
        }
        Map<Integer,List<Menu>> systemMenuMap = this.transSystemMenuMap(allMenu);
        for(Map.Entry<Integer,List<Menu>> entry: systemMenuMap.entrySet()) {
            Integer systemId = entry.getKey();
            if(systemId==null ) {
                continue;
            }
            System system = this.findSystem(sysList,systemId);
            if(system==null) {
                continue;
            }
            SystemDTO systemDTO = TransUtil.transEntity(system,SystemDTO.class);
            List<Menu> sysMenuList = entry.getValue();
            List<MenuDTO>  dtoList = TransUtil.transList(sysMenuList,MenuDTO.class);
            for(MenuDTO per:dtoList) {
                per.setSelected(0);
                if(hasMenuSet.contains(per.getId())) {
                    per.setSelected(1);
                }
            }
            List<MenuDTO> menuDTOList = this.genTree2(dtoList);

            SystemMenuDTO systemMenuDTO = new SystemMenuDTO(systemDTO,menuDTOList);
            list.add(systemMenuDTO);
        }
        return list;
    }

    private List<MenuDTO> genTree2(List<MenuDTO> list) {
        if(list==null || list.size()<=0) {
            return null;
        }
        List<MenuDTO> rtList = new ArrayList<MenuDTO>();
        for (MenuDTO menu : list) {
            if (menu.getParentId() == null || menu.getParentId() <= 0) {
                rtList.add(this.appendChildren(menu, list));
            }
        }
        return rtList;
    }

    private System findSystem(List<System> sysList,Integer systemId) {
        System system = null;
        for(System per: sysList) {
            if(systemId.intValue()==per.getId()) {
                return per;
            }
        }
        return null;
    }

    private  Map<Integer,List<Menu>> transSystemMenuMap(List<Menu> list ) {
        Map<Integer,List<Menu>> map =new HashMap<Integer,List<Menu>>();
        if(list==null || list.size()<=0) {
            return map;
        }
        Map<Integer, Set<Integer>> setMap =new HashMap<Integer,Set<Integer>>();
        for(Menu menu:list) {
            Integer systemId = menu.getSystemId()==null?0:menu.getSystemId();
            List<Menu> mList = map.get(systemId);
            Set<Integer> idSet = setMap.get(systemId);
            if(mList==null) {
                mList= new ArrayList<Menu>();
                map.put(systemId,mList);
                idSet= new HashSet<Integer>();
                setMap.put(systemId,idSet);
            }
            if(!idSet.contains(menu.getId())) {
                idSet.add(menu.getId());
                mList.add(menu);
            }
        }
        return map;
    }

    //修改角色系统菜单
    @Transactional
    @Override
    public boolean updateRoleMenu(Integer roleId,List<SystemMenuDTO> systemMenuList) {
        log.info("MenuServiceImpl updateRoleMenu start,roleId={}",roleId);
        //先删除以前的
        menuDao.deleteRoleMenuByRole(roleId);
        if(systemMenuList==null || systemMenuList.size()<=0) {
            return Boolean.TRUE;
        }
        //再添加现在的
        for(SystemMenuDTO per:systemMenuList) {
            List<MenuDTO> menuList = per.getMenuList();
            if(menuList==null || menuList.size()<=0) {
                continue;
            }
            List<Integer> menuIdList = this.tipMenuIdList(menuList);
            menuDao.addRoleMenu(roleId,menuIdList);
        }
        return Boolean.TRUE;
    }

    public List<Integer> tipMenuIdList(List<MenuDTO> list ) {
        List<Integer> rtList= new ArrayList<Integer>();
        for(MenuDTO per:list) {
            rtList.add(per.getId());
            List<MenuDTO> childList = per.getChildren();
            if(childList!=null && childList.size()>0) {
                this.appendChildrenMenuId(rtList,childList);
            }
        }
        return rtList;
    }

    private void appendChildrenMenuId(List<Integer> rtList, List<MenuDTO> list) {
        for (MenuDTO per : list) {
            rtList.add(per.getId());
            List<MenuDTO> childList = per.getChildren();
            if(childList!=null && childList.size()>0) {
                this.appendChildrenMenuId(rtList,childList);
            }
        }
    }
}