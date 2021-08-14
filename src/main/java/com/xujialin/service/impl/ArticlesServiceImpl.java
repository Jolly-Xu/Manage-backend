package com.xujialin.service.impl;

import com.xujialin.entity.Articles;
import com.xujialin.mapper.ArticlesMapper;
import com.xujialin.service.ArticlesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-05
 */
@Service
public class ArticlesServiceImpl extends ServiceImpl<ArticlesMapper, Articles> implements ArticlesService {

    @Override
    public List<Articles> getArticlesbypage(String articlecontent, String authname, String starttime, String endtime, Integer currentpage, Integer pagesize,Integer authid) {
        articlecontent=articlecontent == null?"":articlecontent;
        authname=authname == null?"":authname;
        starttime=starttime == null?"":starttime;

        return this.baseMapper.getArticlesbypage(articlecontent,authname,starttime,endtime,currentpage,pagesize,authid);
    }

    @Override
    public Boolean DeletearticlebyId(String id) {
        return this.baseMapper.DeletearticlebyId(id);
    }

    @Override
    public void Changeprivacy(String id, int state) {
         this.baseMapper.Changeprivacy(id,state);
    }
}
