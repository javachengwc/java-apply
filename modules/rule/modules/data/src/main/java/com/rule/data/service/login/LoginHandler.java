package com.rule.data.service.login;

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

import java.util.HashMap;
import java.util.Map;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class LoginHandler implements Handler {

    @Override
    public HttpResponse handleReq(String content) throws Throwable {
        LoginReqInfo reqInfo = DataUtil.parse(content, LoginReqInfo.class);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_name", reqInfo.getUserName());
        final SerService po = Services.getService(Services.SERVICE_USER_INFO);
        D2Data data = DataService.getD2Data(po, param);
        Object password = data.getValue("密码", 0);
        Object groupID = data.getValue("组ID", 0);

        if (password == null) {
            throw new IllegalArgumentException("用户不存在, " + reqInfo.getUserName());
        }

        String encryptedPassword = DataUtil.getEncryptedPassword(reqInfo.getPassword());

        if (!encryptedPassword.equalsIgnoreCase(password.toString())) {
            throw new IllegalArgumentException("密码错误");
        }


        LoginRespInfo respInfo = new LoginRespInfo();
        respInfo.setGroupID(String.valueOf(groupID));
        String json = JSON.toJSONString(respInfo);

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        response.setHeader(CONTENT_TYPE, "application/json");
        response.setContent(ChannelBuffers.wrappedBuffer(json.getBytes()));

        return response;
    }
}
