package com.xujialin.Handler;

import com.alibaba.fastjson.JSON;
import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author XuJiaLin
 * @date 2021/8/4 9:33
 */

@Component
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ReturnResult returnResult=new ReturnResult(false, ResultCode.LOGIN_FRIST.getCode(),ResultCode.LOGIN_FRIST.getMessage());
        httpServletResponse.setContentType("text/json;charset=utf-8");
        //System.out.println(JSON.toJSONString(returnResult));
        //System.out.println(returnResult);
        httpServletResponse.getWriter().write(JSON.toJSONString(returnResult));
    }
}
