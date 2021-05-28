package com.manage.rbac.service.impl;

import com.manage.rbac.dao.OrgCrowdMapper;
import com.manage.rbac.dao.OrgPostMapper;
import com.manage.rbac.dao.OrganizationMapper;
import com.manage.rbac.dao.ext.OrgDao;
import com.manage.rbac.entity.*;
import com.manage.rbac.entity.ext.*;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.service.*;
import com.model.base.PageVo;
import com.util.TransUtil;
import com.util.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Organization的服务接口的实现类
 */
@Slf4j
@Service
public class OrganizationServiceImpl implements IOrganizationService {

    @Autowired
    private IPostService postService;

    @Autowired
    private ICrowdService crowdService;

    @Autowired
    private IUserService userService;

    @Autowired
    private OrganizationMapper mapper;

    @Autowired
    private OrgPostMapper orgPostMapper;

    @Autowired
    private OrgCrowdMapper orgCrowdMapper;

    @Autowired
    private OrgDao orgDao;

    //分页查询机构
    @Override
    public PageVo<OrganizationDTO> listOrgPage(String name, Integer pageIndex, Integer pageSize) {

        PageVo<OrganizationDTO> pageData = new PageVo<OrganizationDTO>();
        //查询总条数
        long total = orgDao.countPage(name);
        int totalCount = new Long(total).intValue();
        pageData.setTotalCount(totalCount);

        Page page = new Page(pageIndex, pageSize, totalCount);
        if (page.isBound()) {
            //数据页越界处理
            return pageData;
        }

        int start = page.getStart();
        List<Organization> list = orgDao.listOrgPage(name,start,pageSize);
        //查各机构人数
        List<OrganizationDTO> dtoList = TransUtil.transList(list,OrganizationDTO.class);
        List<Integer> orgIdList = dtoList.stream().map(OrganizationDTO::getId).collect(Collectors.toList());
        Map<Integer, Long> orgPersonCountMap = this.queryPersonCountByOrgList(orgIdList);
        for(OrganizationDTO per:dtoList) {
            Integer orgId = per.getId();
            Long personCount = orgPersonCountMap.get(orgId);
            //注入机构人数
            per.setPersonCount(personCount==null?0:personCount.intValue());
        }

        Map<Integer, List<PostDTO>> orgPostMap = this.queryPostByOrgList(orgIdList);
        if(orgPostMap!=null) {
            for(OrganizationDTO orgDTO:dtoList) {
                Integer orgId = orgDTO.getId();
                List<PostDTO> postList = orgPostMap.get(orgId);
                //注入机构的岗位信息
                orgDTO.setPostList(postList==null? Collections.EMPTY_LIST:postList);
            }
        }
        Map<Integer, List<CrowdDTO>> orgCrowdMap = this.queryCrowdByOrgList(orgIdList);
        if(orgCrowdMap!=null) {
            for(OrganizationDTO orgDTO:dtoList) {
                Integer orgId = orgDTO.getId();
                List<CrowdDTO> crowdList = orgCrowdMap.get(orgId);
                //注入机构的用户组信息
                orgDTO.setCrowdList(crowdList==null? Collections.EMPTY_LIST:crowdList);
            }
        }

        pageData.setList(dtoList);
        return pageData;
    }

    private  Map<Integer, List<PostDTO>> queryPostByOrgList( List<Integer> orgIdList) {
        if(orgIdList==null || orgIdList.size()<=0) {
            return null;
        }
        List<OrgPostDO> orgPostList = orgDao.listOrgPostByOrgs(orgIdList);
        if(orgPostList==null || orgPostList.size()<=0) {
            return null;
        }
        Map<Integer, List<PostDTO>> orgPostMap = new HashMap<Integer, List<PostDTO>>();
        for(OrgPostDO per:orgPostList) {
            Integer orgId = per.getOrgId();
            PostDTO postDTO = new PostDTO();
            postDTO.setId(per.getPostId());
            postDTO.setName(per.getPostName());

            List<PostDTO> postList = orgPostMap.get(orgId);
            if(postList==null) {
                postList= new ArrayList<PostDTO>();
                orgPostMap.put(orgId,postList);
            }
            postList.add(postDTO);
        }
        return orgPostMap;
    }

