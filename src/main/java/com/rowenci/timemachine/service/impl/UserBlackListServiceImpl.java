package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.UserBlackList;
import com.rowenci.timemachine.mapper.UserBlackListMapper;
import com.rowenci.timemachine.service.IUserBlackListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-02-09
 */
@Service
public class UserBlackListServiceImpl extends ServiceImpl<UserBlackListMapper, UserBlackList> implements IUserBlackListService {

}
