package com.xujialin.Handler;

import com.alibaba.fastjson.JSON;
import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author XuJiaLin
 * @date 2021/8/12 13:55
 */
@Component
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("text/json;charset=utf-8");
        ReturnResult returnResult = new ReturnResult(true);
        httpServletResponse.getWriter().write(JSON.toJSONString(returnResult));
    }
}
