package com.xujialin.SafetyVerification;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.User;
import com.xujialin.service.UserService;
import com.xujialin.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author XuJiaLin
 * @date 2021/8/3 20:50
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    //注入Encoder
    @Autowired
    private PasswordEncoder encoder;

    //注入UserService
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //判断username是否为空
        if (username.equals("")||username == null)
        throw new UsernameNotFoundException("用户名为空");
        MyUserDetails userDetails = new MyUserDetails();

        String key = RedisUtils.GenerateLoginKey(username);
        User o=null;
            //先查询Redis数据库
         o = (User) redisTemplate.opsForValue().get(key);

        if (o ==null){
            QueryWrapper wrapper=new QueryWrapper();
            Map<String,Object> map =new HashMap<>();
            map.put("username",username);
            map.put("logic_delete",0);
            wrapper.allEq(map);
            User one = userService.getOne(wrapper);
            if (one == null){
              throw new UsernameNotFoundException("用户名不存在");
            }
            redisTemplate.opsForValue().set(key,one);
            redisTemplate.expire(key,60*60*2, TimeUnit.SECONDS);
            o=one;
        }
            userDetails.setUser(o);
            userDetails.setUsername(o.getUsername());
            userDetails.setPassword(encoder.encode(o.getPassword()));
            SimpleGrantedAuthority authority=new SimpleGrantedAuthority(o.getAuthoritiy());
            Set<GrantedAuthority> authorities=new HashSet<>();
            authorities.add(authority);
            userDetails.setAuthorities(authorities);
            return userDetails;
    }
}
