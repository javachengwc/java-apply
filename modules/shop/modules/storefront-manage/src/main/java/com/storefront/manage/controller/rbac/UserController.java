package com.storefront.manage.controller.rbac;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.util.TransUtil;
import com.storefront.manage.model.pojo.rbac.Role;
import com.storefront.manage.model.pojo.rbac.User;
import com.storefront.manage.model.vo.UserQueryVo;
import com.storefront.manage.model.vo.UserVo;
import com.storefront.manage.service.rbac.UserService;
import com.util.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api(value = "用户相关接口", description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "分页查询用户", notes = "分页查询用户")
    @RequiresPermissions("user:list")
    @PostMapping("/queryPage")
    public Resp<Page<UserVo>> queryPage(@Validated @RequestBody Req<UserQueryVo> req, Errors errors) {
        UserQueryVo queryVo = req.getData();
        String endDateStr = queryVo.getEndDate();
        if(StringUtils.isNotBlank(endDateStr)){
            Date endDate = DateUtil.getDate(endDateStr,DateUtil.FMT_YMD);
            Date nextDate = DateUtil.addDates(endDate,1);
            queryVo.setEndDate(DateUtil.formatDate(nextDate,DateUtil.FMT_YMD));
        }
        queryVo.genPage();

        Page<UserVo> page= userService.queryPage(queryVo);

        Resp<Page<UserVo>> resp = Resp.success(page,"成功");
        return  resp;
    }

    @ApiOperation(value = "根据用户ID查询用户", notes = "根据用户ID查询用户")
    @RequiresPermissions("user:query")
    @PostMapping("/getById")
    public Resp<UserVo> getById(@Validated @RequestBody Req<Long> req) {
        Long userId = req.getData();
        if(userId==null ) {
            Resp<UserVo> resp = Resp.error("参数验证失败");
            return  resp;
        }
        User user = userService.getById(userId);
        if (user == null) {
            Resp<UserVo> resp = Resp.error("用户不存在");
            return  resp;
        }
        UserVo userVo =TransUtil.transEntity(user,UserVo.class);
        userVo.setCreateTimeStr(DateUtil.formatDate(user.getCreateTime(),DateUtil.FMT_YMD_HMS));
        userVo.setModifiedTimeStr(DateUtil.formatDate(user.getModifiedTime(),DateUtil.FMT_YMD_HMS));
        List<Role> roleList = userService.queryRoleByUser(userId);
        if(roleList!=null) {
            for(Role role:roleList) {
                userVo.getRoleIds().add(role.getId());
            }
        }
        Resp<UserVo> resp = Resp.success(userVo,"成功");
        return  resp;
    }

    @ApiOperation(value = "新增用户", notes = "新增用户")
    @RequiresPermissions("user:add")
    @PostMapping("/add")
    public Resp<Long> add(@Validated @RequestBody Req<UserVo> req) {
        UserVo userVo = req.getData();
        if(userVo==null) {
            Resp<Long> resp = Resp.error("参数验证失败");
            return  resp;
        }
        User user = userService.addUserWithRole(userVo);
        Long userId = user.getId();
        Resp<Long> resp = Resp.success(userId,"成功");
        return  resp;
    }

    @ApiOperation(value = "修改用户", notes = "修改用户")
    @RequiresPermissions("user:update")
    @PostMapping("/update")
    public Resp<Void> update(@Validated @RequestBody Req<UserVo> req) {
        UserVo userVo = req.getData();
        if(userVo==null || userVo.getId()==null) {
            Resp<Void> resp = Resp.error("参数验证失败");
            return  resp;
        }
        userService.uptUserWithRole(userVo);

        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }

    @ApiOperation(value = "禁用用户", notes = "禁用用户")
    @RequiresPermissions("user:disable")
    @PostMapping("/disable")
    public Resp<Void> disable(@Validated @RequestBody Req<Long> req) {
        Long userId = req.getData();
        if(userId==null) {
            Resp<Void> resp = Resp.error("参数验证失败");
            return  resp;
        }
        userService.disable(userId);
        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }

    @ApiOperation(value = "启用用户", notes = "启用用户")
    @RequiresPermissions("user:enable")
    @PostMapping("/enable")
    public Resp<Void> enable(@Validated @RequestBody Req<Long> req) {
        Long userId = req.getData();
        if(userId==null) {
            Resp<Void> resp = Resp.error("参数验证失败");
            return  resp;
        }
        userService.enable(userId);
        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }

}
