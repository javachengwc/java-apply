package com.micro.training.util;

import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApolloClientUtil {

    private static Logger logger= LoggerFactory.getLogger(ApolloClientUtil.class);

    public void demo() {

        //这里没设置命名空间，默认命名空间是application
        Config config = ConfigService.getAppConfig(); //ConfigService.getConfig(Namespace);
        String key = "key";
        String defaultValue = "def"; //默认值
        String value = config.getProperty(key, defaultValue);

        //监听配置修改事件
        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                logger.info("Apollo 配置改变，changes for namespace " + changeEvent.getNamespace());
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    logger.info(String.format("Apollo change - key: %s, oldValue: %s, newValue: %s, changeType: %s",
                            change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
                }
            }
        });
    }
}
