package com.xujialin.service;

import com.xujialin.entity.Articles;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-05
 */
public interface ArticlesService extends IService<Articles> {
    public List<Articles> getArticlesbypage (String articlecontent,
                                             String authname,
                                             String starttime,
                                             String endtime,
                                             Integer currentpage,
                                             Integer pagesize,
                                             Integer authid);

    public Boolean DeletearticlebyId(String id);

    public void Changeprivacy(String id,int state);

}