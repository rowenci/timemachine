package com.rowenci.timemachine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.mapper.MessageMapper;
import com.rowenci.timemachine.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rowenci.timemachine.util.CodeInfo.CRUDCodeInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-19
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private CRUDCodeInfo crudCodeInfo;

    @Override
    public List<Message> getMessageByUserId(int user_id) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", user_id);
        List<Message> list = messageMapper.selectList(qw);
        return list;
    }

    @Override
    public Message getMessageByMessageId(int message_id) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", message_id);
        Message message = messageMapper.selectOne(qw);
        return message;
    }

    @Override
    public int deleteMessage(int message_id) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", message_id);
        if (messageMapper.selectOne(qw) == null){
            return crudCodeInfo.DELETE_ERROR;
        }
        else {
            messageMapper.delete(qw);
            return crudCodeInfo.DELETE_SUCCESS;
        }

    }
}
