package com.micro.training.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//apollo默认本地缓存文件放于/opt/data/${app.id}/config-cache下，可在配置中修改，需要保证程序对此目录有读写权限
public class ApolloClientConfig {

    private static Logger logger= LoggerFactory.getLogger(ApolloClientConfig.class);

    private static String requestTimeoutKey ="request.timeout";

    @ApolloConfig
    //@ApolloConfig("application") 这样是指定具体命名空间
    private Config config;

    @ApolloConfigChangeListener("application")
    private void onChange(ConfigChangeEvent changeEvent) {
        logger.info("ApolloClientConfig apollo 配置改变，changes for namespace " + changeEvent.getNamespace());
        for (String key : changeEvent.changedKeys()) {
            ConfigChange change = changeEvent.getChange(key);
            logger.info(String.format("ApolloClientConfig apollo change - key: %s, oldValue: %s, newValue: %s, changeType: %s",
                    change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
        }
        if (changeEvent.isChanged(requestTimeoutKey)) {
            logger.info("ApolloClientConfig apollo key "+requestTimeoutKey +"changed ");
            //触发相应的处理
        }
    }

    public String getConfig(String key) {
        String result = this.getConfig(key,null);
        return result;
    }

    public String getConfig(String key,String defValue) {

        String result = config.getProperty(key, defValue);
        logger.info("ApolloClientConfig getConfig key={},value={}", key, result);
        return result;
    }

    public String getConfigFile(String key) {

        ConfigFile configFile = ConfigService.getConfigFile(key, ConfigFileFormat.XML);
        String result = configFile.getContent();
        logger.info("ApolloClientConfig getConfigFile key={},value={}", key, result);
        return result;

    }

}
