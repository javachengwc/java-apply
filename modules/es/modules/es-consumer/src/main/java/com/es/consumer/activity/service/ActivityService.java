package com.es.consumer.activity.service;

import com.alibaba.fastjson.JSON;
import com.es.consumer.activity.model.ActivityIndex;
import com.es.consumer.base.config.IndexEnum;
import com.es.consumer.base.config.SettingConfig;
import com.es.consumer.base.service.MessageService;
import com.es.consumer.base.thread.DealExecutor;
import com.es.consumer.es.EsService;
import com.es.consumer.util.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ActivityService implements MessageService {

    public static Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private EsService esService;

    @Override
    public void handleMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        String[] strs = message.split(",");

        String header = strs[0];
        String[] items = LogUtil.formatLogHeader(header);
        String statDate = items[0];
        String hour = items[1];

        String uid = strs[4];
        String ip = strs[5];
        String imei = strs[6];

        ActivityIndex index = new ActivityIndex();
        index.setId(UUID.randomUUID().toString());
        index.setStatDate(statDate);
        index.setHour(hour);
        index.setUid(uid);
        index.setIp(ip);
        if (StringUtils.isNotEmpty(imei)) {
            index.setImei(imei.replace("-", ""));
        }
        index.setCreateTime(new Date().getTime());
        String data = JSON.toJSONString(index);

        String docId=null;
        if(!SettingConfig.autoId) {
            //id非自动生成，设置记录id为文档id
            docId= index.getId();
        }
        DealExecutor.getInstance().insert(esService, data, IndexEnum.ACTIVITY.getIndex(), IndexEnum.ACTIVITY.getType(), docId);
    }
}
