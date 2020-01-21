package com.rowenci.timemachine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.mapper.MessageMapper;
import com.rowenci.timemachine.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rowenci.timemachine.util.CodeInfo.CRUDCodeInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Integer> getPublicMessage(int user_id) {
        QueryWrapper qw = new QueryWrapper();
        Map map = new HashMap();
        map.put("user_id", user_id);
        map.put("is_public", 1);
        qw.allEq(map);
        List<Message> messages = messageMapper.selectList(qw);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++){
            list.set(i, messages.get(i).getMessageId());
        }
        return list;
    }

    @Override
    public int setPublicMessage(int message_id) {
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("message_id", message_id);
        Message message = messageMapper.selectOne(uw);
        if (message != null){
            return crudCodeInfo.UPDATE_ERROR;
        }
        else {
            message.setIsPublic(1);
            messageMapper.update(message, uw);
            return crudCodeInfo.UPDATE_SUCCESS;
        }
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
