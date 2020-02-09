package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.mapper.UserMapper;
import com.rowenci.timemachine.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-02-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
