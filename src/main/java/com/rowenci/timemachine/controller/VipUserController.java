package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.VipUser;
import com.rowenci.timemachine.service.IVipUserService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-10
 */
@RestController
@RequestMapping("/timemachine/vip-user")
public class VipUserController {



    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IVipUserService iVipUserService;

    /**
     * 获取用户vip信息
     * @param userId
     * @return
     */
    @GetMapping("/")
    public String getUserVipInfo(String userId) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        VipUser vipUser = iVipUserService.getOne(qw);
        if (vipUser == null) {
            //没有会员信息
            modelMap.addAttribute("code", serviceCodeInfo.NO_USER);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "没有该用户的会员信息");
            return JSON.toJSONString(modelMap);
        } else {
            //有会员信息
            //判断是否过期
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = sdf.format(date);

            try {
                //将日期转成Date对象作比较
                Date fomatDate1 = sdf.parse(dateNow);
                Date fomatDate2 = sdf.parse(vipUser.getEndTime());
                //比较两个日期
                int result = fomatDate2.compareTo(fomatDate1);
                //如果日期相等返回0
                if (result > 0) {
                    modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                    modelMap.addAttribute("data", vipUser);
                    modelMap.addAttribute("result", "success");
                    modelMap.addAttribute("description", "查找成功");
                    return JSON.toJSONString(modelMap);
                } else {
                    //过期
                    if (vipUser.getStatus() == 1) {
                        //更改用户vip信息
                        UpdateWrapper uw = new UpdateWrapper();
                        uw.eq("user_id", userId);
                        uw.set("status", 0);
                        iVipUserService.update(uw);
                        vipUser.setStatus(0);
                    }
                    modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                    modelMap.addAttribute("data", vipUser);
                    modelMap.addAttribute("result", "success");
                    modelMap.addAttribute("description", "查找成功");
                    return JSON.toJSONString(modelMap);
                }
            } catch (ParseException e) {
                //系统错误
                modelMap.addAttribute("code", serviceCodeInfo.GET_VIPUSER_INFO_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "系统错误");
                return JSON.toJSONString(modelMap);
            }
        }
    }

    /**
     * check是否是会员
     *
     * @param userId
     * @return
     */
    @GetMapping("/check")
    public String checkVip(String userId) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        VipUser vipUser = iVipUserService.getOne(qw);
        if (vipUser == null) {
            //没有会员信息
            modelMap.addAttribute("code", serviceCodeInfo.NO_USER);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "没有该用户的会员信息");
            return JSON.toJSONString(modelMap);
        } else {
            //有会员信息
            //判断是否过期
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = sdf.format(date);

            try {
                //将日期转成Date对象作比较
                Date fomatDate1 = sdf.parse(dateNow);
                Date fomatDate2 = sdf.parse(vipUser.getEndTime());
                //比较两个日期
                int result = fomatDate2.compareTo(fomatDate1);
                //如果日期相等返回0
                if (result > 0) {
                    //过期
                    if (vipUser.getStatus() == 1) {
                        UpdateWrapper uw = new UpdateWrapper();
                        uw.eq("user_id", userId);
                        uw.set("status", 0);
                        iVipUserService.update(uw);
                        vipUser.setStatus(0);
                    }
                    modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                    modelMap.addAttribute("data", vipUser.getStatus());
                    modelMap.addAttribute("result", "success");
                    modelMap.addAttribute("description", "过期");
                    return JSON.toJSONString(modelMap);
                } else if (result == 0) {
                    //即将过期
                    modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                    modelMap.addAttribute("data", vipUser.getStatus());
                    modelMap.addAttribute("result", "success");
                    modelMap.addAttribute("description", "今天将过期");
                    return JSON.toJSONString(modelMap);
                } else {
                    //没有过期
                    modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                    modelMap.addAttribute("data", vipUser.getStatus());
                    modelMap.addAttribute("result", "success");
                    modelMap.addAttribute("description", "没有过期");
                    return JSON.toJSONString(modelMap);
                }

            } catch (ParseException e) {
                //系统错误
                modelMap.addAttribute("code", serviceCodeInfo.GET_VIPUSER_INFO_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "系统错误");
                return JSON.toJSONString(modelMap);
            }
        }
    }
}
