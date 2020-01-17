package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.BannedUser;
import com.rowenci.timemachine.mapper.BannedUserMapper;
import com.rowenci.timemachine.service.IBannedUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-15
 */
@Service
public class BannedUserServiceImpl extends ServiceImpl<BannedUserMapper, BannedUser> implements IBannedUserService {

}
