package com.xujialin.service.impl;

import com.xujialin.entity.Url;
import com.xujialin.mapper.UrlMapper;
import com.xujialin.service.UrlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-04
 */
@Service
public class UrlServiceImpl extends ServiceImpl<UrlMapper, Url> implements UrlService {
    @Override
    public List<String> getpermissionListByURL(String url) {
        return this.baseMapper.getpermissionListByURL(url);
    }
}
