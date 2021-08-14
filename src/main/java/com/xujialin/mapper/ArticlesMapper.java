package com.xujialin.mapper;

import com.xujialin.entity.Articles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-05
 */
@Mapper
public interface ArticlesMapper extends BaseMapper<Articles> {
    public List<Articles> getArticlesbypage (String articlecontent,
                                             String authname,
                                             String starttime,
                                             String endtime,
                                             Integer startpage,
                                             Integer pagesize,
                                             Integer authid);

    public Boolean DeletearticlebyId(String id);

    public void Changeprivacy(String id,int state);
}
