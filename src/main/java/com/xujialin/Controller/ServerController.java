package com.xujialin.Controller;

import com.xujialin.POJO.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XuJiaLin
 * @date 2021/7/29 11:24
 */
@RestController
public class ServerController {
    @RequestMapping("/login")
    public User getcar(@RequestBody User user){
        System.out.println(user);
        return user;
    }

    @RequestMapping("/login1")
    public String login(String username,String password){
        System.out.println(username + password);
        return username+password;
    }

    @RequestMapping("/login2")
    public User login2(User user){
        System.out.println(user);
        if (user.getUsername().equals("xujialin")&&user.getPassword().equals("123456"))
        {
            return user;
        }
        System.out.println(111);
        return null;

    }
}
