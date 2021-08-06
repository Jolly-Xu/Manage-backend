package com.xujialin.SafetyVerification;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author XuJiaLin
 * @date 2021/8/4 11:36
 * 自定义权限决策处理器
 */

@Component
public class CustomizeAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        //当前用户所具有的权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        //获取一个迭代器
        Iterator<ConfigAttribute> iterator = collection.iterator();

        String attribute=null;
        //判断用户具有的权限和访问路径需要的权限是否匹配
        for (ConfigAttribute configAttribute : collection) {
          attribute = configAttribute.getAttribute();
            for (GrantedAuthority authority : authorities) {
                if(authority.getAuthority().equals(attribute)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