    private  Map<Integer, List<CrowdDTO>> queryCrowdByOrgList( List<Integer> orgIdList) {
        if(orgIdList==null || orgIdList.size()<=0) {
            return null;
        }
        List<OrgCrowdDO> orgCrowdList = orgDao.listOrgCrowdByOrgs(orgIdList);
        if(orgCrowdList==null || orgCrowdList.size()<=0) {
            return null;
        }
        Map<Integer, List<CrowdDTO>> orgCrowdMap = new HashMap<Integer, List<CrowdDTO>>();
        for(OrgCrowdDO per:orgCrowdList) {
            Integer orgId = per.getOrgId();
            CrowdDTO crowdDTO = new CrowdDTO();
            crowdDTO.setId(per.getCrowdId());
            crowdDTO.setName(per.getCrowdName());

            List<CrowdDTO> crowdList = orgCrowdMap.get(orgId);
            if(crowdList==null) {
                crowdList= new ArrayList<CrowdDTO>();
                orgCrowdMap.put(orgId,crowdList);
            }
            crowdList.add(crowdDTO);
        }
        return orgCrowdMap;
    }

    private Map<Integer,Long> queryPersonCountByOrgList(List<Integer> orgIdList) {
        if(orgIdList==null || orgIdList.size()<=0) {
            return Collections.EMPTY_MAP;
        }
        List<OrgPersonCountDO> list  = userService.listOrgPersonCount(orgIdList);
        if(list==null || list.size()<=0) {
            return  Collections.EMPTY_MAP;
        }
        Map<Integer,Long> map = new HashMap<Integer,Long>();
        for(OrgPersonCountDO per:list) {
            map.put(per.getOrgId(),per.getCount());
        }
        return map;
    }

    //查询机构详情
    @Override
    public OrganizationDTO getOrgDetail(Integer id) {
        Organization org =mapper.selectByPrimaryKey(id);
        if(org==null) {
            return null;
        }
        OrganizationDTO orgDTO = TransUtil.transEntity(org,OrganizationDTO.class);

        List<Post> pList =postService.listPostByOrg(id);
        List<PostDTO> postList = TransUtil.transList(pList,PostDTO.class);
        orgDTO.setPostList(postList);

        List<Crowd> cList = crowdService.listCrowdByOrg(id);
        List<CrowdDTO> crowdList = TransUtil.transList(cList,CrowdDTO.class);
        orgDTO.setCrowdList(crowdList);

        return orgDTO;
    }

