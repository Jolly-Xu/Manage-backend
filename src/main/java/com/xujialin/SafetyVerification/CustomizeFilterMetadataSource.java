package com.xujialin.SafetyVerification;

import com.xujialin.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * @author XuJiaLin
 * @date 2021/8/4 11:37
 * 自定义过滤元数据
 *
 */

@Component
public class CustomizeFilterMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private UrlService urlService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取当前请求路径
        HttpServletRequest httpRequest = ((FilterInvocation) o).getHttpRequest();
        String requestURI = httpRequest.getRequestURI();

        System.out.println(requestURI);

        //获取对应的路径允许的访问权限
        List<String> list = urlService.getpermissionListByURL(requestURI);

        //如果权限列表为空，返回null
        if (list.isEmpty() || list.size() == 0)
            return null;

        //创建一个数组进行权限传参,因为为可变长参数
        String [] attributes = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            attributes[i]=list.get(i);
        }


        return SecurityConfig.createList(attributes);

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
