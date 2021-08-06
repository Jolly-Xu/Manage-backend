package com.xujialin.service.impl;

import com.xujialin.entity.User;
import com.xujialin.mapper.UserMapper;
import com.xujialin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
