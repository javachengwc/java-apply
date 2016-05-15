package com.shopstat.service.rbac;

import com.shopstat.dao.mapper.StResourceMapper;
import com.shopstat.model.pojo.StResource;
import com.shopstat.model.pojo.StResourceExample;
import com.shopstat.model.vo.TreeNode;
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
    private StResourceMapper  stResourceMapper;

    public List<TreeNode> queryMenuList()
    {
        int parentId =0;
        List<TreeNode> nodeList = queryMenuNode(parentId);
        return nodeList;
    }

    public List<TreeNode> queryMenuNode(int parentId)
    {
        List<TreeNode> nodeList = new ArrayList<TreeNode>();

        List<StResource> list =queryListByPid(parentId);
        if(list!=null)
        {
            for(StResource resource:list)
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

    public List<StResource> queryListByPid(int parentId)
    {
        StResourceExample example = new StResourceExample();
        StResourceExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        return  stResourceMapper.selectByExample(example);
    }

    public TreeNode resourceToNode(StResource resource)
    {
        TreeNode node = new TreeNode();
        node.setId(resource.getId());
        node.setText(resource.getName());
        node.setUrl(resource.getPath());
        node.setPid(resource.getParentId());

        return node;
    }
}
