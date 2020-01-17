package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.PublicMessage;
import com.rowenci.timemachine.mapper.PublicMessageMapper;
import com.rowenci.timemachine.service.IPublicMessageService;
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
public class PublicMessageServiceImpl extends ServiceImpl<PublicMessageMapper, PublicMessage> implements IPublicMessageService {

}
