package com.rowenci.timemachine.service;

import com.rowenci.timemachine.entity.PublicMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-15
 */
public interface IPublicMessageService extends IService<PublicMessage> {

    int addPublicMessage(PublicMessage publicMessage);

    List<PublicMessage> getAllPublicMessage(int message_id);

    int banPublicMessage(int public_message_id);

}
