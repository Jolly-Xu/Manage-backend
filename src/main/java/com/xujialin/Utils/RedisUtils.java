package com.xujialin.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

/**
 * @author XuJiaLin
 * @date 2021/8/5 9:33
 */

@Component
public class RedisUtils {


    @Autowired
    private RedisTemplate redisTemplate;

    public Object get(final String key) {
        if (key.isEmpty() || key.equals("") || key == null) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object set(final String key, Object o) {
        if (key.isEmpty() || key.equals("") || key == null) {
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String GenerateFindKey(String q1,String q2,String q3,String q4,Integer id)
    {
        return "Blog:Find:Key:"+q1+q2+q3+q4+id;
    }

    public static String GenerateFindKey(String q1)
    {
        return "Blog:Find:Key:"+q1;
    }

    public String GenerateKey(Integer id){
        return "Blog:UserInfo:"+id;
    }

    public static String GenerateLoginKey(String id){
        return "Blog:UserInfo:"+id;
    }

    public static String GenerateTagkey()
    {
        return "Blog:Find:TagNames";
    }

    public static String Generatecommentkey(Integer id){return "Blog:Find:Comment:"+id;}

    public static String GenerateKeyAlluserkey(){return  "Blog:UserInfo:ALL";}
}


