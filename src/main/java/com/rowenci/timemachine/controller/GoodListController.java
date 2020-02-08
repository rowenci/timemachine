package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.GoodList;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.service.IGoodListService;
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
 * @since 2020-02-07
 */
@RestController
@RequestMapping("/timemachine/good-list")
public class GoodListController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IGoodListService iGoodListService;

    @Resource
    private IPublicMessageService iPublicMessageService;

    @Resource
    private IMessageService iMessageService;

    @PostMapping("/")
    public String addGoodList(GoodList goodList){
        ModelMap modelMap = new ModelMap();
        boolean res = iGoodListService.save(goodList);
        if (res){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "添加成功");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.ADD_GOODLIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "添加失败");
        }
        return JSON.toJSONString(modelMap);
    }

    @GetMapping("/")
    public String getGoodList(String userId, int page, int limit){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        IPage<GoodList> goodListIPage = new Page<>(page, limit);
        List<GoodList> goodListList = iGoodListService.page(goodListIPage, qw).getRecords();

        List<Message> messageList = new ArrayList<>();

        for(int i = 0; i < goodListList.size(); i ++){

            QueryWrapper qBan = new QueryWrapper();
            qBan.eq("message_id", goodListList.get(i).getMessageId());
            if (iPublicMessageService.getOne(qBan).getBanned() == 1){
                //跳出循环
                continue;
            }

            String messageId = goodListList.get(i).getMessageId();
            QueryWrapper qwm = new QueryWrapper();
            qwm.eq("message_id", messageId);
            Message message = iMessageService.getOne(qwm);
            messageList.add(message);
        }
        int count = messageList.size();

        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "查找成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", messageList);
        return JSON.toJSONString(modelMap);
    }

    @GetMapping("/check")
    public String getGoodRecordByUserId(String userId, String messageId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        qw.eq("message_id", messageId);
        GoodList goodList = iGoodListService.getOne(qw);
        if (goodList == null){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "可以添加");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.ADD_MESSAGE_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "不可添加");
        }
        return JSON.toJSONString(modelMap);
    }

    @DeleteMapping("/")
    public String delGoodList(String userId, String messageId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        qw.eq("message_id", messageId);
        boolean res = iGoodListService.remove(qw);
        if (res){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "删除成功");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.DEL_GOODLIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "删除失败");
        }
        return JSON.toJSONString(modelMap);
    }

}
