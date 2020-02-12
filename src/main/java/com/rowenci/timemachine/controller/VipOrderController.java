package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.entity.VipOrder;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.service.IVipOrderService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @since 2020-02-11
 */
@RestController
@RequestMapping("/timemachine/vip-order")
public class VipOrderController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IVipOrderService iVipOrderService;

    @Resource
    private IUserService iUserService;

    @GetMapping("/")
    public String getVipOrder(int limit, int page){
        ModelMap modelMap = new ModelMap();
        IPage<VipOrder> vipOrderIPage = new Page<>(page, limit);
        int count = iVipOrderService.count();

        List<VipOrder> vipOrderList = iVipOrderService.page(vipOrderIPage).getRecords();
        for (int i = 0; i < vipOrderList.size(); i ++){
            QueryWrapper qw = new QueryWrapper();
            qw.eq("user_id", vipOrderList.get(i).getUserId());
            User user = iUserService.getOne(qw);
            vipOrderList.get(i).setUserId(user.getAccount());
        }
        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "查找订单成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", vipOrderList);
        return JSON.toJSONString(modelMap);
    }

}
