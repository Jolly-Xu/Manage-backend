package com.xujialin.service;

import com.xujialin.entity.Url;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-04
 */
public interface UrlService extends IService<Url> {
    public List<String> getpermissionListByURL(String url);
}
