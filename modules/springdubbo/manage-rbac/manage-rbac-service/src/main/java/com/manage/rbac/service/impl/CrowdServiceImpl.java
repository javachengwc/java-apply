package com.manage.rbac.service.impl;

import com.manage.rbac.dao.CrowdMapper;
import com.manage.rbac.dao.CrowdRoleMapper;
import com.manage.rbac.dao.OrgCrowdMapper;
import com.manage.rbac.dao.ext.CrowdDao;
import com.manage.rbac.entity.*;
import com.manage.rbac.entity.ext.CrowdRoleDO;
import com.manage.rbac.entity.ext.OrgCrowdDO;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.service.IOrganizationService;
import com.manage.rbac.service.IRoleService;
import com.model.base.PageVo;
import com.util.TransUtil;
import com.util.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.rbac.service.ICrowdService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Crowd的服务接口的实现类
 * @author 21
 */
@Slf4j
@Service
public class CrowdServiceImpl implements ICrowdService {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private CrowdDao crowdDao;

    @Autowired
    private CrowdMapper mapper;

    @Autowired
    private CrowdRoleMapper crowdRoleMapper;

    @Autowired
    private OrgCrowdMapper orgCrowdMapper;

    //分页查询用户组
    @Override
    public PageVo<CrowdDTO> listCrowdPage(String name, Integer pageIndex, Integer pageSize) {

        PageVo<CrowdDTO> pageData = new PageVo<CrowdDTO>();
        //查询总条数
        long total = crowdDao.countPage(name);
        int totalCount = new Long(total).intValue();
        pageData.setTotalCount(totalCount);

        Page page = new Page(pageIndex, pageSize, totalCount);
        if (page.isBound()) {
            //数据页越界处理
            return pageData;
        }

        int start = page.getStart();
        List<Crowd> list = crowdDao.listCrowdPage(name,start,pageSize);
        List<CrowdDTO> dtoList = TransUtil.transList(list,CrowdDTO.class);
        this.merginCrowdRelaInfo(dtoList);

        pageData.setList(dtoList);
        return pageData;
    }

    //注入关联信息
    private void  merginCrowdRelaInfo( List<CrowdDTO> dtoList ) {
        if(dtoList==null || dtoList.size()<=0) {
            return;
        }
        List<Integer> crowdIdList = dtoList.stream().map(CrowdDTO::getId).collect(Collectors.toList());
        Map<Integer, List<RoleDTO>> crowdRoleMap = this.queryRoleByCrowdList(crowdIdList);
        if(crowdRoleMap!=null) {
            for(CrowdDTO crowdDTO:dtoList) {
                Integer crowdId = crowdDTO.getId();
                List<RoleDTO> roleList = crowdRoleMap.get(crowdId);
                //注入用户组的角色信息
                crowdDTO.setRoleList(roleList==null?Collections.EMPTY_LIST:roleList);
            }
        }
        Map<Integer, List<OrganizationDTO>> orgCrowdMap = this.queryOrgByCrowdList(crowdIdList);
        if(orgCrowdMap!=null) {
            for(CrowdDTO crowdDTO:dtoList) {
                Integer crowdId = crowdDTO.getId();
                List<OrganizationDTO> orgList = orgCrowdMap.get(crowdId);
                //注入用户组的机构信息
                crowdDTO.setOrgList(orgList==null?Collections.EMPTY_LIST:orgList);
            }
        }
    }

    //分页查询不包含系统角色的用户组
    @Override
    public PageVo<CrowdDTO> listCrowdNoSysPage(String name, Integer pageIndex, Integer pageSize) {

        PageVo<CrowdDTO> pageData = new PageVo<CrowdDTO>();
        //查询总条数
        long total = crowdDao.countNoSysPage(name);
        int totalCount = new Long(total).intValue();
        pageData.setTotalCount(totalCount);

        Page page = new Page(pageIndex, pageSize, totalCount);
        if (page.isBound()) {
            //数据页越界处理
            return pageData;
        }

        int start = page.getStart();
        List<Crowd> list = crowdDao.listCrowdNoSysPage(name,start,pageSize);
        List<CrowdDTO> dtoList = TransUtil.transList(list,CrowdDTO.class);
        this.merginCrowdRelaInfo(dtoList);

        pageData.setList(dtoList);
        return pageData;
    }

