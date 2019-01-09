package com.shop.book.manage.service.rdbc.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.base.util.TransUtil;
import com.shop.book.manage.config.DbManageConfig;
import com.shop.book.manage.dao.manage.RoleDao;
import com.shop.book.manage.dao.manage.RoleMenuDao;
import com.shop.book.manage.dao.manage.UserRoleDao;
import com.shop.book.manage.dao.manage.mapper.RoleMapper;
import com.shop.book.manage.model.pojo.manage.Role;
import com.shop.book.manage.model.pojo.manage.RoleExample;
import com.shop.book.manage.model.vo.RoleQueryVo;
import com.shop.book.manage.model.vo.RoleVo;
import com.shop.book.manage.service.rdbc.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

//service层返回的list都可能为空
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private UserRoleDao userRoleDao;

    public Role getById(Long id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        return role;
    }

    public List<Role> queryAll() {
        RoleExample example = new RoleExample();
        List<Role> list = roleMapper.selectByExample(example);
        return list;
    }

    public Role queryByCode(String code) {
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code);

        List<Role> list = roleMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    public Role queryByName(String name) {
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);

        List<Role> list = roleMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    public Role addRole(Role role) {
        roleMapper.insertSelective(role);
        return role;
    }

    //添加角色，顺带添加角色菜单
    @Transactional(value = DbManageConfig.MANAGE_TRANSACTION_MANAGER_NAME)
    public Role addRoleWithMenu(RoleVo roleVo) {
        Date now = new Date();
        Role role = TransUtil.transEntity(roleVo,Role.class);
        role.setCreateTime(now);
        role.setModifiedTime(now);
        this.addRole(role);
        Long roleId = role.getId();

        List<Long> menuIds = roleVo.getMenuIds();
        if(menuIds!=null && menuIds.size()>0) {
            roleMenuDao.addRoleMenus(roleId,menuIds.toArray(new Long [menuIds.size()]));
        }
        return role;
    }

    public Integer uptRole(Role role) {
        int rt = roleMapper.updateByPrimaryKeySelective(role);
        return rt;
    }

    //更新角色，顺带更新菜单
    @Transactional(value = DbManageConfig.MANAGE_TRANSACTION_MANAGER_NAME)
    public Integer uptRoleWithMenu(RoleVo roleVo) {
        Date now = new Date();
        Role role = TransUtil.transEntity(roleVo,Role.class);
        role.setModifiedTime(now);
        Integer rt =this.uptRole(role);
        Long roleId = role.getId();

        roleMenuDao.deleteByRole(roleId);
        List<Long> menuIds = roleVo.getMenuIds();
        if(menuIds!=null && menuIds.size()>0) {
            roleMenuDao.addRoleMenus(roleId,menuIds.toArray(new Long [menuIds.size()]));
        }
        return rt;
    }

    public Integer delRoles(List<Long> roleIds) {
        if(roleIds==null || roleIds.size()<=0) {
            return 0;
        }
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(roleIds);
        int delCnt = roleMapper.deleteByExample(example);
        return delCnt;
    }

    //删除角色，顺带删除角色菜单
    @Transactional(value = DbManageConfig.MANAGE_TRANSACTION_MANAGER_NAME)
    public Integer delRolesWithMenu(List<Long> roleIds) {
        int rt=this.delRoles(roleIds);
        if(roleIds!=null ) {
            for(Long roleId:roleIds) {
                roleMenuDao.deleteByRole(roleId);
            }
        }
        return rt;
    }

    public Page<RoleVo> queryPage(RoleQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<RoleVo> list = roleDao.queryPage(queryVo);
        PageInfo<RoleVo> pageInfo = new PageInfo<RoleVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<RoleVo> page = new Page<RoleVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }

    public boolean hasExistRoleName(String name) {
        Role role=queryByName(name);
        boolean rt =role==null?false:true;
        return rt;
    }

    public boolean hasExistRoleCode(String code) {
        Role role=queryByCode(code);
        boolean rt =role==null?false:true;
        return rt;
    }

    public List<RoleVo> queryUserRole(Long userId) {
        List<RoleVo> list = roleDao.queryByUserId(userId);
        return  list;
    }

    public boolean hasUser(Long roleId) {
        Integer cnt = userRoleDao.queryUserRoleCountByRole(roleId);
        if(cnt!=null && cnt>0) {
            return true;
        }
        return false;
    }

    //获取角色下的菜单ID
    public List<Long> queryMenuIdsByRole(Long roleId) {
        List<Long> list = roleMenuDao.queryMenuIdsByRole(roleId);
        return list;
    }
}
