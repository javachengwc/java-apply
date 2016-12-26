package com.db.service.sys;

import com.db.dao.mapper.SysResourceMapper;
import com.db.model.pojo.SysResource;
import com.db.model.pojo.SysResourceExample;
import com.db.model.vo.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * 资源菜单服务类
 */
@Service
public class ResourceService {

    @Autowired
    private SysResourceMapper  sysResourceMapper;

    public List<TreeNode> queryMenuList()
    {
        int parentId =0;
        List<TreeNode> nodeList = queryMenuNode(parentId);
        return nodeList;
    }

    public List<TreeNode> queryMenuNode(int parentId)
    {
        List<TreeNode> nodeList = new ArrayList<TreeNode>();

        List<SysResource> list =queryListByPid(parentId);
        if(list!=null)
        {
            for(SysResource resource:list)
            {
                TreeNode node = resourceToNode(resource);
                nodeList.add(node);
                int pid= resource.getId();
                node.setChildren(queryMenuNode(pid));
                if(node.getChildren()!=null && node.getChildren().size()>0)
                {
                    node.setIsParent(1);
                }
            }
        }
        return nodeList;
    }

    public List<SysResource> queryListByPid(int parentId)
    {
        SysResourceExample example = new SysResourceExample();
        SysResourceExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        return  sysResourceMapper.selectByExample(example);
    }

    public TreeNode resourceToNode(SysResource resource)
    {
        TreeNode node = new TreeNode();
        node.setId(resource.getId());
        node.setText(resource.getName());
        node.setUrl(resource.getPath());
        node.setPid(resource.getParentId());

        return node;
    }
}
