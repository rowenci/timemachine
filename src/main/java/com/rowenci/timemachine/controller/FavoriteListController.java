package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.FavoriteList;
import com.rowenci.timemachine.entity.GoodList;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.service.IFavoriteListService;
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
@RequestMapping("/timemachine/favorite-list")
public class FavoriteListController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IFavoriteListService iFavoriteListService;

    @Resource
    private IMessageService iMessageService;

    @Resource
    private IPublicMessageService iPublicMessageService;

    @PostMapping("/")
    public String addFavoriteList(FavoriteList favoriteList){
        ModelMap modelMap = new ModelMap();
        boolean res = iFavoriteListService.save(favoriteList);
        if (res){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "添加成功");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.UPDATE_FAVORITELIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "添加失败");
        }
        return JSON.toJSONString(modelMap);
    }

    @GetMapping("/")
    public String getFavoriteList(String userId, int page, int limit){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        IPage<FavoriteList> favoriteListIPage = new Page<>(page, limit);
        List<FavoriteList> favoriteLists = iFavoriteListService.page(favoriteListIPage, qw).getRecords();

        List<Message> messageList = new ArrayList<>();

        for(int i = 0; i < favoriteLists.size(); i ++){
            QueryWrapper qBan = new QueryWrapper();
            qBan.eq("message_id", favoriteLists.get(i).getMessageId());
            if (iPublicMessageService.getOne(qBan).getBanned() == 1){
                continue;
            }

            String messageId = favoriteLists.get(i).getMessageId();
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
        FavoriteList favoriteList = iFavoriteListService.getOne(qw);
        if (favoriteList == null){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "可以收藏");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.ADD_MESSAGE_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "不可收藏");
        }
        return JSON.toJSONString(modelMap);
    }

    @DeleteMapping("/")
    public String delFavoriteList(String userId, String messageId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        qw.eq("message_id", messageId);
        boolean res = iFavoriteListService.remove(qw);
        if (res){
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "删除成功");
        }else {
            modelMap.addAttribute("code", serviceCodeInfo.DEL_FAVORITE_LIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "删除失败");
        }
        return JSON.toJSONString(modelMap);
    }

}
