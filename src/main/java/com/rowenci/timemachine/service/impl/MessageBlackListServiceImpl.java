package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.MessageBlackList;
import com.rowenci.timemachine.mapper.MessageBlackListMapper;
import com.rowenci.timemachine.service.IMessageBlackListService;
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
public class MessageBlackListServiceImpl extends ServiceImpl<MessageBlackListMapper, MessageBlackList> implements IMessageBlackListService {

}
