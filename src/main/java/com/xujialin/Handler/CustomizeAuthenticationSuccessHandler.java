package com.xujialin.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.SafetyVerification.MyUserDetails;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XuJiaLin
 * @date 2021/8/4 10:42
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("text/json;charset=utf-8");
        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();
        User user = principal.getUser();

        Map<String,String> map=new HashMap<>();
        map.put("id", String.valueOf(user.getId()));
        map.put("nickname",user.getNickname());
        map.put("avatar",user.getAvatar());
        map.put("authoritiy",user.getAuthoritiy());

        ReturnResult returnResult = new ReturnResult(true, ResultCode.LOGIN_SUCCESS.getCode(),
                ResultCode.LOGIN_SUCCESS.getMessage(), map);
        httpServletResponse.getWriter().write(JSON.toJSONString(returnResult));
    }
}
