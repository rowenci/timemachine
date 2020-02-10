package com.rowenci.timemachine.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.util.QrCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/10 17:41
 */
@Controller
@RequestMapping("/timemachine/share")
public class ShareController {

    @Resource
    private IMessageService iMessageService;

    @Resource
    private IUserService iUserService;

    @RequestMapping("/shareMessage")
    public ModelAndView shareMessage(HttpServletRequest request, HttpServletResponse response){

        ModelAndView modelAndView = new ModelAndView();

        String messageId = request.getParameter("messageId");
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", messageId);
        Message message = iMessageService.getOne(qw);

        QueryWrapper userQW = new QueryWrapper();
        userQW.eq("user_id", message.getUserId());
        User user = iUserService.getOne(userQW);

        modelAndView.addObject("message", message);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("share");
        return modelAndView;
    }
}
