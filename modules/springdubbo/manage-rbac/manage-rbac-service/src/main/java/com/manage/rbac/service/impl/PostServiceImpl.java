package com.manage.rbac.service.impl;

import com.manage.rbac.dao.OrgPostMapper;
import com.manage.rbac.dao.PostMapper;
import com.manage.rbac.dao.PostRoleMapper;
import com.manage.rbac.dao.ext.PostDao;
import com.manage.rbac.entity.*;
import com.manage.rbac.entity.ext.OrgPostDO;
import com.manage.rbac.entity.ext.PostRoleDO;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.service.*;
import com.model.base.PageVo;
import com.util.TransUtil;
import com.util.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Post的服务接口的实现类
 */
@Slf4j
@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostMapper mapper;

    @Autowired
    private PostRoleMapper postRoleMapper;

    @Autowired
    private OrgPostMapper orgPostMapper;


    //分页查询岗位
    @Override
    public PageVo<PostDTO> listPostPage(String post, Integer pageIndex, Integer pageSize) {

        PageVo<PostDTO> pageData = new PageVo<PostDTO>();
        //查询总条数
        long total = postDao.countByNameOrCode(post);
        int totalCount = new Long(total).intValue();
        pageData.setTotalCount(totalCount);

        Page page = new Page(pageIndex, pageSize, totalCount);
        if (page.isBound()) {
            //数据页越界处理
            return pageData;
        }

        int start = page.getStart();
        List<Post> list = postDao.listPostPage(post,start,pageSize);
        //查角色
        List<PostDTO> dtoList = TransUtil.transList(list,PostDTO.class);
        List<Integer> postIdList = dtoList.stream().map(PostDTO::getId).collect(Collectors.toList());
        Map<Integer, List<RoleDTO>> postRoleMap = this.queryRoleByPostList(postIdList);
        if(postRoleMap!=null) {
            for(PostDTO postDTO:dtoList) {
                Integer postId = postDTO.getId();
                List<RoleDTO> roleList = postRoleMap.get(postId);
                //注入岗位的角色信息
                postDTO.setRoleList(roleList==null?Collections.EMPTY_LIST:roleList);
            }
        }

        Map<Integer, List<OrganizationDTO>> orgPostMap = this.queryOrgByPostList(postIdList);
        if(orgPostMap!=null) {
            for(PostDTO postDTO:dtoList) {
                Integer postId = postDTO.getId();
                List<OrganizationDTO> orgList = orgPostMap.get(postId);
                //注入用户组的机构信息
                postDTO.setOrgList(orgList==null?Collections.EMPTY_LIST:orgList);
            }
        }

        pageData.setList(dtoList);
        return pageData;
    }

    private  Map<Integer, List<RoleDTO>> queryRoleByPostList( List<Integer> postIdList) {
        if(postIdList==null || postIdList.size()<=0) {
            return null;
        }
        List<PostRoleDO> postRoleList = postDao.listPostRoleByPosts(postIdList);
        if(postRoleList==null || postRoleList.size()<=0) {
            return null;
        }
        Map<Integer, List<RoleDTO>> postRoleMap = new HashMap<Integer, List<RoleDTO>>();
        for(PostRoleDO per:postRoleList) {
            Integer postId = per.getPostId();
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(per.getRoleId());
            roleDTO.setName(per.getRoleName());

            List<RoleDTO> roleList = postRoleMap.get(postId);
            if(roleList==null) {
                roleList= new ArrayList<RoleDTO>();
                postRoleMap.put(postId,roleList);
            }
            roleList.add(roleDTO);
        }
        return postRoleMap;
    }

    private  Map<Integer, List<OrganizationDTO>> queryOrgByPostList( List<Integer> postIdList) {
        if(postIdList==null || postIdList.size()<=0) {
            return null;
        }
        List<OrgPostDO> list = postDao.listOrgPostByPosts(postIdList);
        if(list==null || list.size()<=0) {
            return null;
        }
        Map<Integer, List<OrganizationDTO>> orgPostMap = new HashMap<Integer, List<OrganizationDTO>>();
        for(OrgPostDO per:list) {
            Integer postId = per.getPostId();
            OrganizationDTO orgDTO = new OrganizationDTO();
            orgDTO.setId(per.getOrgId());
            orgDTO.setName(per.getOrgName());

            List<OrganizationDTO> orgList = orgPostMap.get(postId);
            if(orgList==null) {
                orgList= new ArrayList<OrganizationDTO>();
                orgPostMap.put(postId,orgList);
            }
            orgList.add(orgDTO);
        }
        return orgPostMap;
    }

    //查询岗位详情
    @Override
    public PostDTO getPostDetail(Integer id) {
        Post post =mapper.selectByPrimaryKey(id);
        if(post==null) {
            return null;
        }
        PostDTO postDTO = TransUtil.transEntity(post,PostDTO.class);

        List<Role> rList =roleService.listRoleByPost(id);
        List<RoleDTO> roleList = TransUtil.transList(rList,RoleDTO.class);
        postDTO.setRoleList(roleList);

        List<Organization> oList = organizationService.listOrgByPost(id);
        List<OrganizationDTO> orgList = TransUtil.transList(oList,OrganizationDTO.class);
        postDTO.setOrgList(orgList);

        return postDTO;
    }

    //根据岗位名称查询数量
    @Override
    public long countByName(String name) {
        PostExample example = new PostExample();
        PostExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        long count = mapper.countByExample(example);
        return count;
    }

    //根据编码查询数量
    @Override
    public long countByCode(String code) {
        PostExample example = new PostExample();
        PostExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code);
        long count = mapper.countByExample(example);
        return count;
    }

    //启用岗位
    @Override
    public boolean enablePost(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updatePostState(id, EnableEnum.ENABLE.getValue(),operatorId,operatorNickname);
        return true;
    }

    //禁用岗位
    @Override
    public boolean disablePost(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updatePostState(id,EnableEnum.FORBID.getValue(),operatorId,operatorNickname);
        return true;
    }

    private int updatePostState(Integer id,Integer state,Integer operatorId,String operatorNickname) {
        Post post = new Post();
        post.setId(id);
        post.setState(state);//0--正常，1--禁用
        post.setOperatorId(operatorId);
        post.setOperatorNickname(operatorNickname);
        int rt =mapper.updateByPrimaryKeySelective(post);
        return rt;
    }

    //添加岗位
    @Transactional
    @Override
    public boolean addPost(PostDTO postDTO) {
        String name = postDTO.getName();
        log.info("PostServiceImpl addPost start,name={}",name);

        Post post = TransUtil.transEntity(postDTO,Post.class);
        List<RoleDTO> rList = postDTO.getRoleList();
        List<OrganizationDTO> oList = postDTO.getOrgList();
        List<Integer> roleIdList =rList==null?null: rList.stream().map(RoleDTO::getId).collect(Collectors.toList());
        List<Integer> orgIdList = oList==null?null: oList.stream().map(OrganizationDTO::getId).collect(Collectors.toList());
        OperatorDTO operator = postDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        post.setCreateTime(now);
        post.setModifyTime(now);
        post.setCreaterId(operatorId);
        post.setCreaterNickname(operatorNickname);
        post.setOperatorId(operatorId);
        post.setOperatorNickname(operatorNickname);

        mapper.insertSelective(post);
        Integer postId =post.getId();
        log.info("PostServiceImpl addPost postId={}",postId);
        List<PostRole> prList = this.transPostRole(postId,roleIdList);
        if( prList!=null) {
            this.addBatchPostRole(prList);
        }
        List<OrgPost> opList = this.transOrgPost(postId,orgIdList);
        if( opList!=null) {
            this.addBatchOrgPost(opList);
        }
        return true;
    }

    private Integer addBatchPostRole(List<PostRole> list) {
        Integer rt=0;
        for(PostRole per:list) {
            rt+=postRoleMapper.insertSelective(per);
        }
        return rt;
    }

    private Integer addBatchOrgPost(List<OrgPost> list) {
        Integer rt=0;
        for(OrgPost per:list) {
            rt+=orgPostMapper.insertSelective(per);
        }
        return rt;
    }

    //修改岗位
    @Transactional
    @Override
    public boolean updatePost(PostDTO postDTO) {
        Integer postId = postDTO.getId();
        log.info("PostServiceImpl updatePost start,postId={}",postId);

        Post post = TransUtil.transEntity(postDTO,Post.class);
        List<RoleDTO> rList = postDTO.getRoleList();
        List<OrganizationDTO> oList = postDTO.getOrgList();

        List<Integer> roleIdList =rList==null?null: rList.stream().map(RoleDTO::getId).collect(Collectors.toList());
        List<Integer> orgIdList = oList==null?null: oList.stream().map(OrganizationDTO::getId).collect(Collectors.toList());

        OperatorDTO operator = postDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        post.setModifyTime(now);
        post.setOperatorId(operatorId);
        post.setOperatorNickname(operatorNickname);

        mapper.updateByPrimaryKeySelective(post);

        List<PostRole> orglPrList =this.listPostRole(postId);
        List<PostRole> curPrList = this.transPostRole(postId,roleIdList);
        //对比产生新增的岗位角色
        List<PostRole> addPrList = this.genAddPostRoleList(orglPrList,curPrList);
        //对比产生删除的岗位角色
        List<PostRole> delPrList = this.genDelPostRoleList(orglPrList,curPrList);
        if( addPrList!=null) {
            this.addBatchPostRole(addPrList);
        }
        if(delPrList!=null) {
            this.deleteBatchPostRole(delPrList);
        }

        List<OrgPost> orglOpList =this.listOrgPost(postId);
        List<OrgPost> curOpList = this.transOrgPost(postId,orgIdList);
        //对比产生新增的机构岗位
        List<OrgPost> addOpList = this.genAddOrgPostList(orglOpList,curOpList);
        //对比产生删除的机构岗位
        List<OrgPost> delOpList = this.genDelOrgPostList(orglOpList,curOpList);
        if( addOpList!=null) {
            this.addBatchOrgPost(addOpList);
        }
        if(delOpList!=null) {
            this.deleteBatchOrgPost(delOpList);
        }
        return true;
    }

    private List<PostRole> listPostRole(Integer postId) {
        PostRoleExample example = new PostRoleExample();
        PostRoleExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        List<PostRole> list = postRoleMapper.selectByExample(example);
        return list;
    }

    private List<OrgPost> listOrgPost(Integer postId) {
        OrgPostExample example = new OrgPostExample();
        OrgPostExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        List<OrgPost> list = orgPostMapper.selectByExample(example);
        return list;
    }

    private Integer deleteBatchPostRole(List<PostRole> list) {
        Integer rt=0;
        for(PostRole per:list) {
            rt+=this.deletePostRole(per.getPostId(),per.getRoleId());
        }
        return rt;
    }

    private Integer deletePostRole(Integer postId,Integer roleId) {
        PostRoleExample example = new PostRoleExample();
        PostRoleExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        criteria.andRoleIdEqualTo(roleId);
        Integer rt=postRoleMapper.deleteByExample(example);
        return rt;
    }

    private Integer deleteBatchOrgPost(List<OrgPost> list) {
        Integer rt=0;
        for(OrgPost per:list) {
            rt+=this.deleteOrgPost(per.getOrgId(),per.getPostId());
        }
        return rt;
    }

    private Integer deleteOrgPost(Integer orgId,Integer postId) {
        OrgPostExample example = new OrgPostExample();
        OrgPostExample.Criteria criteria = example.createCriteria();
        criteria.andOrgIdEqualTo(orgId);
        criteria.andPostIdEqualTo(postId);
        Integer rt=orgPostMapper.deleteByExample(example);
        return rt;
    }

    //对比产生新增的岗位角色
    private List<PostRole> genAddPostRoleList(List<PostRole> orglList,List<PostRole> curList) {
        if(curList==null) {
            return null;
        }
        if(orglList==null) {
            return curList;
        }
        Set<String> orglSet = new HashSet<String>();
        for(PostRole per:orglList) {
            orglSet.add(per.getPostId()+"_"+per.getRoleId());
        }
        List<PostRole> rtList = new ArrayList<PostRole>();
        for(PostRole per: curList) {
            //遍历当前的
            String key = per.getPostId()+"_"+per.getRoleId();
            if(!orglSet.contains(key)) {
                //原来的不包含当前的，则为新增
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生删除的岗位角色
    private List<PostRole> genDelPostRoleList(List<PostRole> orglList,List<PostRole> curList) {
        if(orglList==null) {
            return null;
        }
        if(curList==null) {
            return orglList;
        }
        Set<String> curSet = new HashSet<String>();
        for(PostRole per:curList) {
            curSet.add(per.getPostId()+"_"+per.getRoleId());
        }
        List<PostRole> rtList = new ArrayList<PostRole>();
        for(PostRole per: orglList) {
            //遍历原来的
            String key = per.getPostId()+"_"+per.getRoleId();
            if(!curSet.contains(key)) {
                //当前的不包含原来的，则为删除
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生新增的机构岗位
    private List<OrgPost> genAddOrgPostList(List<OrgPost> orglList,List<OrgPost> curList) {
        if(curList==null) {
            return null;
        }
        if(orglList==null) {
            return curList;
        }
        Set<String> orglSet = new HashSet<String>();
        for(OrgPost per:orglList) {
            orglSet.add(per.getPostId()+"_"+per.getOrgId());
        }
        List<OrgPost> rtList = new ArrayList<OrgPost>();
        for(OrgPost per: curList) {
            //遍历当前的
            String key = per.getPostId()+"_"+per.getOrgId();
            if(!orglSet.contains(key)) {
                //原来的不包含当前的，则为新增
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生删除的机构岗位
    private List<OrgPost> genDelOrgPostList(List<OrgPost> orglList,List<OrgPost> curList) {
        if(orglList==null) {
            return null;
        }
        if(curList==null) {
            return orglList;
        }
        Set<String> curSet = new HashSet<String>();
        for(OrgPost per:curList) {
            curSet.add(per.getPostId()+"_"+per.getOrgId());
        }
        List<OrgPost> rtList = new ArrayList<OrgPost>();
        for(OrgPost per: orglList) {
            //遍历原来的
            String key = per.getPostId()+"_"+per.getOrgId();
            if(!curSet.contains(key)) {
                //当前的不包含原来的，则为删除
                rtList.add(per);
            }
        }
        return rtList;
    }

    private List<PostRole> transPostRole(Integer postId,List<Integer> roleIdList) {
        if(roleIdList==null || roleIdList.size()<=0) {
            return null;
        }
        Date now = new Date();
        List<PostRole> postRoleList = new ArrayList<PostRole>();
        for(Integer roleId:roleIdList) {
            PostRole postRole = new PostRole();
            postRole.setCreateTime(now);
            postRole.setModifyTime(now);
            postRole.setPostId(postId);
            postRole.setRoleId(roleId);

            postRoleList.add(postRole);
        }
        return postRoleList;
    }

    private List<OrgPost> transOrgPost(Integer postId, List<Integer> orgIdList) {
        if(orgIdList==null || orgIdList.size()<=0) {
            return null;
        }
        Date now = new Date();
        List<OrgPost> orgPostList = new ArrayList<OrgPost>();
        for(Integer orgId:orgIdList) {
            OrgPost orgPost = new OrgPost();
            orgPost.setCreateTime(now);
            orgPost.setModifyTime(now);
            orgPost.setPostId(postId);
            orgPost.setOrgId(orgId);

            orgPostList.add(orgPost);
        }
        return orgPostList;
    }

    //查询可选岗位
    @Override
    public List<Post> listOptionalPost() {
        PostExample example = new PostExample();
        PostExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo(0);
        List<Post> list = mapper.selectByExample(example);
        return list;
    }

    //查询没机构的岗位
    @Override
    public List<Post> listPostNotOrg() {
        List<Post> list = postDao.listPostNotOrg();
        return list;
    }

    //根据机构查询关联的岗位
    @Override
    public List<Post> listPostByOrg(Integer orgId) {
        List<Post> list = postDao.listPostByOrg(orgId);
        return list;
    }

    //根据用户查询关联的岗位
    @Override
    public List<Post> listPostByUser(Integer userId) {
        List<Post> list = postDao.listPostByUser(userId);
        return list;
    }
}