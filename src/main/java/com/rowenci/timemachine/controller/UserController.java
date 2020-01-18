package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.rowenci.timemachine.entity.SendMessage;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  UserController
 * </p>
 *
 * @author rowenci
 * @since 2020-01-15
 */
@RestController
@RequestMapping("/timemachine/user")
public class UserController {

    @Resource
    private IUserService iUserService;

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @PostMapping("/")
    public String logUp(@RequestBody User user){            /* do not forget the @RequestBody */
        int logup_code = iUserService.logUp(user);          /* it's necessary for receiving json objects */
        SendMessage sendMessage = new SendMessage();
        sendMessage.setCode(logup_code);
        if (logup_code == 2){
            sendMessage.setResult("success");
        }else {
            sendMessage.setResult("error");
        }
        sendMessage.setDescription(serviceCodeInfo.getCodeInfo(logup_code));
        return JSON.toJSONString(sendMessage);
    }

    @GetMapping("/")
    public String logIn(String account, String password){
        SendMessage sendMessage = new SendMessage();
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("account", account);
        loginMap.put("password", password);
        int login_code = iUserService.logIn(loginMap);
        if (login_code > 0){
            sendMessage.setCode(login_code);
            sendMessage.setData(iUserService.getUserByAccount(account));
            sendMessage.setResult("success");
            return JSON.toJSONString(sendMessage);
        }else {
            sendMessage.setCode(login_code);
            sendMessage.setResult("error");
            return JSON.toJSONString(sendMessage);
        }
    }

    @PutMapping("/")
    public String changeInfo(@RequestBody User user){
        SendMessage sendMessage = new SendMessage();
        int changeInfo_code = iUserService.changeInfo(user);
        sendMessage.setCode(changeInfo_code);
        if (changeInfo_code == 3){
            sendMessage.setResult("success");
        }else {
            sendMessage.setResult("error");
        }
        iUserService.getUserByAccount(user.getAccount());
        sendMessage.setData(user);
        return JSON.toJSONString(sendMessage);
    }

}
