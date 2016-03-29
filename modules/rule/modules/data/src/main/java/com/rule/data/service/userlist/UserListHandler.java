package com.rule.data.service.userlist;

import com.alibaba.fastjson.JSON;
import com.rule.data.handler.Handler;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.DataService;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class UserListHandler implements Handler {

    @Override
    public HttpResponse handleReq(String content) throws Throwable {

        UserListReqInfo reqInfo = DataUtil.parse(content, UserListReqInfo.class);

        final String userName = reqInfo.getUserName().trim();
        final Integer page = reqInfo.getPage();
        final Integer size = reqInfo.getSize();
        final Integer start = (page - 1) * size;

        Map<String, Object> param = new HashMap<String, Object>();
        if (userName.length() != 0) {
            param.put("user_name", userName + "%");
        } else {
            param.put("user_name", "");
        }
        param.put("start", start);
        param.put("size", size);

        SerService po = Services.getService(Services.SERVICE_USER_LIST);

        D2Data data = DataService.getD2Data(po, param);

        UserListRespInfo respInfo = new UserListRespInfo();
        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        UserInfo info = null;

        int len = data.getData().length;

        for (int i = 0; i < len; i++) {

            info = new UserInfo();

            Object user_name = data.getValue("用户登录", i);
            Object real_name = data.getValue("用户真实姓名", i);
            Object job_type = data.getValue("职位类型", i);

            if (user_name != null) {
                info.setUserName(String.valueOf(user_name));
            }
            if (real_name != null) {
                info.setRealName(String.valueOf(real_name));
            }
            if (job_type != null) {
                info.setJobType(String.valueOf(job_type));
            }

            userInfoList.add(info);
        }

        respInfo.setUserInfoList(userInfoList);

        String json = JSON.toJSONString(respInfo);

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);

        response.setHeader(CONTENT_TYPE, "application/json");
        response.setContent(ChannelBuffers.wrappedBuffer(json.getBytes()));

        return response;
    }
}
