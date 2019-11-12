package com.micro.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.micro.course.dao.UserCourseDao;
import com.micro.course.dao.mapper.UserCourseMapper;
import com.micro.course.model.pojo.UserCourse;
import com.micro.course.model.vo.UserCourseQueryVo;
import com.micro.course.model.vo.UserCourseVo;
import com.micro.course.service.UserCourseService;
import com.model.base.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCourseServiceImpl extends ServiceImpl<UserCourseMapper, UserCourse> implements UserCourseService {

    @Autowired
    private UserCourseDao userCourseDao;

    public PageVo<UserCourseVo> queryPage(UserCourseQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<UserCourseVo> list = userCourseDao.queryPage(queryVo);
        PageInfo<UserCourseVo> pageInfo = new PageInfo<UserCourseVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        PageVo<UserCourseVo> page = new PageVo<UserCourseVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }
}
