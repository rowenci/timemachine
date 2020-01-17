package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.BannedMessage;
import com.rowenci.timemachine.mapper.BannedMessageMapper;
import com.rowenci.timemachine.service.IBannedMessageService;
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
public class BannedMessageServiceImpl extends ServiceImpl<BannedMessageMapper, BannedMessage> implements IBannedMessageService {

}
