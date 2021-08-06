package com.xujialin.mapper;

import com.xujialin.entity.Url;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-04
 */
@Mapper
public interface UrlMapper extends BaseMapper<Url> {

    public List<String> getpermissionListByURL(String url);
}
