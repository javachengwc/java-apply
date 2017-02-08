package com.configcenter.service.rbac;

import com.configcenter.dao.IDao;
import com.configcenter.dao.rbac.TagRelaUserOrRoleDao;
import com.configcenter.dao.rbac.UserDao;
import com.configcenter.dao.rbac.UserRoleDao;
import com.configcenter.model.rbac.*;
import com.configcenter.service.LogManager;
import com.configcenter.service.SessionManager;
import com.configcenter.service.core.BaseService;
import com.configcenter.vo.TreeNode;
import com.util.base.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务类
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private TagRelaUserOrRoleDao tagRelaUserOrRoleDao;

    public IDao getDao()
    {
        return userDao;
    }

    public User getByAccount(String account)
    {
        User queryUser = new User();
        queryUser.setAccount(account);
        return userDao.getByAccount(queryUser);
    }

    public List<TreeNode> queryMenuList(String account)
    {
        int parentId=0;
        List<TreeNode> nodeList = queryUserMenuNode(account,parentId);
        return nodeList;
    }

    public List<TreeNode>  queryUserMenuNode(String account,int parentId)
    {
        List<TreeNode> nodeList = new ArrayList<TreeNode>();

        Map<String,Object> param= new HashMap<String,Object>(2);
        param.put("account",account);
        param.put("parentId",parentId);

        List<Resource> list = userDao.queryUserResource(param);
        if(list!=null)
        {
            for(Resource resource:list)
            {
                TreeNode node = resourceToNode(resource);
                nodeList.add(node);
                int pid= resource.getId();
                node.setChildren(queryUserMenuNode(account,pid));
                if(node.getChildren()!=null && node.getChildren().size()>0)
                {
                    node.setIsParent(1);
                }
            }
        }
        return nodeList;
    }

    public TreeNode resourceToNode(Resource resource)
    {
        TreeNode node = new TreeNode();
        node.setId(resource.getId());
        node.setText(resource.getName());
        node.setUrl(resource.getPath());
        node.setPid(resource.getParentId());

        return node;
    }

    public void delUserAndRela(Integer id )
    {
        if(id==null)
        {
            return;
        }
        User user = new User();
        user.setId(id);
        user = getById(user);
        if(user==null)
        {
            return;
        }

        delete(user);

        userRoleDao.deleteByUser(user);
        tagRelaUserOrRoleDao.deleteByUser(user);
        LogManager.log(SessionManager.getCurUser(), "删除用户" + user.getAccount());
    }

    //授权用户
    public void  authUser(Integer userId, String roleIds)
    {
        if(userId==null )
        {
            return;
        }
        User user = new User();
        user.setId(userId);
        user = getById(user);
        if(user==null)
        {
            return;
        }

        List<UserRole> list = userRoleDao.queryByUser(user);

        List<Integer> roles= new ArrayList<Integer>();

        if(!StringUtils.isBlank(roleIds)) {
            String roleArray[] = roleIds.split(",");
            for (String per : roleArray) {
                if (NumberUtil.isNumeric(per)) {
                    roles.add(Integer.parseInt(per));
                }
            }
        }

        //从已有的找出新设置中不存在的作为新增的
        List<UserRole> addRoleList =findAdd(userId,list,roles);

        //从新设置的找出在已有中不存在的作为删除的
        List<UserRole> delRoleList =findDel(list,roles);

        //删
        if(delRoleList!=null && delRoleList.size()>0) {
            for(UserRole rr:delRoleList) {
                userRoleDao.delete(rr);
            }
            delRoleList.clear();
        }

        //加
        if(addRoleList!=null && addRoleList.size()>0) {
            for(UserRole rr:addRoleList) {
                userRoleDao.add(rr);
            }
            addRoleList.clear();
        }

        if(list!=null)
        {
            list.clear();
        }
        LogManager.log(SessionManager.getCurUser(), "用户"+user.getAccount()+"授权" + roleIds);
    }

    public List<UserRole>  findAdd(Integer userId, List<UserRole> list, List<Integer> roles)
    {
        List<UserRole> addRoleList = new ArrayList<UserRole>();

        int hasCount= (list==null)?0:list.size();
        //从已有的找出新设置中不存在的作为新增的
        for(Integer roleId:roles)
        {
            boolean has =false;
            if(hasCount>0)
            {
                for(UserRole rr:list)
                {
                    if(rr.getRoleId().intValue()==roleId)
                    {
                        has=true;
                        break;
                    }
                }
            }
            if(!has)
            {
                UserRole newrr= new UserRole();
                newrr.setUserId(userId);
                newrr.setRoleId(roleId);

                addRoleList.add(newrr);
            }
        }
        return addRoleList;
    }

    public  List<UserRole>  findDel( List<UserRole> list, List<Integer> roles)
    {
        List<UserRole> delRoleList = new ArrayList<UserRole>();

        int hasCount= (list==null)?0:list.size();

        //从新设置的找出在已有中不存在的作为删除的
        if(hasCount>0)
        {
            for(UserRole rr:list)
            {
                boolean has=false;
                for(Integer roleId:roles) {
                    if (rr.getRoleId().intValue() == roleId) {
                        has = true;
                        break;
                    }
                }
                if(!has)
                {
                    delRoleList.add(rr);
                }
            }
        }
        return delRoleList;
    }

    public int add(User user)
    {
        int rt =getDao().add(user);
        LogManager.log(SessionManager.getCurUser(), "新增用户:[" +user.getAccount()+","+user.getName()+"]" );
        return rt;
    }

    public int update(User user)
    {
        int rt = getDao().update(user);
        LogManager.log(SessionManager.getCurUser(), "修改用户:[" +user.getAccount()+","+user.getName()+"]" );
        return rt;
    }
}