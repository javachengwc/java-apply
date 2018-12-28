package com.shop.book.manage.config;


import com.shop.book.manage.shiro.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private final static Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    //负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setGlobalSessionTimeout(30 * 24 * 60 * 60 * 1000); //30天
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setSessionIdCookie(simpleCookie());
        sessionManager.setSessionIdCookieEnabled(true);

        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        return sessionManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(){
        return new RedisSessionDAO();
    }

    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        return redisCacheManager;
    }

    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName("session.token");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }

    @Bean
    public SessionListener sessionListener() {
        return new ShiroSessionListener();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login");
        //shiroFilter.setSuccessUrl("/home");  登录成功的url
        shiroFilter.setUnauthorizedUrl("/unauthorized");//未授权界面

        //配置访问权限
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        filterMap.put("/swagger/**", "anon");//表示可以匿名访问
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/statics/**", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/logout", "anon");
        filterMap.put("/sms/**", "anon");
        filterMap.put("/mock/**", "anon");
        filterMap.put("/role/**", "anon");
        filterMap.put("/menu/**", "anon");
        filterMap.put("/dict/**", "anon");
        filterMap.put("/qa/**", "anon");
        filterMap.put("/advert/**", "anon");

        filterMap.put("/**", "authc");//表示需要认证才可以访问

        //加载shiroFilter权限控制规则
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        //自定义拦截器
        LinkedHashMap<String, Filter> filtsMap=new LinkedHashMap<String, Filter>();
        filtsMap.put("authc",new ShiroAuthFilter() );
        shiroFilter.setFilters(filtsMap);

        return shiroFilter;
    }

    //DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    //开启shiro aop注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


}