    private  Map<Integer, List<RoleDTO>> queryRoleByCrowdList( List<Integer> crowdIdList) {
        if(crowdIdList==null || crowdIdList.size()<=0) {
            return null;
        }
        List<CrowdRoleDO> list = crowdDao.listCrowdRoleByCrowds(crowdIdList);
        if(list==null || list.size()<=0) {
            return null;
        }
        Map<Integer, List<RoleDTO>> crowdRoleMap = new HashMap<Integer, List<RoleDTO>>();
        for(CrowdRoleDO per:list) {
            Integer crowdId = per.getCrowdId();
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(per.getRoleId());
            roleDTO.setName(per.getRoleName());

            List<RoleDTO> roleList = crowdRoleMap.get(crowdId);
            if(roleList==null) {
                roleList= new ArrayList<RoleDTO>();
                crowdRoleMap.put(crowdId,roleList);
            }
            roleList.add(roleDTO);
        }
        return crowdRoleMap;
    }

    private  Map<Integer, List<OrganizationDTO>> queryOrgByCrowdList( List<Integer> crowdIdList) {
        if(crowdIdList==null || crowdIdList.size()<=0) {
            return null;
        }
        List<OrgCrowdDO> list = crowdDao.listOrgCrowdByCrowds(crowdIdList);
        if(list==null || list.size()<=0) {
            return null;
        }
        Map<Integer, List<OrganizationDTO>> orgCrowdMap = new HashMap<Integer, List<OrganizationDTO>>();
        for(OrgCrowdDO per:list) {
            Integer crowdId = per.getCrowdId();
            OrganizationDTO orgDTO = new OrganizationDTO();
            orgDTO.setId(per.getOrgId());
            orgDTO.setName(per.getOrgName());

            List<OrganizationDTO> orgList = orgCrowdMap.get(crowdId);
            if(orgList==null) {
                orgList= new ArrayList<OrganizationDTO>();
                orgCrowdMap.put(crowdId,orgList);
            }
            orgList.add(orgDTO);
        }
        return orgCrowdMap;
    }

    //查询用户组详情
    @Override
    public CrowdDTO getCrowdDetail(Integer id) {
        Crowd crowd =mapper.selectByPrimaryKey(id);
        if(crowd==null) {
            return null;
        }
        CrowdDTO crowdDTO = TransUtil.transEntity(crowd,CrowdDTO.class);

        List<Organization> oList = organizationService.listOrgByCrowd(id);
        List<OrganizationDTO> orgList = TransUtil.transList(oList,OrganizationDTO.class);
        crowdDTO.setOrgList(orgList);

        List<Role> rList = roleService.listRoleByCrowd(id);
        List<RoleDTO> roleList = TransUtil.transList(rList,RoleDTO.class);
        crowdDTO.setRoleList(roleList);

        return crowdDTO;
    }

