package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.rowenci.timemachine.entity.PublicMessage;
import com.rowenci.timemachine.entity.SendMessage;
import com.rowenci.timemachine.service.IPublicMessageService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-01-15
 */
@RestController
@RequestMapping("/timemachine/public-message")
public class PublicMessageController {

    @Resource
    private IPublicMessageService iPublicMessageService;

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    /**
     * 添加公共信件
     * @param publicMessage
     * @return
     */
    @PostMapping("/")
    public String addPublicMessage(@RequestBody PublicMessage publicMessage){
        SendMessage sendMessage = new SendMessage();
        int code = iPublicMessageService.addPublicMessage(publicMessage);
        if (code > 0){
            sendMessage.initMessage(sendMessage, serviceCodeInfo.SUCCESS, "", "success", "");
        }
        else {
            sendMessage.initMessage(sendMessage, code, "", "error", "");
        }
        return JSON.toJSONString(sendMessage);
    }

    /**
     * 根据信件id获取公共信件
     * @param message_id
     * @return
     */
    @GetMapping("/")
    public String getAllPublicMessage(int message_id){
        SendMessage sendMessage = new SendMessage();
        List<PublicMessage> list = iPublicMessageService.getAllPublicMessage(message_id);
        sendMessage.initMessage(sendMessage, serviceCodeInfo.SUCCESS, list, "success", "");
        return JSON.toJSONString(sendMessage);
    }

    /**
     * 将公共信件加入黑名单
     * @param public_message_id
     * @return
     */
    @PutMapping("/")
    public String banPublicMessage(int public_message_id){
        SendMessage sendMessage = new SendMessage();
        int code = iPublicMessageService.banPublicMessage(public_message_id);
        if (code > 0){
            sendMessage.initMessage(sendMessage, serviceCodeInfo.SUCCESS, "", "success", "");
        }
        else {
            sendMessage.initMessage(sendMessage, code, "", "error", "");
        }
        return JSON.toJSONString(sendMessage);
    }

}
