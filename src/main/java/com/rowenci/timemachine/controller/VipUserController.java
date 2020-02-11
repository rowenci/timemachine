package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.VipOrder;
import com.rowenci.timemachine.entity.VipOrderSuccess;
import com.rowenci.timemachine.entity.VipUser;
import com.rowenci.timemachine.service.IVipOrderService;
import com.rowenci.timemachine.service.IVipOrderSuccessService;
import com.rowenci.timemachine.service.IVipUserService;
import com.rowenci.timemachine.util.AliPayService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-10
 */
@Slf4j
@RestController
@RequestMapping("/timemachine/vip-user")
public class VipUserController {


    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IVipUserService iVipUserService;

    @Resource
    private AliPayService aliPayService;

    @Resource
    private IVipOrderService OrderService;

    @Resource
    private IVipOrderSuccessService OrderSuccessService;

    /**
     * 获取用户vip信息
     *
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
                if (result >= 0) {
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

    @GetMapping("/aliPayReturnUrl/")
    public String aliPayReturnUrl(HttpServletRequest request, HttpServletRequest response) throws Exception {

        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            System.out.println(name + "----------" + valueStr);
        }

        String orderId = request.getParameter("out_trade_no");
        String time = request.getParameter("timestamp");
        String tradeNo = request.getParameter("trade_no");

        QueryWrapper orderQW = new QueryWrapper();
        orderQW.eq("order_id", orderId);
        VipOrder vipOrder = OrderService.getOne(orderQW);

        if (vipOrder != null) {
            //充值成功
            //保存成功订单信息
            VipOrderSuccess vipOrderSuccess = new VipOrderSuccess();
            vipOrderSuccess.setOrderId(orderId);
            vipOrderSuccess.setTime(time);
            vipOrderSuccess.setTradeNo(tradeNo);
            boolean res = OrderSuccessService.save(vipOrderSuccess);
            if (res) {
                //订单信息保存成功
                //添加会员信息
                //判断是否为第一次充值
                QueryWrapper vipQW = new QueryWrapper();
                vipQW.eq("user_id", vipOrder.getUserId());
                VipUser vipUser = iVipUserService.getOne(vipQW);
                if (vipOrder == null) {
                    //第一次充值
                    vipUser.setUserId(vipOrder.getUserId());

                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNow = sdf.format(date);
                    vipUser.setStartTime(dateNow);
                    switch (vipOrder.getVipMode()) {
                        case 1: {
                            //一个月
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 30 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                        case 2: {
                            //两个月
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 60 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                        case 3: {
                            //三个月
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 90 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                        case 4: {
                            //半年
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 180 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                    }
                    vipUser.setStatus(1);
                    iVipUserService.save(vipUser);
                } else {
                    //有会员信息
                    //续费会员
                    //更新end_time字段
                    //判断是否过期
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNow = sdf.format(date);
                    UpdateWrapper uw = new UpdateWrapper();
                    uw.eq("user_id", vipOrder.getUserId());
                    try {
                        //将日期转成Date对象作比较
                        Date fomatDate1 = sdf.parse(dateNow);
                        Date fomatDate2 = sdf.parse(vipUser.getEndTime());
                        //比较两个日期
                        int result = fomatDate2.compareTo(fomatDate1);
                        //如果日期相等返回0
                        if (result < 0) {
                            //过期
                            // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
                            Date date1 = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date1);
                            switch (vipOrder.getVipMode()) {
                                case 1: {
                                    //一个月
                                    calendar.add(Calendar.DATE, 30);
                                    break;
                                }
                                case 2: {
                                    //两个月
                                    calendar.add(Calendar.DATE, 60);
                                    break;
                                }
                                case 3: {
                                    //三个月
                                    calendar.add(Calendar.DATE, 90);
                                    break;
                                }
                                case 4: {
                                    //半年
                                    calendar.add(Calendar.DATE, 180);
                                    break;
                                }
                            }
                            Date newEndTimeDate = calendar.getTime();
                            uw.set("end_time", sdf.format(newEndTimeDate));
                            uw.set("status", 1);
                            iVipUserService.update(uw);
                        } else if (result >= 0) {
                            //即将过期或未过期
                            String str = vipUser.getEndTime();
                            // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
                            Date date1 = sdf.parse(str, new ParsePosition(0));
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date1);
                            switch (vipOrder.getVipMode()) {
                                case 1: {
                                    //一个月
                                    calendar.add(Calendar.DATE, 30);
                                    break;
                                }
                                case 2: {
                                    //两个月
                                    calendar.add(Calendar.DATE, 60);
                                    break;
                                }
                                case 3: {
                                    //三个月
                                    calendar.add(Calendar.DATE, 90);
                                    break;
                                }
                                case 4: {
                                    //半年
                                    calendar.add(Calendar.DATE, 180);
                                    break;
                                }
                            }
                            Date newEndTimeDate = calendar.getTime();
                            uw.set("end_time", sdf.format(newEndTimeDate));
                            iVipUserService.update(uw);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return "充值成功";
            } else {
                return "充值失败";
            }
        }
        return "充值失败";
    }

    @GetMapping("/aliPayNo/")
    public String aliPayNo(HttpServletRequest request, HttpServletRequest response) throws Exception {

        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            System.out.println(name + "----------" + valueStr);
        }

        String orderId = request.getParameter("out_trade_no");
        String time = request.getParameter("timestamp");
        String tradeNo = request.getParameter("trade_no");

        QueryWrapper orderQW = new QueryWrapper();
        orderQW.eq("order_id", orderId);
        VipOrder vipOrder = OrderService.getOne(orderQW);

        if (vipOrder != null) {
            //充值成功
            //保存成功订单信息
            VipOrderSuccess vipOrderSuccess = new VipOrderSuccess();
            vipOrderSuccess.setOrderId(orderId);
            vipOrderSuccess.setTime(time);
            vipOrderSuccess.setTradeNo(tradeNo);
            boolean res = OrderSuccessService.save(vipOrderSuccess);
            if (res) {
                //订单信息保存成功
                //添加会员信息
                //判断是否为第一次充值
                QueryWrapper vipQW = new QueryWrapper();
                vipQW.eq("user_id", vipOrder.getUserId());
                VipUser vipUser = iVipUserService.getOne(vipQW);
                if (vipOrder == null) {
                    //第一次充值
                    vipUser.setUserId(vipOrder.getUserId());

                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNow = sdf.format(date);
                    vipUser.setStartTime(dateNow);
                    switch (vipOrder.getVipMode()) {
                        case 1: {
                            //一个月
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 30 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                        case 2: {
                            //两个月
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 60 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                        case 3: {
                            //三个月
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 90 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                        case 4: {
                            //半年
                            vipUser.setEndTime(sdf.format(new Date(date.getTime() + 180 * 24 * 60 * 60 * 1000)));
                            break;
                        }
                    }
                    vipUser.setStatus(1);
                    iVipUserService.save(vipUser);
                } else {
                    //有会员信息
                    //续费会员
                    //更新end_time字段
                    //判断是否过期
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNow = sdf.format(date);
                    UpdateWrapper uw = new UpdateWrapper();
                    uw.eq("user_id", vipOrder.getUserId());
                    try {
                        //将日期转成Date对象作比较
                        Date fomatDate1 = sdf.parse(dateNow);
                        Date fomatDate2 = sdf.parse(vipUser.getEndTime());
                        //比较两个日期
                        int result = fomatDate2.compareTo(fomatDate1);
                        //如果日期相等返回0
                        if (result < 0) {
                            //过期
                            // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
                            Date date1 = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date1);
                            switch (vipOrder.getVipMode()) {
                                case 1: {
                                    //一个月
                                    calendar.add(Calendar.DATE, 30);
                                    break;
                                }
                                case 2: {
                                    //两个月
                                    calendar.add(Calendar.DATE, 60);
                                    break;
                                }
                                case 3: {
                                    //三个月
                                    calendar.add(Calendar.DATE, 90);
                                    break;
                                }
                                case 4: {
                                    //半年
                                    calendar.add(Calendar.DATE, 180);
                                    break;
                                }
                            }
                            Date newEndTimeDate = calendar.getTime();
                            uw.set("end_time", sdf.format(newEndTimeDate));
                            uw.set("status", 1);
                            iVipUserService.update(uw);
                        } else if (result >= 0) {
                            //即将过期或未过期
                            String str = vipUser.getEndTime();
                            // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
                            Date date1 = sdf.parse(str, new ParsePosition(0));
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date1);
                            switch (vipOrder.getVipMode()) {
                                case 1: {
                                    //一个月
                                    calendar.add(Calendar.DATE, 30);
                                    break;
                                }
                                case 2: {
                                    //两个月
                                    calendar.add(Calendar.DATE, 60);
                                    break;
                                }
                                case 3: {
                                    //三个月
                                    calendar.add(Calendar.DATE, 90);
                                    break;
                                }
                                case 4: {
                                    //半年
                                    calendar.add(Calendar.DATE, 180);
                                    break;
                                }
                            }
                            Date newEndTimeDate = calendar.getTime();
                            uw.set("end_time", sdf.format(newEndTimeDate));
                            iVipUserService.update(uw);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return "充值成功";
            } else {
                return "充值失败";
            }
        }
        return "充值失败";
    }

    @GetMapping("/aliPay")
    public String alipay(String userId, Integer money, Integer vipMode) throws AlipayApiException {
        return aliPayService.aliPay(userId, money, vipMode);
    }

}
