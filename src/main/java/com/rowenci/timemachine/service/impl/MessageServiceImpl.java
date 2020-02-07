package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.mapper.MessageMapper;
import com.rowenci.timemachine.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-02-07
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
