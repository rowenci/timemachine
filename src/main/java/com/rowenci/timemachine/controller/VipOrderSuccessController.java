package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.OrderSend;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.entity.VipOrder;
import com.rowenci.timemachine.entity.VipOrderSuccess;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.service.IVipOrderService;
import com.rowenci.timemachine.service.IVipOrderSuccessService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@RequestMapping("/timemachine/vip-order-success")
public class VipOrderSuccessController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IVipOrderSuccessService iVipOrderSuccessService;

    @Resource
    private IVipOrderService iVipOrderService;

    @Resource
    private IUserService iUserService;

    @GetMapping("/")
    public String getVipOrder(int limit, int page){
        ModelMap modelMap = new ModelMap();
        IPage<VipOrderSuccess> vipOrderIPage = new Page<>(page, limit);
        int count = iVipOrderSuccessService.count();

        List<VipOrderSuccess> vipOrderSuccessList = iVipOrderSuccessService.page(vipOrderIPage).getRecords();

        List<OrderSend> orderSendList = new ArrayList<>();
        for (int i = 0; i < vipOrderSuccessList.size(); i ++){
            QueryWrapper orderQW = new QueryWrapper();
            orderQW.eq("order_id", vipOrderSuccessList.get(i).getOrderId());
            VipOrder vipOrder = iVipOrderService.getOne(orderQW);
            QueryWrapper userQW = new QueryWrapper();
            userQW.eq("user_id", vipOrder.getUserId());
            User user = iUserService.getOne(userQW);

            OrderSend orderSend = new OrderSend(vipOrderSuccessList.get(i).getOrderId(), vipOrderSuccessList.get(i).getTradeNo(), user.getAccount(), vipOrderSuccessList.get(i).getTime(), vipOrder.getMoney(), vipOrder.getMoney());
            orderSendList.add(orderSend);
        }
        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "查找订单成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", orderSendList);
        return JSON.toJSONString(modelMap);
    }

}