    //根据名称查询机构数量
    @Override
    public long countByName(String name) {
        OrganizationExample example = new OrganizationExample();
        OrganizationExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(name)) {
            criteria.andNameEqualTo(name);
        }
        long count = mapper.countByExample(example);
        return count;
    }

    //添加机构
    @Transactional
    @Override
    public boolean addOrg(OrganizationDTO orgDTO) {
        String name = orgDTO.getName();
        log.info("OrganizationServiceImpl addOrg start,name={}",name);

        Organization org = TransUtil.transEntity(orgDTO,Organization.class);
        List<PostDTO> pList = orgDTO.getPostList();
        List<CrowdDTO> cList = orgDTO.getCrowdList();

        List<Integer> postIdList =pList==null?null: pList.stream().map(PostDTO::getId).collect(Collectors.toList());
        List<Integer> crowdIdList = cList==null?null: cList.stream().map(CrowdDTO::getId).collect(Collectors.toList());

        OperatorDTO operator = orgDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        org.setCreateTime(now);
        org.setModifyTime(now);
        org.setCreaterId(operatorId);
        org.setCreaterNickname(operatorNickname);
        org.setOperatorId(operatorId);
        org.setOperatorNickname(operatorNickname);

        String prePath="";
        Integer parentId = org.getParentId();
        if(parentId==null) {
            parentId=0;
            org.setParentId(parentId);
        }
        if(parentId<=0) {
            org.setParentName("");
            org.setLevel(1);
            org.setPathName("/"+org.getName());
        }else {
            Organization parentOrg = mapper.selectByPrimaryKey(parentId);
            Integer parentLevel  = parentOrg==null?0:parentOrg.getLevel();
            String parentPathName = parentOrg==null?"":parentOrg.getPathName();
            prePath = parentOrg==null?"":parentOrg.getPath();
            Integer curLevel = parentLevel+1;
            org.setLevel(curLevel);
            org.setPathName(parentPathName+"/"+org.getName());
            org.setParentName(parentOrg==null?"":parentOrg.getName());
        }

        mapper.insertSelective(org);
        Integer orgId =org.getId();
        Organization orgSelf = new Organization();
        orgSelf.setId(orgId);
        orgSelf.setPath(prePath+"/"+orgId);
        mapper.updateByPrimaryKeySelective(orgSelf);

        List<OrgPost> opList = this.transOrgPost(orgId,postIdList);
        if( opList!=null) {
            this.addBatchOrgPost(opList);
        }
        List<OrgCrowd> ocList = this.transOrgCrowd(orgId,crowdIdList);
        if( ocList!=null) {
            this.addBatchOrgCrowd(ocList);
        }
        return true;
    }

    private Integer addBatchOrgPost(List<OrgPost> list) {
        Integer rt=0;
        for(OrgPost per:list) {
            rt+=orgPostMapper.insertSelective(per);
        }
        return rt;
    }

    private List<OrgCrowd> transOrgCrowd(Integer orgId,List<Integer> crowdIdList) {
        if(crowdIdList==null || crowdIdList.size()<=0) {
            return null;
        }
        Date now = new Date();
        List<OrgCrowd> list = new ArrayList<OrgCrowd>();
        for(Integer crowdId:crowdIdList) {
            OrgCrowd orgCrowd = new OrgCrowd();
            orgCrowd.setCreateTime(now);
            orgCrowd.setModifyTime(now);
            orgCrowd.setOrgId(orgId);
            orgCrowd.setCrowdId(crowdId);

            list.add(orgCrowd);
        }
        return list;
    }

    private List<OrgPost> transOrgPost(Integer orgId, List<Integer> postIdList) {
        if(postIdList==null || postIdList.size()<=0) {
            return null;
        }
        Date now = new Date();
        List<OrgPost> list = new ArrayList<OrgPost>();
        for(Integer postId:postIdList) {
            OrgPost orgPost = new OrgPost();
            orgPost.setCreateTime(now);
            orgPost.setModifyTime(now);
            orgPost.setPostId(postId);
            orgPost.setOrgId(orgId);

            list.add(orgPost);
        }
        return list;
    }

    //修改机构
    @Transactional
    @Override
    public boolean updateOrg(OrganizationDTO orgDTO) {
        Integer orgId = orgDTO.getId();
        log.info("OrganizationServiceImpl updateOrg start,orgId={}",orgId);
        Organization org = TransUtil.transEntity(orgDTO,Organization.class);
        List<PostDTO> pList = orgDTO.getPostList();
        List<CrowdDTO> cList = orgDTO.getCrowdList();

        List<Integer> postIdList =pList==null?null: pList.stream().map(PostDTO::getId).collect(Collectors.toList());
        List<Integer> crowdIdList = cList==null?null: cList.stream().map(CrowdDTO::getId).collect(Collectors.toList());

        OperatorDTO operator = orgDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        org.setModifyTime(now);
        org.setOperatorId(operatorId);
        org.setOperatorNickname(operatorNickname);

        Organization orglOrg = mapper.selectByPrimaryKey(orgId);
        String orglPathName = orglOrg.getPathName()==null ?"":orglOrg.getPathName();
        int lastIndex = orglPathName.lastIndexOf("/");
        if(lastIndex<0) {
            org.setPathName("/"+org.getName());
        }else {
            String prePathName = orglPathName.substring(0, lastIndex);
            org.setPathName(prePathName+"/"+org.getName());
        }

        mapper.updateByPrimaryKeySelective(org);

        List<OrgPost> orglOpList =this.listOrgPost(orgId);
        List<OrgPost> curOpList = this.transOrgPost(orgId,postIdList);
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

        List<OrgCrowd> orglOcList =this.listOrgCrowd(orgId);
        List<OrgCrowd> curOcList = this.transOrgCrowd(orgId,crowdIdList);
        //对比产生新增的机构用户组
        List<OrgCrowd> addOcList = this.genAddOrgCrowdList(orglOcList,curOcList);
        //对比产生删除的机构用户组
        List<OrgCrowd> delOcList = this.genDelOrgCrowdList(orglOcList,curOcList);
        if( addOcList!=null) {
            this.addBatchOrgCrowd(addOcList);
        }
        if(delOcList!=null) {
            this.deleteBatchOrgCrowd(delOcList);
        }
        return true;
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

    private Integer deleteBatchOrgCrowd(List<OrgCrowd> list) {
        Integer rt=0;
        for(OrgCrowd per:list) {
            rt+=this.deleteOrgCrowd(per.getOrgId(),per.getCrowdId());
        }
        return rt;
    }

    private Integer deleteOrgCrowd(Integer orgId,Integer crowdId) {
        OrgCrowdExample example = new OrgCrowdExample();
        OrgCrowdExample.Criteria criteria = example.createCriteria();
        criteria.andOrgIdEqualTo(orgId);
        criteria.andCrowdIdEqualTo(crowdId);
        Integer rt=orgCrowdMapper.deleteByExample(example);
        return rt;
    }

    private List<OrgCrowd> listOrgCrowd(Integer orgId) {
        OrgCrowdExample example = new OrgCrowdExample();
        OrgCrowdExample.Criteria criteria = example.createCriteria();
        criteria.andOrgIdEqualTo(orgId);
        List<OrgCrowd> list = orgCrowdMapper.selectByExample(example);
        return list;
    }

    private List<OrgPost> listOrgPost(Integer orgId) {
        OrgPostExample example = new OrgPostExample();
        OrgPostExample.Criteria criteria = example.createCriteria();
        criteria.andOrgIdEqualTo(orgId);
        List<OrgPost> list = orgPostMapper.selectByExample(example);
        return list;
    }

    public Integer addBatchOrgCrowd(List<OrgCrowd> list) {
        Integer rt=0;
        for(OrgCrowd per:list) {
            rt+=orgCrowdMapper.insertSelective(per);
        }
        return rt;
    }

    //对比产生新增的机构用户组
    private List<OrgCrowd> genAddOrgCrowdList(List<OrgCrowd> orglList,List<OrgCrowd> curList) {
        if(curList==null) {
            return null;
        }
        if(orglList==null) {
            return curList;
        }
        Set<String> orglSet = new HashSet<String>();
        for(OrgCrowd per:orglList) {
            orglSet.add(per.getOrgId()+"_"+per.getCrowdId());
        }
        List<OrgCrowd> rtList = new ArrayList<OrgCrowd>();
        for(OrgCrowd per: curList) {
            //遍历当前的
            String key = per.getOrgId()+"_"+per.getCrowdId();
            if(!orglSet.contains(key)) {
                //原来的不包含当前的，则为新增
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生删除的机构用户组
    private List<OrgCrowd> genDelOrgCrowdList(List<OrgCrowd> orglList,List<OrgCrowd> curList) {
        if(orglList==null) {
            return null;
        }
        if(curList==null) {
            return orglList;
        }
        Set<String> curSet = new HashSet<String>();
        for(OrgCrowd per:curList) {
            curSet.add(per.getOrgId()+"_"+per.getCrowdId());
        }
        List<OrgCrowd> rtList = new ArrayList<OrgCrowd>();
        for(OrgCrowd per: orglList) {
            //遍历原来的
            String key = per.getOrgId()+"_"+per.getCrowdId();
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

    //启用机构
    @Override
    public boolean enableOrg(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateOrgState(id, EnableEnum.ENABLE.getValue(),operatorId,operatorNickname);
        return true;
    }

    //禁用机构
    @Override
    public boolean disableOrg(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateOrgState(id,EnableEnum.FORBID.getValue(),operatorId,operatorNickname);
        return true;
    }

    private int updateOrgState(Integer id,Integer state,Integer operatorId,String operatorNickname) {
        Organization org = new Organization();
        org.setId(id);
        //0--正常，1--禁用
        org.setState(state);
        org.setOperatorId(operatorId);
        org.setOperatorNickname(operatorNickname);
        org.setModifyTime(new Date());
        int rt =mapper.updateByPrimaryKeySelective(org);
        return rt;
    }

    //查询所有启用的机构
    @Override
    public List<Organization> listAbleOrg() {
        List<Organization> list =this.ableOrgList();
        return list;
    }

    //查询可选机构列表
    @Override
    public List<Organization> listOptionalOrg() {
        List<Organization> list =this.ableOrgList();
        return list;
    }

    private  List<Organization> ableOrgList() {
        OrganizationExample example = new OrganizationExample();
        OrganizationExample.Criteria criteria = example.createCriteria();
        //0--正常，1--禁用
        criteria.andStateEqualTo(0);
        example.setOrderByClause(" sort asc,id desc ");
        List<Organization> list = mapper.selectByExample(example);
        return list;
    }

    //查询岗位可属机构列表
    @Override
    public List<Organization> listOptionalOrgByPost(Integer postId) {
        List<Organization> list = orgDao.listOrgNotRelaPost(postId);
        return list;
    }

    //查询用户组可属机构列表
    @Override
    public List<Organization> listOptionalOrgByCrowd(Integer crowdId) {
        List<Organization> list = orgDao.listOrgNotRelaCrowd(crowdId);
        return list;
    }

    //查询岗位关联的机构
    @Override
    public List<Organization> listOrgByPost(Integer postId) {
        List<Organization> list = orgDao.listOrgByPost(postId);
        return list;
    }

    //查询用户组关联的机构
    @Override
    public List<Organization> listOrgByCrowd(Integer crowdId) {
        List<Organization> list = orgDao.listOrgByCrowd(crowdId);
        return list;
    }

    //根据名称查询机构列表
    @Override
    public List<Organization> listOrgByName(String name) {
        List<Organization> list = orgDao.listOrgByName(name);
        return list;
    }

    public Organization getById(Integer id) {
        Organization org = mapper.selectByPrimaryKey(id);
        return org;
    }
}