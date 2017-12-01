package com.configcenter.service.rbac;

import com.configcenter.annotation.LogTag;
import com.configcenter.dao.IDao;
import com.configcenter.dao.rbac.RoleDao;
import com.configcenter.dao.rbac.RoleResourceDao;
import com.configcenter.dao.rbac.TagRelaUserOrRoleDao;
import com.configcenter.dao.rbac.UserRoleDao;
import com.configcenter.model.rbac.Role;
import com.configcenter.model.rbac.RoleResource;
import com.configcenter.model.rbac.User;
import com.configcenter.model.rbac.UserRole;
import com.configcenter.service.manager.LogManager;
import com.configcenter.service.SessionManager;
import com.configcenter.service.core.BaseService;
import com.configcenter.vo.CommonQueryVo;
import com.configcenter.vo.RoleVo;
import com.util.base.NumberUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 角色服务类
 */
@Service
@LogTag("add,update")
public class RoleService extends BaseService<Role> {

    private static Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleResourceDao roleResourceDao;

    @Autowired
    private TagRelaUserOrRoleDao tagRelaUserOrRoleDao;

    public IDao getDao()
    {
        return roleDao;
    }


    public List<Role> queryAll()
    {
        List<Role> allList = new ArrayList<Role>();
        int start =0;
        int pageSize=5000;
        int queryCount=pageSize;

        CommonQueryVo queryVo=new CommonQueryVo();
        queryVo.setStart(start);
        queryVo.setRows(pageSize);

        while(queryCount>=pageSize)
        {
            List<Role> perList=queryList(queryVo);
            queryCount = (perList==null)?0:perList.size();
            if(queryCount>0)
            {
                start =start+queryCount;
                queryVo.setStart(start);

                allList.addAll(perList);
                perList.clear();
            }
        }
        return allList;
    }

    public void delRoleAndRela(Integer id)
    {
        if(id==null)
        {
            return;
        }
        Role role = new Role();
        role.setId(id);
        role =getById(role);
        if(role==null)
        {
            return;
        }

        delete(role);

        userRoleDao.deleteByRole(role);
        roleResourceDao.deleteByRole(role);
        tagRelaUserOrRoleDao.deleteByRole(role);
        LogManager.log(SessionManager.getCurUser(), "删除角色"+role.getName());
    }

    //角色关联资源
    public void roleRelaResource(Integer roleId,String resourceIds)
    {
        if(roleId==null )
        {
            return;
        }
        Role role = new Role();
        role.setId(roleId);
        role =getById(role);
        if(role==null)
        {
            return;
        }

        List<RoleResource> list = roleResourceDao.queryByRole(role);

        List<Integer> resources= new ArrayList<Integer>();

        if(!StringUtils.isBlank(resourceIds)) {
            String rsArray[] = resourceIds.split(",");
            for (String per : rsArray) {
                if (NumberUtil.isNumeric(per)) {
                    resources.add(Integer.parseInt(per));
                }
            }
        }

        //从已有的找出新设置中不存在的作为新增的
        List<RoleResource> addRsList =findAdd(roleId,list,resources);

        //从新设置的找出在已有中不存在的作为删除的
        List<RoleResource> delRsList =findDel(list,resources);

        //删关联资源
        if(delRsList!=null && delRsList.size()>0) {
            for(RoleResource rr:delRsList) {
                roleResourceDao.delete(rr);
            }
            delRsList.clear();
        }

        //添加关联资源
        if(addRsList!=null && addRsList.size()>0) {
            for(RoleResource rr:addRsList) {
                roleResourceDao.add(rr);
            }
            addRsList.clear();
        }

        if(list!=null)
        {
            list.clear();
        }
        LogManager.log(SessionManager.getCurUser(), "角色"+role.getName()+"关联资源"+resourceIds);
    }

    public List<RoleResource>  findAdd(Integer roleId, List<RoleResource> list, List<Integer> resources)
    {
        List<RoleResource> addRsList = new ArrayList<RoleResource>();

        int hasCount= (list==null)?0:list.size();
        //从已有的找出新设置中不存在的作为新增的
        for(Integer resourceId:resources)
        {
            boolean has =false;
            if(hasCount>0)
            {
                for(RoleResource rr:list)
                {
                    if(rr.getResourceId().intValue()==resourceId)
                    {
                        has=true;
                        break;
                    }
                }
            }
            if(!has)
            {
                RoleResource newrr= new RoleResource();
                newrr.setRoleId(roleId);
                newrr.setResourceId(resourceId);

                addRsList.add(newrr);
            }
        }

        return addRsList;
    }

    public  List<RoleResource>  findDel( List<RoleResource> list, List<Integer> resources)
    {
        List<RoleResource> delRsList = new ArrayList<RoleResource>();

        int hasCount= (list==null)?0:list.size();

        //从新设置的找出在已有中不存在的作为删除的
        if(hasCount>0)
        {
            for(RoleResource rr:list)
            {
                boolean has=false;
                for(Integer resourceId:resources) {
                    if (rr.getResourceId().intValue() == resourceId) {
                        has = true;
                        break;
                    }
                }
                if(!has)
                {
                    delRsList.add(rr);
                }
            }
        }
        return delRsList;
    }

    //获取带用户选择标记的角色列表
    public List<RoleVo> getUserSelectRole(Integer userId)
    {
        if(userId==null)
        {
            return Collections.EMPTY_LIST;
        }
        List<Role> allList =queryAll();
        if(allList==null || allList.size()<=0)
        {
            return Collections.EMPTY_LIST;
        }
        User user = new User();
        user.setId(userId);
        List<UserRole> userRoleList = userRoleDao.queryByUser(user);
        List<Integer> selectRoleIds = new ArrayList<Integer>();
        if(userRoleList!=null)
        {
            for(UserRole userRole:userRoleList)
            {
                selectRoleIds.add(userRole.getRoleId());
            }
            userRoleList.clear();
        }
        List<RoleVo> list  =new ArrayList<RoleVo>();
        try {
            for (Role role : allList) {
                RoleVo vo = new RoleVo();
                BeanUtils.copyProperties(vo, role);
                if (selectRoleIds.contains(role.getId())) {
                    vo.setIsSelect(true);
                }
                list.add(vo);
            }
            allList.clear();
        }catch(Exception e)
        {
            logger.error("----------getUserSelectRole error,",e);
        }
        return list;
    }
}
