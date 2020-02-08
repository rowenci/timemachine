package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.entity.PublicArea;
import com.rowenci.timemachine.entity.PublicMessage;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.service.IPublicMessageService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import com.rowenci.timemachine.util.CreateUUID;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-07
 */
@RestController
@RequestMapping("/timemachine/public-message")
public class PublicMessageController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IPublicMessageService iPublicMessageService;

    @Resource
    private IMessageService iMessageService;

    @PostMapping("/")
    public String addPublicMessage(PublicMessage publicMessage){

        ModelMap modelMap = new ModelMap();

        //获取uuid
        CreateUUID createUUID = new CreateUUID();
        String publicMessageId = createUUID.create();
        publicMessage.setPublicMessageId(publicMessageId);

        boolean res = iPublicMessageService.save(publicMessage);

        if (res){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "添加成功");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.ADD_PUBLIC_MESSAGE_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "添加失败");
        }
        return JSON.toJSONString(modelMap);

    }

    @GetMapping("/")
    public String getPublicMessage(String userId, int limit, int page){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        IPage<PublicMessage> publicMessageIPage = new Page<>(page, limit);
        int count = iPublicMessageService.count(qw);
        List<PublicMessage> publicMessageList = iPublicMessageService.page(publicMessageIPage, qw).getRecords();

        List<Message> messageList = new ArrayList<>();
        for(int i = 0; i < publicMessageList.size(); i ++){
            String messageId = publicMessageList.get(i).getMessageId();
            QueryWrapper qwm = new QueryWrapper();
            qwm.eq("message_id", messageId);
            Message message = iMessageService.getOne(qwm);
            messageList.add(message);
        }

        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "根据userid查找publicmessage成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", messageList);
        return JSON.toJSONString(modelMap);

    }

    @GetMapping("/public_area")
    public String publicArea(int limit, int page){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.orderByDesc("good_number", "favorite_number");
        IPage<PublicMessage> publicMessageIPage = new Page<>(page, limit);
        int count = iPublicMessageService.count(qw);
        List<PublicMessage> publicMessageList = iPublicMessageService.page(publicMessageIPage, qw).getRecords();

        List<PublicArea> messageList = new ArrayList<>();
        for(int i = 0; i < publicMessageList.size(); i ++){
            String messageId = publicMessageList.get(i).getMessageId();
            QueryWrapper qwm = new QueryWrapper();
            qwm.eq("message_id", messageId);

            Message message = iMessageService.getOne(qwm);

            PublicArea publicArea = new PublicArea();
            publicArea.setId(message.getId());
            publicArea.setUserId(message.getUserId());
            publicArea.setMessageId(messageId);
            publicArea.setTitle(message.getTitle());
            publicArea.setContext(message.getContext());
            publicArea.setWriteTime(message.getWriteTime());
            publicArea.setSendTime(message.getSendTime());
            publicArea.setGood_number(publicMessageList.get(i).getGoodNumber());
            publicArea.setFavorite_number(publicMessageList.get(i).getFavoriteNumber());
            publicArea.setIsSend(message.getIsSend());
            messageList.add(publicArea);
        }

        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "生成public_area成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", messageList);
        return JSON.toJSONString(modelMap);
    }

    @GetMapping("/getGoodNumber")
    public String getGoodNumber(String messageId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", messageId);
        PublicMessage publicMessage = iPublicMessageService.getOne(qw);
        modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
        modelMap.addAttribute("data", publicMessage.getGoodNumber());
        modelMap.addAttribute("result", "success");
        modelMap.addAttribute("description", "获取点赞数成功");
        return JSON.toJSONString(modelMap);
    }

    @GetMapping("/getFavoriteNumber")
    public String getFavoriteNumber(String messageId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", messageId);
        PublicMessage publicMessage = iPublicMessageService.getOne(qw);
        modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
        modelMap.addAttribute("data", publicMessage.getFavoriteNumber());
        modelMap.addAttribute("result", "success");
        modelMap.addAttribute("description", "获取收藏数成功");
        return JSON.toJSONString(modelMap);
    }

    @PutMapping("/goodNumber")
    public String updateGoodNumber(String messageId, int goodNumber){
        ModelMap modelMap = new ModelMap();
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("message_id", messageId);
        uw.set("good_number", goodNumber);

        boolean res = iPublicMessageService.update(uw);
        if (res){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "修改成功");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.UPDATE_GOODNUMBER_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "修改失败");
        }
        return JSON.toJSONString(modelMap);
    }

    @PutMapping("/favoriteNumber")
    public String updateFavoriteNumber(String messageId, int favoriteNumber){
        ModelMap modelMap = new ModelMap();
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("message_id", messageId);
        uw.set("favorite_number", favoriteNumber);

        boolean res = iPublicMessageService.update(uw);
        if (res){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "修改成功");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.UPDATE_FAVORITE_NUMBER_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "修改失败");
        }
        return JSON.toJSONString(modelMap);
    }

}