    //根据名称查询数量
    @Override
    public long countByName(String name) {
        CrowdExample example = new CrowdExample();
        CrowdExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNoneBlank(name)) {
            criteria.andNameEqualTo(name);
        }
        long count = mapper.countByExample(example);
        return count;
    }

    //增加用户组
    @Transactional
    @Override
    public boolean addCrowd(CrowdDTO crowdDTO) {
        String name = crowdDTO.getName();
        log.info("CrowdServiceImpl addCrowd start,name={}",name);

        Crowd crowd = TransUtil.transEntity(crowdDTO,Crowd.class);
        List<OrganizationDTO> oList = crowdDTO.getOrgList();
        List<RoleDTO> rList = crowdDTO.getRoleList();

        List<Integer> orgIdList =oList==null?null: oList.stream().map(OrganizationDTO::getId).collect(Collectors.toList());
        List<Integer> roleList = rList==null?null: rList.stream().map(RoleDTO::getId).collect(Collectors.toList());

        OperatorDTO operator = crowdDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        crowd.setCreateTime(now);
        crowd.setModifyTime(now);
        crowd.setCreaterId(operatorId);
        crowd.setCreaterNickname(operatorNickname);
        crowd.setOperatorId(operatorId);
        crowd.setOperatorNickname(operatorNickname);

        mapper.insertSelective(crowd);
        Integer crowdId =crowd.getId();
        List<OrgCrowd> ocList = this.transOrgCrowd(crowdId,orgIdList);
        if( ocList!=null) {
            this.addBatchOrgCrowd(ocList);
        }
        List<CrowdRole> crList = this.transCrowdRole(crowdId,roleList);
        if( crList!=null) {
            this.addBatchCrowdRole(crList);
        }
        return true;
    }

    private Integer addBatchCrowdRole(List<CrowdRole> list) {
        Integer rt=0;
        for(CrowdRole per:list) {
            rt+=crowdRoleMapper.insertSelective(per);
        }
        return rt;
    }

    private Integer addBatchOrgCrowd(List<OrgCrowd> list) {
        Integer rt=0;
        for(OrgCrowd per:list) {
            rt+=orgCrowdMapper.insertSelective(per);
        }
        return rt;
    }

    private List<OrgCrowd> transOrgCrowd(Integer crowdId,List<Integer> orgIdList) {
        if(orgIdList==null || orgIdList.size()<=0) {
            return null;
        }
        Date now = new Date();
        List<OrgCrowd> list = new ArrayList<OrgCrowd>();
        for(Integer orgId:orgIdList) {
            OrgCrowd orgCrowd = new OrgCrowd();
            orgCrowd.setCreateTime(now);
            orgCrowd.setModifyTime(now);
            orgCrowd.setOrgId(orgId);
            orgCrowd.setCrowdId(crowdId);

            list.add(orgCrowd);
        }
        return list;
    }

    private List<CrowdRole> transCrowdRole(Integer crowdId, List<Integer> roleList) {
        if(roleList==null || roleList.size()<=0) {
            return null;
        }
        Date now = new Date();
        List<CrowdRole> list = new ArrayList<CrowdRole>();
        for(Integer roleId:roleList) {
            CrowdRole crowdRole = new CrowdRole();
            crowdRole.setCreateTime(now);
            crowdRole.setModifyTime(now);
            crowdRole.setCrowdId(crowdId);
            crowdRole.setRoleId(roleId);

            list.add(crowdRole);
        }
        return list;
    }

    //修改用户组
    @Transactional
    @Override
    public boolean updateCrowd(CrowdDTO crowdDTO) {
        Integer crowdId = crowdDTO.getId();
        log.info("CrowdServiceImpl updateCrowd start,crowdId={}",crowdId);

        Crowd crowd = TransUtil.transEntity(crowdDTO,Crowd.class);
        List<OrganizationDTO> oList = crowdDTO.getOrgList();
        List<RoleDTO> rList = crowdDTO.getRoleList();

        List<Integer> orgIdList =oList==null?null: oList.stream().map(OrganizationDTO::getId).collect(Collectors.toList());
        List<Integer> roleList = rList==null?null: rList.stream().map(RoleDTO::getId).collect(Collectors.toList());

        OperatorDTO operator = crowdDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        crowd.setModifyTime(now);
        crowd.setOperatorId(operatorId);
        crowd.setOperatorNickname(operatorNickname);
        mapper.updateByPrimaryKeySelective(crowd);

        List<OrgCrowd> orglOcList =this.listOrgCrowd(crowdId);
        List<OrgCrowd> curOcList = this.transOrgCrowd(crowdId,orgIdList);
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

        List<CrowdRole> orglCrList =this.listCrowdRole(crowdId);
        List<CrowdRole> curCrList = this.transCrowdRole(crowdId,roleList);
        //对比产生新增的用户组角色
        List<CrowdRole> addCrList = this.genAddCrowdRoleList(orglCrList,curCrList);
        //对比产生删除的用户组角色
        List<CrowdRole> delCrList = this.genDelCrowdRoleList(orglCrList,curCrList);
        if( addCrList!=null) {
            this.addBatchCrowdRole(addCrList);
        }
        if(delCrList!=null) {
            this.deleteBatchCrowdRole(delCrList);
        }
        return true;
    }

    private List<OrgCrowd> listOrgCrowd(Integer crowdId) {
        OrgCrowdExample example = new OrgCrowdExample();
        OrgCrowdExample.Criteria criteria = example.createCriteria();
        criteria.andCrowdIdEqualTo(crowdId);
        List<OrgCrowd> list = orgCrowdMapper.selectByExample(example);
        return list;
    }

    private List<CrowdRole> listCrowdRole(Integer crowdId) {
        CrowdRoleExample example = new CrowdRoleExample();
        CrowdRoleExample.Criteria criteria = example.createCriteria();
        criteria.andCrowdIdEqualTo(crowdId);
        List<CrowdRole> list = crowdRoleMapper.selectByExample(example);
        return list;
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

    private Integer deleteBatchCrowdRole(List<CrowdRole> list) {
        Integer rt=0;
        for(CrowdRole per:list) {
            rt+=this.deleteCrowdRole(per.getCrowdId(),per.getRoleId());
        }
        return rt;
    }

    private Integer deleteCrowdRole(Integer crowdId,Integer roleId) {
        CrowdRoleExample example = new CrowdRoleExample();
        CrowdRoleExample.Criteria criteria = example.createCriteria();
        criteria.andCrowdIdEqualTo(crowdId);
        criteria.andRoleIdEqualTo(roleId);
        Integer rt=crowdRoleMapper.deleteByExample(example);
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

    //对比产生新增的用户组角色
    private List<CrowdRole> genAddCrowdRoleList(List<CrowdRole> orglList,List<CrowdRole> curList) {
        if(curList==null) {
            return null;
        }
        if(orglList==null) {
            return curList;
        }
        Set<String> orglSet = new HashSet<String>();
        for(CrowdRole per:orglList) {
            orglSet.add(per.getCrowdId()+"_"+per.getRoleId());
        }
        List<CrowdRole> rtList = new ArrayList<CrowdRole>();
        for(CrowdRole per: curList) {
            //遍历当前的
            String key = per.getCrowdId()+"_"+per.getRoleId();
            if(!orglSet.contains(key)) {
                //原来的不包含当前的，则为新增
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生删除的用户组角色
    private List<CrowdRole> genDelCrowdRoleList(List<CrowdRole> orglList,List<CrowdRole> curList) {
        if(orglList==null) {
            return null;
        }
        if(curList==null) {
            return orglList;
        }
        Set<String> curSet = new HashSet<String>();
        for(CrowdRole per:curList) {
            curSet.add(per.getCrowdId()+"_"+per.getRoleId());
        }
        List<CrowdRole> rtList = new ArrayList<CrowdRole>();
        for(CrowdRole per: orglList) {
            //遍历原来的
            String key = per.getCrowdId()+"_"+per.getRoleId();
            if(!curSet.contains(key)) {
                //当前的不包含原来的，则为删除
                rtList.add(per);
            }
        }
        return rtList;
    }

    //删除用户组
    @Transactional
    @Override
    public boolean deleteCrowd(Integer id) {
        log.info("CrowdServiceImpl deleteCrowd start,id={}",id);
        mapper.deleteByPrimaryKey(id);
        crowdDao.deleteCrowdRoleByCrowd(id);
        crowdDao.deleteOrgCrowdByCrowd(id);
        crowdDao.deleteUserCrowdByCrowd(id);
        return true;
    }

    //启用用户组
    @Override
    public boolean enableCrowd(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateCrowdState(id, EnableEnum.ENABLE.getValue(),operatorId,operatorNickname);
        return true;
    }

    //禁用用户组
    @Override
    public boolean disableCrowd(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateCrowdState(id,EnableEnum.FORBID.getValue(),operatorId,operatorNickname);
        return true;
    }

    private int updateCrowdState(Integer id,Integer state,Integer operatorId,String operatorNickname) {
        Crowd crowd = new Crowd();
        crowd.setId(id);
        //0--正常，1--禁用
        crowd.setState(state);
        crowd.setOperatorId(operatorId);
        crowd.setOperatorNickname(operatorNickname);
        int rt =mapper.updateByPrimaryKeySelective(crowd);
        return rt;
    }

    //查询可选用户组
    @Override
    public List<Crowd> listOptionalCrowd() {
        CrowdExample example = new CrowdExample();
        CrowdExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo(0);
        List<Crowd> list = mapper.selectByExample(example);
        return list;
    }

    //查询无机构关联的用户组
    public List<Crowd> listCrowdNotOrg() {
        List<Crowd> list = crowdDao.listCrowdNotOrg();
        return list;
    }

    //查询机构关联的用户组
    @Override
    public List<Crowd> listCrowdByOrg(Integer orgId) {
        List<Crowd> list = crowdDao.listCrowdByOrg(orgId);
        return list;
    }

    //查询用户关联的用户组
    @Override
    public List<Crowd> listCrowdByUser(Integer userId) {
        List<Crowd> list = crowdDao.listCrowdByUser(userId);
        return list;
    }
}