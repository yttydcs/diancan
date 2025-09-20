package com.example.diancan2.common;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    /**
     * SecurityManager 配置
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    /**
     * 使用标准 ShiroFilterChainDefinition 来定义过滤规则
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // 公开接口
        chainDefinition.addPathDefinition("/api/user/login", "anon");

        // 权限管理
        chainDefinition.addPathDefinition("/api/user/**", "authc, perms[user:manage]");
        chainDefinition.addPathDefinition("/api/role/**", "authc, perms[user:manage]");
        chainDefinition.addPathDefinition("/api/store/**", "authc, perms[store:manage]");

        // 业务管理
        chainDefinition.addPathDefinition("/api/seat/**", "authc");
        chainDefinition.addPathDefinition("/api/food/**", "authc");
        chainDefinition.addPathDefinition("/api/order/**", "authc");

        // 其他所有请求都放行
        chainDefinition.addPathDefinition("/**", "anon");

        return chainDefinition;
    }

    /**
     * 密码加密配置
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(2);
        return matcher;
    }

    /**
     * 开启 Shiro 注解支持，比如 @RequiresRoles, @RequiresPermissions
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
