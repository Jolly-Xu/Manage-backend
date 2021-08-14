package com.xujialin.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xujialin.CommonResult.ResultCode;
import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.Utils.RedisUtils;
import com.xujialin.entity.Comment;
import com.xujialin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-11
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommentService commentService;

    @GetMapping("/getAllComments")
    public ReturnResult getAllComments(Integer id,Integer currentpage,Integer pagesize){
        //先查redis数据库
        String key = RedisUtils.Generatecommentkey(id);

        Integer redisstart=(currentpage-1)*pagesize;
        Integer redisend=(redisstart-1)+pagesize;

        try {
            List range = redisTemplate.opsForList().range(key, redisstart, redisend);
            Long total = redisTemplate.opsForList().size(key);
            if (range.size() != 0 && !range.isEmpty()) {
                Map<String,Object> map =new HashMap();
                map.put("data",range);
                map.put("total",total);
                return new ReturnResult(true, ResultCode.GET_SUCCESS.getCode(),ResultCode.GET_SUCCESS.getMessage(),map);
            }
            QueryWrapper wrapper =new QueryWrapper();
            Map<String,Object> qmap = new HashMap<>();
            qmap.put("authid",id);
            qmap.put("logic_delete",0);
            wrapper.allEq(qmap);
            wrapper.orderByDesc("time");
            List list = commentService.list(wrapper);
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

        @DeleteMapping("/deletecommentbyid")
    public ReturnResult deletecommentbyid(Integer id,Integer authid){
        try{
            Comment comment =new Comment();
            comment.setLogicDelete(true);
            QueryWrapper wrapper =new QueryWrapper();
            wrapper.eq("id",id);
            commentService.update(comment,wrapper);
            redisTemplate.delete(RedisUtils.Generatecommentkey(authid));
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnResult(false);
        }

        return new ReturnResult(true);
    }
}
