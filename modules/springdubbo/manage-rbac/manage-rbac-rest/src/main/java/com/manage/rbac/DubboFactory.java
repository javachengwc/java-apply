package com.manage.rbac;

import com.manage.rbac.model.common.Constant;
import com.manage.rbac.provider.*;
import lombok.Data;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
@Data
public class DubboFactory {

    @Reference(version = Constant.DUBBO_API_VERSION)
    private ICrowdProvider crowdProvider;

    @Reference(version = Constant.DUBBO_API_VERSION)
    private IUserProvider userProvider;

    @Reference(version = Constant.DUBBO_API_VERSION)
    private IMenuProvider menuProvider;

    @Reference(version = Constant.DUBBO_API_VERSION)
    private IOrganizationProvider orgProvider;

    @Reference(version = Constant.DUBBO_API_VERSION)
    private IPostProvider postProvider;

    @Reference(version = Constant.DUBBO_API_VERSION)
    private IRoleProvider roleProvider;

    @Reference(version = Constant.DUBBO_API_VERSION)
    private ISystemProvider systemProvider;

    @Reference(version = Constant.DUBBO_API_VERSION)
    private ITokenProvider tokenProvider;
}
