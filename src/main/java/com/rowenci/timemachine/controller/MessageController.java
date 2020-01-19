package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.entity.SendMessage;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-01-19
 */
@RestController
@RequestMapping("/timemachine/message")
public class MessageController {

    @Resource
    IMessageService iMessageService;

    @Resource
    ServiceCodeInfo serviceCodeInfo;

    @GetMapping("/")
    public String getAllMessages(int user_id){
        SendMessage sendMessage = new SendMessage();
        List<Message> list = iMessageService.getMessageByUserId(user_id);
        sendMessage.initMessage(serviceCodeInfo.SUCCESS, list, "success", "");
        return JSON.toJSONString(sendMessage);
    }

    @GetMapping("/{message_id}")
    public String getMessageByMessageId(@PathVariable("message_id") int message_id){
        SendMessage sendMessage = new SendMessage();
        Message message = iMessageService.getMessageByMessageId(message_id);
        sendMessage.initMessage(serviceCodeInfo.SUCCESS, message, "success", "");
        return JSON.toJSONString(sendMessage);
    }

    @GetMapping("/delete")
    public String deleteMessage(int message_id){
        SendMessage sendMessage = new SendMessage();
        int code = iMessageService.deleteMessage(message_id);
        if (code > 0){
            sendMessage.initMessage(code, "", "success", "");
        }
        else {
            sendMessage.initMessage(code, "", "error", "");
        }
        return JSON.toJSONString(sendMessage);
    }

}
