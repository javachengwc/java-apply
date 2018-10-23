package com.mybatis.springboot.autoconfigure;


import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@ConditionalOnBean({SqlSessionFactory.class})
@EnableConfigurationProperties({PageHelperProperties.class})
@AutoConfigureAfter({MybatisAutoConfiguration.class})
public class PageHelperAutoConfiguration implements EnvironmentAware
{
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Autowired
    private PageHelperProperties pageHelperProperties;

    private RelaxedPropertyResolver resolver;

    public void setEnvironment(Environment environment)
    {
        this.resolver = new RelaxedPropertyResolver(environment, "pagehelper.");
    }

    @PostConstruct
    public void addPageInterceptor() {
        PageInterceptor interceptor = new PageInterceptor();
        Properties properties = this.pageHelperProperties.getProperties();
        Map<String,Object> subProperties = this.resolver.getSubProperties("");
        for (String key : subProperties.keySet()) {
            if (!properties.containsKey(key)) {
                properties.setProperty(key, this.resolver.getProperty(key));
            }
        }
        interceptor.setProperties(properties);
        for (SqlSessionFactory sqlSessionFactory : this.sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }
}
