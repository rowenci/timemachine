package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.entity.UserBlackList;
import com.rowenci.timemachine.entity.UserInBlack;
import com.rowenci.timemachine.entity.VipUser;
import com.rowenci.timemachine.service.*;
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
@RequestMapping("/timemachine/user-black-list")
public class UserBlackListController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IUserBlackListService iUserBlackListService;

    @Resource
    private IUserService iUserService;

    @Resource
    private IVipUserService iVipUserService;

    @Resource
    private IMessageService iMessageService;

    @Resource
    private IPublicMessageService iPublicMessageService;

    /**
     * 封禁用户
     * @param userBlackList
     * @return
     */
    @PostMapping("/")
    public String addUserBlack(UserBlackList userBlackList){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("ban_user_id", userBlackList.getBanUserId());
        UserBlackList dbuserBlack = iUserBlackListService.getOne(qw);
        if (dbuserBlack != null){
            modelMap.addAttribute("code", serviceCodeInfo.ADD_USER_BLACK_LIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "添加失败");
        }else {
            boolean res = iUserBlackListService.save(userBlackList);
            if (res){
                modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "success");
                modelMap.addAttribute("description", "添加成功");
            }else {
                modelMap.addAttribute("code", serviceCodeInfo.ADD_USER_BLACK_LIST_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "添加失败");
            }
        }
        return JSON.toJSONString(modelMap);

    }

    @GetMapping("/")
    public String getUserBlackList(int limit, int page){
        ModelMap modelMap = new ModelMap();
        IPage<UserBlackList> userBlackListIPage = new Page<>(page, limit);
        int count = iUserBlackListService.count();

        List<UserBlackList> userBlackLists = iUserBlackListService.page(userBlackListIPage).getRecords();

        List<UserInBlack> userInBlacks = new ArrayList<>();

        for(int i = 0; i < userBlackLists.size(); i ++){

            QueryWrapper qw = new QueryWrapper();
            qw.eq("user_id", userBlackLists.get(i).getBanUserId());


            VipUser vipUser = iVipUserService.getOne(qw);
            User user = iUserService.getOne(qw);

            int messageNumber = iMessageService.count(qw);
            int publicMessageNumber = iPublicMessageService.count(qw);

            if (vipUser == null){
                UserInBlack userInBlack = new UserInBlack(user.getUserId(), user.getAccount(), user.getLogupTime(), "否",  messageNumber, publicMessageNumber, userBlackLists.get(i).getTime(), userBlackLists.get(i).getReason());
                userInBlacks.add(userInBlack);
            }else {
                UserInBlack userInBlack = new UserInBlack(user.getUserId(), user.getAccount(), user.getLogupTime(), "是",  messageNumber, publicMessageNumber, userBlackLists.get(i).getTime(), userBlackLists.get(i).getReason());
                userInBlacks.add(userInBlack);
            }

        }

        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "封禁用户查找成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", userInBlacks);
        return JSON.toJSONString(modelMap);
    }

    @DeleteMapping("/")
    public String cancelBlack(String userId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("ban_user_id", userId);
        if (iUserBlackListService.getOne(qw) == null){
            modelMap.addAttribute("code", serviceCodeInfo.DEL_USER_BLACK_LIST_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "该黑名单不存在");
        }else {
            boolean res = iUserBlackListService.remove(qw);
            if (res){
                modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "success");
                modelMap.addAttribute("description", "删除黑名单成功");
            }else {
                modelMap.addAttribute("code", serviceCodeInfo.DEL_USER_BLACK_LIST_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "删除黑名单失败");
            }
        }

        return JSON.toJSONString(modelMap);
    }

}
