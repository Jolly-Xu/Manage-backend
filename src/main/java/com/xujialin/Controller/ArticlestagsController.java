package com.xujialin.Controller;


import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.Articlestags;
import com.xujialin.service.ArticlestagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/articlestags")
public class ArticlestagsController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticlestagsService articlestagsService;

    @GetMapping("/getalltagnames")
    public ReturnResult getalltagnames(){
        String key = RedisUtils.GenerateTagkey();
        List range = redisTemplate.opsForList().range(key, 0, -1);
        Map<String,Object> map =new HashMap();
        if (range != null && range.size()!=0){
            map.put("data",range);
            map.put("total",range.size());
            return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),map);
        }

        List<Articlestags> list = articlestagsService.list();
        redisTemplate.opsForList().rightPushAll(key,list);
        map.put("data",list);
        map.put("total",list.size());
        return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),map);
    }

}
