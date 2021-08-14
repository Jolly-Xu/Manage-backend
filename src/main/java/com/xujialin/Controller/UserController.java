package com.xujialin.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.Comment;
import com.xujialin.entity.User;
import com.xujialin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/getuserinfo")
    public ReturnResult getUserinfo(Integer id) {
        String key= redisUtils.GenerateKey(id);
        User o = (User) redisUtils.get(key);
        if(o!=null) {
            o.setUpdataTime(null);
            o.setCreateTime(null);
            o.setPassword(null);
            o.setMute(null);
            o.setLogicDelete(null);
            return new ReturnResult(true,
                    ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),o);
        }
        //如果redis查询不到就查询数据库
            User user=null;
            try {
                user = userService.getById(id);
                redisTemplate.opsForValue().set(key,user);
                user.setUpdataTime(null);
                user.setCreateTime(null);
                user.setPassword(null);
                user.setMute(null);
                user.setLogicDelete(null);
                return new ReturnResult(true,
                        ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),user);
            }catch (Exception e){
                e.printStackTrace();
            }
          return null;
    }

    @PutMapping("/updatauserinfo")
    public String Updatauserinfo(String username,String intro,String job,String  password,
                                 String eamil,String avatar,Integer id,String oldusername){

        User user = new User();
        user.setNickname(username);
        user.setIntro(intro);
        if (!password.isEmpty()){
            user.setPassword(password);
        }
        user.setEmail(eamil);
        user.setIntro(intro);
        user.setAvatar(avatar);
        user.setJob(job);
        try{
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("id",id);
            userService.update(user,wrapper);
            redisTemplate.delete("Blog:UserInfo:"+id);
            redisTemplate.delete("Blog:UserInfo:"+oldusername);
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "success";
    }


    @GetMapping("/getallusers")
    public ReturnResult getAllUsers(Integer currentpage,Integer pagesize){
        String key = RedisUtils.GenerateKeyAlluserkey();

        Integer redisstart=(currentpage-1)*pagesize;
        Integer redisend=(redisstart-1)+pagesize;

        //先查询redis数据库
        try {
            List range = redisTemplate.opsForList().range(key, redisstart, redisend);
            Long total = redisTemplate.opsForList().size(key);
            if (range.size() != 0 && !range.isEmpty()) {
                Map<String,Object> map =new HashMap();
                map.put("data",range);
                map.put("total",total);
                return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),map);
            }

            //查询不到就查sql数据库
            QueryWrapper wrapper =new QueryWrapper();
            wrapper.eq("logic_delete",0);
            wrapper.orderByDesc("create_time");
            List list = userService.list(wrapper);
            if (!list.isEmpty()||list.size()!=0){
                redisTemplate.opsForList().rightPushAll(key,list);
                redisTemplate.expire(key,60*60*2, TimeUnit.SECONDS);
                Integer liststart=redisstart;
                Integer listend= Math.toIntExact(liststart + pagesize > list.size() ? list.size() : (pagesize + liststart));
                List list1 = list.subList(liststart, listend);
                Map<String,Object> map =new HashMap();
                map.put("data",list1);
                map.put("total",list.size());
                return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),map);
            }

            return new ReturnResult(false);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ReturnResult(false);
        }
    }

    @DeleteMapping("/deleteuserbyid")
    public ReturnResult deleteuserbyid(Integer id){
        try{
            User user =new User();
            user.setLogicDelete(true);
            user.setUpdataTime(LocalDateTime.now());
            QueryWrapper wrapper =new QueryWrapper();
            wrapper.eq("id",id);
            userService.update(user,wrapper);
            redisTemplate.delete(RedisUtils.GenerateKeyAlluserkey());
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnResult(false);
        }
        return new ReturnResult(true);
    }

    @PutMapping("/changemute")
    public ReturnResult changemute(Integer id,Boolean state){
        try {
            Boolean delete = redisTemplate.delete(RedisUtils.GenerateKeyAlluserkey());
            User user =new User();
            user.setMute(state);
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("id",id);
            userService.update(user,wrapper);
        }catch (Exception e){
            return new ReturnResult(false);
        }
        return new ReturnResult(true);

    }
}

