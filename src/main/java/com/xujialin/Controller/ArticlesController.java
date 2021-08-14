package com.xujialin.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.Utils.UUidUtils;
import com.xujialin.entity.Articles;
import com.xujialin.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;



/**
 * @author XujialinDashuaige
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/articles")
public class ArticlesController {

    private final long expiretime = 3600;

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *
     * @param articletag
     * @param authname
     * @param starttime
     * @param endtime
     * @param currentPage
     * @param pagesize
     * @param id
     * 获取文章列表
     * @return
     */
    @GetMapping("/getarticles")
    public ReturnResult getgetarticles(String articletag, String authname,
                                       String starttime, String endtime,
                                       Integer currentPage, Integer pagesize,
                                       Integer id) {


        long total = 0;
        currentPage = (currentPage - 1) * pagesize;
        //生成查询条件，去redis缓存里面查
        String key = RedisUtils.GenerateFindKey(articletag, authname, starttime, endtime, id);
        if (redisTemplate.hasKey(key)) {
            List articlesbyredis = redisTemplate.opsForList().range(key, currentPage, (currentPage - 1) + pagesize);
            total = redisTemplate.opsForList().size(key);
            Map<String, Object> map = new HashMap<>();
            map.put("data", articlesbyredis);
            map.put("total", total);
            return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(), ResultCode.GET_SUCCESS.getMessage(), map);
        }

        //如果查询条件全满

        List<Articles> articlesbymysql = articlesService.getArticlesbypage(
                articletag, authname, starttime, endtime, 0, 0, id);
        Map<String, Object> map = new HashMap<>();
        if (articlesbymysql != null && articlesbymysql.size() != 0) {
            //System.out.println("查询数据库");
            redisTemplate.opsForList().rightPushAll(key, articlesbymysql);
            redisTemplate.expire(key, expiretime, TimeUnit.SECONDS);
            total = articlesbymysql.size();
            pagesize= Math.toIntExact(pagesize+currentPage > total ? total : currentPage+pagesize);
            map.put("data", articlesbymysql.subList(currentPage ,pagesize));
            map.put("total", total);
        }
        return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(), ResultCode.GET_SUCCESS.getMessage(), map);

    }

    /**
     *
     * @param title
     * @param content
     * @param opt
     * @param coverimg
     * @param isprivacy
     * @param name
     * @param id
     * 添加文章
     * @return
     */
    @PostMapping("/addarticle")
    public ReturnResult addarticle(String title,String content,String opt,String coverimg,Boolean isprivacy,String name,Integer id){

        //存数据
        Articles articles=new Articles();
        articles.setArticlename(title);
        articles.setArticletag(opt);
        articles.setArticleauthname(name);
        articles.setCreatetime(LocalDateTime.now());
        articles.setUpdatatime(LocalDateTime.now());
        articles.setAuthid(id);
        articles.setCover(coverimg);
        articles.setIsprivacy(isprivacy);
        String generateuusqlid=UUidUtils.generateuuid();
        articles.setId(generateuusqlid);
        articles.setArticlecontent(content);

        try {
            articlesService.save(articles);
        //    清空redis里面的查找的缓存
            redisTemplate.delete("Blog:Find:Key:" + id);
            redisTemplate.opsForValue().set(RedisUtils.GenerateFindKey(generateuusqlid),articles);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ReturnResult(false,ResultCode.GLOBAL_ERROR.getCode(),ResultCode.GLOBAL_ERROR.getMessage(),null);
        }

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("id",generateuusqlid);
        Articles one = articlesService.getOne(wrapper);
        System.out.println(one);
        Map<String,Object> map=new HashMap<>();
        map.put("content",one.getArticlecontent());
        return new ReturnResult(true,ResultCode.ADD_SUCCESS.getCode(),ResultCode.ADD_SUCCESS.getMessage(),map);

    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @DeleteMapping("/deletearticle")
    public ReturnResult deletearticle(String id,String authid) {
        try {
            articlesService.DeletearticlebyId(id);
            redisTemplate.delete("Blog:Find:Key:" + authid);
            redisTemplate.delete(RedisUtils.GenerateFindKey(id));
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnResult(false, ResultCode.DELETE_ERROR.getCode(), ResultCode.DELETE_ERROR.getMessage(), null);
        }
        return new ReturnResult(true, ResultCode.DELETE_SUCCESS.getCode(), ResultCode.DELETE_SUCCESS.getMessage(), null);
    }

    /**
     * 修改文章自我可见
     * @param id
     * @param state
     * @param authid
     * @return
     */
    @PutMapping("/changeprivacy")
    public ReturnResult changeprivacy(String id,Boolean state,Integer authid){
        try {
            Boolean delete = redisTemplate.delete("Blog:Find:Key:" + authid);
            int flag= state ? 1 : 0;
            articlesService.Changeprivacy(id, flag);
        }catch (Exception e){
            return new ReturnResult(false);
        }
       return new ReturnResult(true);
    }

    /**
     * 根据文章id,获取文章
     * @param id
     * @return
     */
    @GetMapping("/getarticlebyid")
    public ReturnResult getarticlebyid(String id){
        //先查找reids
        try {
            String key = RedisUtils.GenerateFindKey(id);
            Object key1 = redisTemplate.opsForValue().get(key);
            if (key1 != null) {
                return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(), ResultCode.GET_SUCCESS.getMessage(), key1);
            }
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id", id);
            Articles one = articlesService.getOne(wrapper);
            one.setUpdatatime(null);
            one.setCreatetime(null);
            one.setLogicDelete(null);
            one.setAuthid(null);
            redisTemplate.opsForValue().set(key,one);
            return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(), ResultCode.GET_SUCCESS.getMessage(), one);
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnResult(false, ResultCode.GLOBAL_ERROR.getCode(), ResultCode.GLOBAL_ERROR.getMessage(), null);
        }
    }


    /**
     * 修改文章接口
     * @param title
     * @param content
     * @param opt
     * @param coverimg
     * @param isprivacy
     * @param id
     * @return
     */

    @PostMapping("/UpdataArticle")
    public ReturnResult UpdataArticle(String title,String content,String opt,String coverimg,Boolean isprivacy,String id,Integer authid) {
        //存数据
        Articles articles=new Articles();
        articles.setArticlename(title);
        articles.setArticletag(opt);
        articles.setUpdatatime(LocalDateTime.now());
        articles.setCover(coverimg);
        articles.setIsprivacy(isprivacy);
        articles.setArticlecontent(content);

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("id",id);
        try {
             articlesService.update(articles, wrapper);
            redisTemplate.delete("Blog:Find:Key:" + authid);
            redisTemplate.delete(RedisUtils.GenerateFindKey(id));
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnResult(false,ResultCode.UPDATA_ERROR.getCode(),ResultCode.UPDATA_ERROR.getMessage(),null);
        }
        return new ReturnResult(true,ResultCode.UPDATA_SUCCESS.getCode(),ResultCode.UPDATA_SUCCESS.getMessage(),null);

    }
}
