package com.xujialin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.Articles;
import com.xujialin.entity.Articlestags;
import com.xujialin.entity.User;
import com.xujialin.service.ArticlesService;
import com.xujialin.service.ArticlestagsService;
import com.xujialin.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
class WebServerApplicationTests {

    @Autowired
    private UrlService urlService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticlestagsService articlestagsService;

    @Test
    void contextLoads() {
    }

    /**
     * 测试通过url获取权限列表
     * 测试获取权限列表
     */
    @Test
    void Test01(){
        List list = urlService.getpermissionListByURL("/index1");
        System.out.println(list.isEmpty());
    }

    /**
     * 测试redis存储对象
     */

    @Test
    void Test02(){
        User user=new User();
        user.setUsername("xujialin");
        user.setId(1);
        user.setPassword("xujialin");
        redisUtils.set("user",user);
        Object user1 = redisUtils.get("user");
        System.out.println(user1);
    }

    /**
     * 测试redis工具类
     */
    @Test
    void Test03(){
        String key = redisUtils.GenerateKey(5);
        System.out.println(key);
    }

    /**
     * 测试mybatis的sql语句
     */

    @Test
    void Test04(){
        List<Articles> articlesbypage = articlesService.getArticlesbypage("这", "徐",
                "2021-8-10", "2021-8-22", 0, 10, 2);
        System.out.println(articlesbypage);

        Long articles = redisTemplate.opsForList().rightPushAll("articles", articlesbypage);
        System.out.println(articles);
        //String s ="";
        //System.out.println(s.isEmpty());
    }

    @Test
    void Test05(){
        long articles = redisTemplate.opsForList().size("articles");
        System.out.println(articles);

    }

    /**
     * 测试redis获取的数据
     */

    @Test
    void Test06(){
        User o = (User) redisTemplate.opsForValue().get("Blog:UserInfo:1");
        System.out.println(o.getPassword());
    }

    @Test
    void Test07(){
        String s=" ";
        System.out.println(s.equals(' '));
    }

    /**
     * 测试mybatis list
     */
    @Test
    void Test08(){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.select("tagname");
        List<Articlestags> list = articlestagsService.list(wrapper);
        System.out.println(list);
    }

}
