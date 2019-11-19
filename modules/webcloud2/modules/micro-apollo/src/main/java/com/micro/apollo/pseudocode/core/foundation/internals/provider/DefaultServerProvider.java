package com.micro.apollo.pseudocode.core.foundation.internals.provider;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import com.ctrip.framework.foundation.internals.Utils;
import com.ctrip.framework.foundation.internals.io.BOMInputStream;
import com.micro.apollo.pseudocode.core.foundation.spi.provider.Provider;
import com.micro.apollo.pseudocode.core.foundation.spi.provider.ServerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//服务配置
public class DefaultServerProvider implements ServerProvider {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServerProvider.class);
    private static final String SERVER_PROPERTIES_LINUX = "/opt/settings/server.properties";
    private static final String SERVER_PROPERTIES_WINDOWS = "C:/opt/settings/server.properties";

    //环境
    private String m_env;

    //集群
    private String m_dc;

    private Properties m_serverProperties = new Properties();

    @Override
    public void initialize() {
        try {
            String path = Utils.isOSWindows() ? SERVER_PROPERTIES_WINDOWS : SERVER_PROPERTIES_LINUX;

            File file = new File(path);
            if (file.exists() && file.canRead()) {
                logger.info("Loading {}", file.getAbsolutePath());
                FileInputStream fis = new FileInputStream(file);
                initialize(fis);
                return;
            }

            initialize(null);
        } catch (Throwable ex) {
            logger.error("Initialize DefaultServerProvider failed.", ex);
        }
    }

    @Override
    public void initialize(InputStream in) {
        try {
            if (in != null) {
                try {
                    m_serverProperties.load(new InputStreamReader(new BOMInputStream(in), StandardCharsets.UTF_8));
                } finally {
                    in.close();
                }
            }

            initEnvType();
            initDataCenter();
        } catch (Throwable ex) {
            logger.error("Initialize DefaultServerProvider failed.", ex);
        }
    }

    @Override
    public String getDataCenter() {
        return m_dc;
    }

    @Override
    public boolean isDataCenterSet() {
        return m_dc != null;
    }

    @Override
    public String getEnvType() {
        return m_env;
    }

    @Override
    public boolean isEnvTypeSet() {
        return m_env != null;
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        if ("env".equalsIgnoreCase(name)) {
            String val = getEnvType();
            return val == null ? defaultValue : val;
        } else if ("dc".equalsIgnoreCase(name)) {
            String val = getDataCenter();
            return val == null ? defaultValue : val;
        } else {
            String val = m_serverProperties.getProperty(name, defaultValue);
            return val == null ? defaultValue : val.trim();
        }
    }

    @Override
    public Class<? extends Provider> getType() {
        return ServerProvider.class;
    }

    //初始化环境
    private void initEnvType() {

        //从启动参数中获取env,如:-Denv=YOUR-ENVIRONMENT
        m_env = System.getProperty("env");
        if (!Utils.isBlank(m_env)) {
            m_env = m_env.trim();
            logger.info("Environment is set to [{}] by JVM system property 'env'.", m_env);
            return;
        }

        //从操作系统环境变量ENV中获取env
        m_env = System.getenv("ENV");
        if (!Utils.isBlank(m_env)) {
            m_env = m_env.trim();
            logger.info("Environment is set to [{}] by OS env variable 'ENV'.", m_env);
            return;
        }

        //从配置文件server.properties中获取env
        m_env = m_serverProperties.getProperty("env");
        if (!Utils.isBlank(m_env)) {
            m_env = m_env.trim();
            logger.info("Environment is set to [{}] by property 'env' in server.properties.", m_env);
            return;
        }

        m_env = null;
        logger.info("Environment is set to null. Because it is not available in either (1) JVM system property 'env', (2) OS env variable 'ENV' nor (3) property 'env' from the properties InputStream.");
    }

    //初始化集群(也可以说是数据中心或机房)
    private void initDataCenter() {

        //从启动参数中获取idc,如:-Didc=xxx
        m_dc = System.getProperty("idc");
        if (!Utils.isBlank(m_dc)) {
            m_dc = m_dc.trim();
            logger.info("Data Center is set to [{}] by JVM system property 'idc'.", m_dc);
            return;
        }

        //从操作系统环境变量IDC中获取idc
        m_dc = System.getenv("IDC");
        if (!Utils.isBlank(m_dc)) {
            m_dc = m_dc.trim();
            logger.info("Data Center is set to [{}] by OS env variable 'IDC'.", m_dc);
            return;
        }

        //从配置文件server.properties中获取idc
        m_dc = m_serverProperties.getProperty("idc");
        if (!Utils.isBlank(m_dc)) {
            m_dc = m_dc.trim();
            logger.info("Data Center is set to [{}] by property 'idc' in server.properties.", m_dc);
            return;
        }
    }

    @Override
    public String toString() {
        return "environment [" + getEnvType() + "] data center [" + getDataCenter() + "] properties: " + m_serverProperties
                + " (DefaultServerProvider)";
    }
}
