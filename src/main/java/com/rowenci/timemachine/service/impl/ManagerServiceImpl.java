package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.Manager;
import com.rowenci.timemachine.mapper.ManagerMapper;
import com.rowenci.timemachine.service.IManagerService;
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
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements IManagerService {

}
