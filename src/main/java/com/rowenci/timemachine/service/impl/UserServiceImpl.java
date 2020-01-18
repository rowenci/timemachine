package com.rowenci.timemachine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.mapper.UserMapper;
import com.rowenci.timemachine.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rowenci.timemachine.util.CodeInfo.CRUDCodeInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CRUDCodeInfo CRUDCodeInfo;

    @Override
    public int logUp(User user) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("account", user.getAccount());
        if (userMapper.selectOne(qw) != null){
            return CRUDCodeInfo.ENTITY_EXISTS;
        }
        userMapper.insert(user);
        return CRUDCodeInfo.INSERT_SUCCESS;
    }

    @Override
    public int logIn(Map<String, String> loginMap) {
        QueryWrapper qw = new QueryWrapper();
        qw.allEq(loginMap);
        if (userMapper.selectOne(qw) == null){
            return CRUDCodeInfo.ENTITY_NOT_EXISTS;
        }else {
            return CRUDCodeInfo.SELECT_SUCCESS;
        }
    }

    @Override
    public User getUserByAccount(String account) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("account", account);
        return userMapper.selectOne(qw);
    }

    @Override
    public int changeInfo(User user) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", user.getUserId());
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("user_id", user.getUserId());
        if (userMapper.selectOne(qw) == null){
            return CRUDCodeInfo.UPDATE_ERROR;
        }else {
            userMapper.update(user, uw);
            return CRUDCodeInfo.UPDATE_SUCCESS;
        }
    }
}
