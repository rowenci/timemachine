package com.rowenci.timemachine.service;

import com.rowenci.timemachine.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-19
 */
public interface IMessageService extends IService<Message> {

    List<Message> getMessageByUserId(int user_id);

    Message getMessageByMessageId(int message_id);

    int deleteMessage(int message_id);

}
