package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.rowenci.timemachine.entity.SendMessage;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import com.rowenci.timemachine.util.TokenUtil.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/")
    public String logUp(@RequestBody User user){            /* do not forget the @RequestBody */
        UUID uuid = UUID.randomUUID();                      /* it's necessary for receiving json objects */
        user.setUserId(Integer.parseInt(uuid.toString()));
        int logup_code = iUserService.logUp(user);
        SendMessage sendMessage = new SendMessage();
        if (logup_code == 2){
            sendMessage.initMessage(sendMessage, logup_code, "", "success", "");
        }else {
            sendMessage.initMessage(sendMessage, logup_code, "", "error", "");
        }
        sendMessage.setDescription(serviceCodeInfo.getCodeInfo(logup_code));
        return JSON.toJSONString(sendMessage);
    }

    /**
     * 登陆
     * @param account
     * @param password
     * @return
     */
    @GetMapping("/")
    public String logIn(String account, String password){
        SendMessage sendMessage = new SendMessage();
        User user = iUserService.getUserByAccount(account);
        if (user == null){
            //用户不存在
            sendMessage.initMessage(sendMessage, ServiceCodeInfo.LOGIN_ERROR,"", "error", "账号错误");
        }else {
            if (user.getPassword().equals(password)){
                //登陆成功
                String token = TokenUtil.createToken();
                Map infoMap = new HashMap();
                infoMap.put("user", user);
                infoMap.put("token", token);
                sendMessage.initMessage(sendMessage, ServiceCodeInfo.SUCCESS, infoMap, "success", "登陆成功");
            }else {
                //密码错误
                sendMessage.initMessage(sendMessage, ServiceCodeInfo.LOGIN_ERROR,"", "error", "密码错误");
            }
        }
        return JSON.toJSONString(sendMessage);
    }

    @GetMapping("/logOut")
    public String logOut(String token){
        SendMessage sendMessage = new SendMessage();
        //消除token缓存


        sendMessage.initMessage(sendMessage, ServiceCodeInfo.SUCCESS, "", "success", "登陆成功");
        return JSON.toJSONString(sendMessage);
    }

    /**
     * 根据user_id查找用户信息
     * @param user_id
     * @return
     */
    @GetMapping("/{user_id}")
    public String getUserById(@PathVariable("user_id") int user_id){
        SendMessage sendMessage = new SendMessage();
        User user = iUserService.getUserById(user_id);
        if (user == null){
            sendMessage.initMessage(sendMessage, ServiceCodeInfo.NO_USER, "", "error", "");
        }
        else {
            sendMessage.initMessage(sendMessage, ServiceCodeInfo.SUCCESS, user, "success", "");
        }
        return JSON.toJSONString(sendMessage);
    }

    /**
     * 修改个人信息
     * @param user
     * @return
     */
    @PutMapping("/")
    public String changeInfo(@RequestBody User user){
        SendMessage sendMessage = new SendMessage();
        int changeInfo_code = iUserService.changeInfo(user);
        sendMessage.setCode(changeInfo_code);
        if (changeInfo_code < 0){
            sendMessage.initMessage(sendMessage, changeInfo_code, "", "error", "");
        }else {
            User selectUser = iUserService.getUserByAccount(user.getAccount());
            sendMessage.initMessage(sendMessage, changeInfo_code, selectUser, "success", "");
        }
        return JSON.toJSONString(sendMessage);
    }

    /**
     * 修改密码
     * @param account
     * @param password
     * @return
     */
    @PutMapping("/password")
    public String changePWD(String account, String password){
        SendMessage sendMessage = new SendMessage();
        int changePWD_code = iUserService.changePWD(account, password);
        if (changePWD_code > 0){
            sendMessage.initMessage(sendMessage, changePWD_code, "", "success", "");
        }
        else {
            sendMessage.initMessage(sendMessage, changePWD_code, "", "error", "");
        }
        return JSON.toJSONString(sendMessage);
    }

}
