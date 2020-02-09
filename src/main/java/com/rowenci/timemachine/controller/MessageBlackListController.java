package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.*;
import com.rowenci.timemachine.service.IManagerService;
import com.rowenci.timemachine.service.IMessageBlackListService;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.service.IPublicMessageService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
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
 * @since 2020-02-09
 */
@RestController
@RequestMapping("/timemachine/message-black-list")
public class MessageBlackListController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IMessageBlackListService iMessageBlackListService;

    @Resource
    private IPublicMessageService iPublicMessageService;

    @Resource
    private IMessageService iMessageService;

    @Resource
    private IManagerService iManagerService;

    /**
     * 添加黑名单
     * @param messageId
     * @param managerId
     * @param time
     * @return
     */
    @PostMapping("/")
    public String addBlack(String messageId, String managerId, String time){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("ban_message_id", messageId);
        if (iMessageBlackListService.getOne(qw) != null){
            modelMap.addAttribute("code", serviceCodeInfo.ADD_MESSAGE_BLACK_LIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "该信件已经被加入黑名单");
        }else {
            MessageBlackList messageBlackList = new MessageBlackList();
            messageBlackList.setBanMessageId(messageId);
            messageBlackList.setManagerId(managerId);
            messageBlackList.setReason(1);
            messageBlackList.setTime(time);

            //添加blacklist记录
            boolean res = iMessageBlackListService.save(messageBlackList);

            if (res){
                //添加成功
                //修改publicmessage中的banned信息
                UpdateWrapper uw = new UpdateWrapper();
                uw.eq("message_id", messageId);
                uw.set("banned", 1);
                iPublicMessageService.update(uw);

                modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "success");
                modelMap.addAttribute("description", "成功加入黑名单");
            }else {
                modelMap.addAttribute("code", serviceCodeInfo.ADD_MESSAGE_BLACK_LIST_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "加入黑名单失败");
            }
        }
        return JSON.toJSONString(modelMap);
    }

    @GetMapping("/")
    public String getMessageBlackList(int limit, int page){
        ModelMap modelMap = new ModelMap();
        IPage<MessageBlackList> messageBlackListIPage = new Page<>(page, limit);
        int count = iMessageBlackListService.count();
        List<MessageBlackList> messageBlackLists = iMessageBlackListService.page(messageBlackListIPage).getRecords();

        //获取信件信息
        List<MessageInBlack> messageList = new ArrayList<>();
        for(int i = 0; i < messageBlackLists.size(); i ++){
            String messageId = messageBlackLists.get(i).getBanMessageId();
            QueryWrapper qwm = new QueryWrapper();
            qwm.eq("message_id", messageId);

            Message message = iMessageService.getOne(qwm);
            QueryWrapper managerQW = new QueryWrapper();
            managerQW.eq("manager_id", messageBlackLists.get(i).getManagerId());
            Manager manager = iManagerService.getOne(managerQW);

            MessageInBlack messageInBlack = new MessageInBlack(messageBlackLists.get(i).getBanMessageId(), message.getTitle(), message.getContext(), message.getWriteTime(), message.getSendTime(), message.getIsSend(), messageBlackLists.get(i).getTime(), messageBlackLists.get(i).getReason(), manager.getAccount());

            messageList.add(messageInBlack);
        }
        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "生成message_black_list成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", messageList);
        return JSON.toJSONString(modelMap);

    }


    /**
     * 删除黑名单
     * @param messageId
     * @return
     */
    @DeleteMapping("/")
    public String cancelBlack(String messageId){

        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("ban_message_id", messageId);
        if (iMessageBlackListService.getOne(qw) == null){
            modelMap.addAttribute("code", serviceCodeInfo.DEL_MESSAGE_BLACK_LIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "该黑名单不存在");
        }else {
            boolean res = iMessageBlackListService.remove(qw);
            if (res){
                UpdateWrapper uw = new UpdateWrapper();
                uw.eq("message_id", messageId);
                uw.set("banned", 0);
                iPublicMessageService.update(uw);
                modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "success");
                modelMap.addAttribute("description", "删除黑名单成功");
            }else {
                modelMap.addAttribute("code", serviceCodeInfo.DEL_MESSAGE_BLACK_LIST_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "删除黑名单失败");
            }
        }

        return JSON.toJSONString(modelMap);

    }
}
