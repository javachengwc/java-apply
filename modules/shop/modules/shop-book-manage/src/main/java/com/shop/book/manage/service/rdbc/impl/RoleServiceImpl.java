package com.shop.book.manage.service.rdbc.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.book.manage.dao.RoleDao;
import com.shop.book.manage.dao.mapper.RoleMapper;
import com.shop.book.manage.model.pojo.Role;
import com.shop.book.manage.model.pojo.RoleExample;
import com.shop.book.manage.model.vo.RoleQueryVo;
import com.shop.book.manage.model.vo.RoleVo;
import com.shop.book.manage.service.rdbc.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//service层返回的list都可能为空
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleDao roleDao;

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

    public Integer uptRole(Role role) {
        int rt = roleMapper.updateByPrimaryKeySelective(role);
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
        boolean rt =role==null?true:false;
        return rt;
    }

    public boolean hasExistRoleCode(String code) {
        Role role=queryByCode(code);
        boolean rt =role==null?true:false;
        return rt;
    }

    public List<RoleVo> queryUserRole(Long userId) {
        List<RoleVo> list = roleDao.queryByUserId(userId);
        return  list;
    }
}
