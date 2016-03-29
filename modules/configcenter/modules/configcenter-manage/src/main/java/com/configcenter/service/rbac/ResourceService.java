package com.configcenter.service.rbac;

import com.configcenter.annotation.LogTag;
import com.configcenter.dao.IDao;
import com.configcenter.dao.rbac.ResourceDao;
import com.configcenter.dao.rbac.RoleResourceDao;
import com.configcenter.model.rbac.Resource;
import com.configcenter.model.rbac.Role;
import com.configcenter.model.rbac.RoleResource;
import com.configcenter.service.LogManager;
import com.configcenter.service.SessionManager;
import com.configcenter.service.core.BaseService;
import com.configcenter.vo.CommonQueryVo;
import com.configcenter.vo.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 资源服务类
 */
@Service
@LogTag("add,update")
public class ResourceService extends BaseService<Resource> {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private RoleResourceDao roleResourceDao;

    public IDao getDao()
    {
        return resourceDao;
    }

    public List<Resource> queryAll()
    {
        List<Resource> allList = new ArrayList<Resource>();
        int start =0;
        int pageSize=5000;
        int queryCount=pageSize;

        CommonQueryVo queryVo=new CommonQueryVo();
        queryVo.setStart(start);
        queryVo.setRows(pageSize);

        while(queryCount>=pageSize)
        {
            List<Resource> perList=queryList(queryVo);
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

    //清除资源及相关
    public void delResourceAndRela(Integer id)
    {
        if(id==null)
        {
            return;
        }
        Resource resource = new Resource();
        resource.setId(id);
        resource =getById(resource);
        if(resource==null)
        {
            return;
        }

        delete(resource);

        roleResourceDao.deleteByResource(resource);
        LogManager.log(SessionManager.getCurUser(), "删除资源" + resource.getName());
    }

    //根据角色获取选择的资源树
    public List<TreeNode> getRoleSelectTreeNode(Integer roleId) {

        Role role = new Role();
        role.setId(roleId);

        List<RoleResource> roleResourceList = roleResourceDao.queryByRole(role);
        //已关联的资源
        List<Integer> hasRelaList= new ArrayList<Integer>();
        if(roleResourceList!=null)
        {
            for(RoleResource  roleResource:roleResourceList)
            {
                hasRelaList.add(roleResource.getResourceId());
            }
        }
        List<Resource> allResourceList =queryAll();
        if(allResourceList==null || allResourceList.size()<=0)
        {
            return Collections.EMPTY_LIST;
        }
        //转换成父子关系的map结构数据
        Map<Integer, List<Resource>> parentMap = transParentMap(allResourceList);
        allResourceList.clear();

        TreeNode root = new TreeNode();
        root.setId(0);
        //将父子结构的资源转换成一棵树
        transTree(root, hasRelaList, parentMap);

        hasRelaList.clear();
        parentMap.clear();

        return root.getChildren();
    }

    //转换成父子关系的map结构数据
    public Map<Integer,List<Resource>> transParentMap(List<Resource> list)
    {
        Map<Integer,List<Resource>> map = new HashMap<Integer,List<Resource>>();
        if(list!=null)
        {
            for(Resource rs:list)
            {
                Integer pid = rs.getParentId();
                if(pid==null)
                {
                    pid=0;
                }
                List<Resource> children = map.get(pid);
                if(children==null)
                {
                    children = new ArrayList<Resource>();
                    map.put(pid,children);
                }
                children.add(rs);
            }
        }
        return map;
    }

    //将父子结构的资源转换成一棵树
    public TreeNode transTree(TreeNode root,List<Integer> hasRelaList,Map<Integer, List<Resource>> parentMap  )
    {
        List<Resource> child= parentMap.get(root.getId());
        if(child==null || child.size()<=0)
        {
            root.setChildren(Collections.EMPTY_LIST);
            return root;
        }
        List<TreeNode> children = new ArrayList<TreeNode>();

        for(Resource rs:child)
        {
            TreeNode node = new TreeNode();
            node.setPid(root.getId());
            node.setId(rs.getId());
            node.setText(rs.getName());
            transTree(node,hasRelaList,parentMap);
            if(node.getChildren()==null || node.getChildren().size()<=0)
            {
                if(hasRelaList.contains(node.getId()))
                {
                    node.setChecked(true);
                }else
                {
                    node.setChecked(false);
                }
            }
            children.add(node);
        }
        root.setChildren(children);
        return root;
    }

}
