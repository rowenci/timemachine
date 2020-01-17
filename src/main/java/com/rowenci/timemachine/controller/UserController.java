package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.util.CodeInfo.CodeInfo;
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
    private CodeInfo codeInfo;

    @PostMapping("/")
    public String logUp(User user){
        return codeInfo.getCodeInfo(iUserService.logUp(user));
    }

    @GetMapping("/")
    public String logIn(String account, String password){
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("account", account);
        loginMap.put("password", password);
        if (iUserService.logIn(loginMap) == 1){
            return JSON.toJSONString(iUserService.getUserByAccount(account));
        }else {
            return String.valueOf(codeInfo.LOGIN_ERROR);
        }
    }

    @PutMapping("/")
    public String changeInfo(User user){
        return String.valueOf(iUserService.changeInfo(user));
    }

}
