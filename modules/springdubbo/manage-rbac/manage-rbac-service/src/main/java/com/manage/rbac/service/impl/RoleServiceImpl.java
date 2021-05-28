package com.manage.rbac.service.impl;

import com.manage.rbac.dao.RoleMapper;
import com.manage.rbac.dao.ext.RoleDao;
import com.manage.rbac.entity.RoleExample;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.model.base.PageVo;
import com.util.TransUtil;
import com.util.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.rbac.entity.Role;
import com.manage.rbac.service.IRoleService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Role的服务接口的实现类
 * @author
 */
@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMapper mapper;

    //分页查询角色
    @Override
    public PageVo<RoleDTO> listRolePage(Integer pageIndex, Integer pageSize) {

        PageVo<RoleDTO> pageData = new PageVo<RoleDTO>();
        //查询总条数
        long total = this.countAll();
        int totalCount = new Long(total).intValue();
        pageData.setTotalCount(totalCount);

        Page page = new Page(pageIndex, pageSize, totalCount);
        if (page.isBound()) {
            return pageData;
        }

        int start = page.getStart();
        List<Role> list = roleDao.listRolePage(start,pageSize);
        List<RoleDTO> dtoList = TransUtil.transList(list,RoleDTO.class);

        pageData.setList(dtoList);
        return pageData;
    }

    private long countAll() {
        RoleExample example = new RoleExample();
        long count = mapper.countByExample(example);
        return count;
    }


    //根据名称查询数量
    @Override
    public long countByName(String name) {
        RoleExample example = new RoleExample();
        example.createCriteria().andNameEqualTo(name);
        long count = mapper.countByExample(example);
        return count;
    }

    //根据编码查询数量
    @Override
    public long countByCode(String code) {
        RoleExample example = new RoleExample();
        example.createCriteria().andCodeEqualTo(code);
        long count = mapper.countByExample(example);
        return count;
    }

    @Override
    public boolean addRole(RoleDTO roleDTO) {
        String name = roleDTO.getName();
        log.info("RoleServiceImpl addRole start,name={}",name);
        Role role = TransUtil.transEntity(roleDTO,Role.class);

        OperatorDTO operator = roleDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Date now = new Date();
        role.setCreateTime(now);
        role.setModifyTime(now);
        role.setCreaterId(operatorId);
        role.setCreaterNickname(operatorNickname);
        role.setOperatorId(operatorId);
        role.setOperatorNickname(operatorNickname);

        mapper.insertSelective(role);
        return true;
    }

    @Override
    public boolean updateRole(RoleDTO roleDTO) {
        Integer roleId = roleDTO.getId();
        log.info("RoleServiceImpl updateRole start,roleId={}",roleId);

        OperatorDTO operator = roleDTO.getOperator();
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();

        Role role = TransUtil.transEntity(roleDTO,Role.class);
        Date now = new Date();
        role.setModifyTime(now);
        role.setOperatorId(operatorId);
        role.setOperatorNickname(operatorNickname);

        mapper.updateByPrimaryKeySelective(role);
        return true;
    }

    //启用角色
    @Override
    public boolean enableRole(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateRoleState(id, EnableEnum.ENABLE.getValue(),operatorId,operatorNickname);
        return true;
    }

    //禁用角色
    @Override
    public boolean disableRole(Integer id,OperatorDTO operator) {
        Integer operatorId = operator==null? null: operator.getOperatorId();
        String operatorNickname = operator==null? "": operator.getOperatorNickname();
        this.updateRoleState(id, EnableEnum.FORBID.getValue(),operatorId,operatorNickname);
        return true;
    }

    private int updateRoleState(Integer id,Integer state,Integer operatorId ,String operatorNickname) {
        Role role = new Role();
        role.setId(id);
        //0--正常，1--禁用
        role.setState(state);
        role.setOperatorId(operatorId);
        role.setOperatorNickname(operatorNickname);
        int rt =mapper.updateByPrimaryKeySelective(role);
        return rt;
    }

    //查询可选角色
    @Override
    public List<Role> listOptionalRole() {
        RoleExample example = new RoleExample();
        example.createCriteria().andStateEqualTo(0);
        List<Role> list = mapper.selectByExample(example);
        return list;
    }

    //修改角色菜单
    @Transactional
    @Override
    public boolean updateRoleMenu(RoleMenuDTO roleMenuDTO) {
        Integer roleId = roleMenuDTO.getRoleId();
        List<Integer> menuList = roleMenuDTO.getMenuList();
        log.info("MenuServiceImpl updateRoleMenu start,roleId={}",roleId);
        //先删除以前的
        roleDao.deleteRoleMenuByRole(roleId);
        if(menuList==null || menuList.size()<=0) {
            return Boolean.TRUE;
        }
        //再添加现在的
        roleDao.addRoleMenu(roleId,menuList);
        return Boolean.TRUE;
    }

    //查询岗位的可选角色列表
    @Override
    public List<Role> listOptionalRoleByPost(Integer postId) {
        List<Role> list = roleDao.listRoleNotRelaPost(postId);
        return list;
    }

    //查询用户组可选角色列表
    @Override
    public List<Role> listOptionalRoleByCrowd(Integer crowdId) {
        List<Role> list = roleDao.listRoleNotRelaCrowd(crowdId);
        return list;
    }

    //查询岗位关联的角色
    @Override
    public List<Role> listRoleByPost(Integer postId) {
        List<Role> list = roleDao.listRoleByPost(postId);
        return list;
    }


    //查询用户组关联的角色
    @Override
    public List<Role> listRoleByCrowd(Integer crowdId) {
        List<Role> list = roleDao.listRoleByCrowd(crowdId);
        return list;
    }

    //根据用户岗位查询用户拥有的系统角色数量
    @Override
    public long countSysRoleByUserPost(Integer userId) {
        long count = roleDao.countSysRoleByUserPost(userId);
        return count;
    }

    //根据用户用户组查询用户拥有的系统角色数量
    @Override
    public long countSysRoleByUserCrowd(Integer userId) {
        long count = roleDao.countSysRoleByUserCrowd(userId);
        return count;
    }

    @Override
    public long countOrgAdminRoleByUserPost(Integer id) {
        long count = roleDao.countOrgAdminRoleByUserPost(id);
        return count;
    }

    @Override
    public long countOrgAdminRoleByUserCrowd(Integer id) {
        long count = roleDao.countOrgAdminRoleByUserCrowd(id);
        return count;
    }

    @Override
    public Role getById(Integer id) {
        Role role =mapper.selectByPrimaryKey(id);
        return  role;
    }
}