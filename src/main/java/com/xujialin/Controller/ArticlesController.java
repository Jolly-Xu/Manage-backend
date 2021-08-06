package com.xujialin.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.Articles;
import com.xujialin.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/articles")
public class ArticlesController {

    private final long expiretime = 7200;

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/getarticles")
    public ReturnResult getgetarticles(String articletag, String authname,
                                       String starttime, String endtime,
                                       Integer currentPage, Integer pagesize,
                                       Integer id) {



        long total = 0;
        currentPage = (currentPage - 1) * pagesize;
        int currentPage2 = currentPage == 0? 0:currentPage-1;
        //生成查询条件，去redis缓存里面查
        String key = RedisUtils.GenerateFindKey(articletag, authname, starttime, endtime);
        if (redisTemplate.hasKey(key)) {
            List articlesbyredis = redisTemplate.opsForList().range(key, currentPage2, pagesize);
            total = redisTemplate.opsForList().size(key);
            Map<String, Object> map = new HashMap<>();
            map.put("data", articlesbyredis);
            map.put("total", total);
            return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(), ResultCode.GET_SUCCESS.getMessage(), map);
        }

        //如果查询条件全满


        List<Articles> articlesbymysql = articlesService.getArticlesbypage(
                articletag, authname, starttime, endtime, 0, 0, id);
        System.out.println(articlesbymysql);
        Map<String, Object> map = new HashMap<>();
        if (articlesbymysql!=null&&articlesbymysql.size()!=0)
        {
            redisTemplate.opsForList().rightPushAll(key, articlesbymysql);
            redisTemplate.expire(key, expiretime, TimeUnit.SECONDS);
            total = articlesbymysql.size();
            map.put("data", articlesbymysql.subList(currentPage2, currentPage2 + pagesize));
            map.put("total", total);
        }
        return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(), ResultCode.GET_SUCCESS.getMessage(), map);

    }
}
