package com.rowenci.timemachine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.PublicMessage;
import com.rowenci.timemachine.mapper.PublicMessageMapper;
import com.rowenci.timemachine.service.IPublicMessageService;
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
 * @since 2020-01-15
 */
@Service
public class PublicMessageServiceImpl extends ServiceImpl<PublicMessageMapper, PublicMessage> implements IPublicMessageService {

    @Resource
    private PublicMessageMapper publicMessageMapper;

    @Resource
    private CRUDCodeInfo crudCodeInfo;

    @Override
    public int addPublicMessage(PublicMessage publicMessage) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", publicMessage.getMessageId());
        if (publicMessageMapper.selectOne(qw) != null){
            return crudCodeInfo.INSERT_ERROR;
        }
        else {
            publicMessageMapper.insert(publicMessage);
            return crudCodeInfo.INSERT_SUCCESS;
        }
    }

    @Override
    public List<PublicMessage> getAllPublicMessage(int message_id) {
        QueryWrapper qw=  new QueryWrapper();
        qw.eq("message_id", message_id);
        List<PublicMessage> list = publicMessageMapper.selectList(qw);
        return list;
    }

    @Override
    public int banPublicMessage(int public_message_id) {
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("public_message_id", public_message_id);
        PublicMessage publicMessage = publicMessageMapper.selectOne(uw);
        if (publicMessage == null){
            return crudCodeInfo.ENTITY_NOT_EXISTS;
        }
        else {
            publicMessage.setBanned(1);
            publicMessageMapper.update(publicMessage, uw);
            return crudCodeInfo.UPDATE_SUCCESS;
        }
    }
}
