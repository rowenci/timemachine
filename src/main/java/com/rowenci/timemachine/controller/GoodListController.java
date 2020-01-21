package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.rowenci.timemachine.entity.GoodList;
import com.rowenci.timemachine.entity.SendMessage;
import com.rowenci.timemachine.service.IGoodListService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-01-19
 */
@RestController
@RequestMapping("/timemachine/good-list")
public class GoodListController {

    @Resource
    private IGoodListService iGoodListService;

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @PostMapping("/")
    public String addGood(@RequestBody GoodList goodList) {
        SendMessage sendMessage = new SendMessage();
        iGoodListService.addGood(goodList);
        sendMessage.initMessage(serviceCodeInfo.SUCCESS, "", "success", "");
        return JSON.toJSONString(sendMessage);
    }

    @GetMapping("/delete")
    public String deleteGood(@RequestBody GoodList goodList) {
        SendMessage sendMessage = new SendMessage();
        iGoodListService.deleteGood(goodList);
        sendMessage.initMessage(serviceCodeInfo.SUCCESS, "", "success", "");
        return JSON.toJSONString(sendMessage);
    }

    @GetMapping("/")
    public String getGoodList(int user_id) {
        SendMessage sendMessage = new SendMessage();
        List<GoodList> list = iGoodListService.getGoodList(user_id);
        sendMessage.initMessage(serviceCodeInfo.SUCCESS, list, "success", "");
        return JSON.toJSONString(sendMessage);
    }

}
