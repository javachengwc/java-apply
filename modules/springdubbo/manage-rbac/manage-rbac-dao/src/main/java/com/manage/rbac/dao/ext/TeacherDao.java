package com.manage.rbac.dao.ext;

import com.manage.rbac.entity.ext.TeacherDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherDao {

    public List<TeacherDO> listTeacherPage(@Param("teacherId") Integer teacherId, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    public long countPage(@Param("teacherId") Integer teacherId);

    public List<TeacherDO> listTeacherByIds(@Param("idList") List<Integer> idList);

    public List<TeacherDO> listTeacherWithRoleByIds(@Param("idList") List<Integer> idList);

    public List<TeacherDO> listTeacherByNameOrNickName(@Param("name") String name);

    public List<TeacherDO> listTeacherByOrgId(@Param("orgId") Integer orgId);

}
