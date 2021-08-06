package com.xujialin.Controller;


import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.User;
import com.xujialin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @GetMapping("/getuserinfo")
    public ReturnResult getUserinfo(Integer id) {


        Object o = redisUtils.get(redisUtils.GenerateKey(id));
        //System.out.println(o);

        //如果redis查询不到就查询数据库
        if (o == null) {
            //查询数据库
            System.out.println("查询数据库");
            User user=null;
            try {
                user = userService.getById(id);
                System.out.println(user);
            }catch (Exception e){
                e.printStackTrace();
            }
            return new ReturnResult(true,
                    ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),user);
        }
        else{
           return new ReturnResult(true,
                    ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),o);
        }
    }
}
