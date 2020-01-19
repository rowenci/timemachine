package com.rowenci.timemachine.service;

import com.rowenci.timemachine.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-15
 */
public interface IUserService extends IService<User> {

    int logUp(User user);

    int logIn(Map<String, String> loginMap);

    User getUserByAccount(String account);

    int changeInfo(User user);

    User getUserById(int user_id);

    int changePWD(String account, String password);

}
