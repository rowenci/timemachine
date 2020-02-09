package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.SendMessage;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import com.rowenci.timemachine.util.CreateUUID;
import com.rowenci.timemachine.util.TokenUtil.TokenUtil;
import com.rowenci.timemachine.util.redis.RedisUtil;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-05
 */
@RestController
@RequestMapping("/timemachine/user")
public class UserController {

    @Resource
    private IUserService iUserService;

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private RedisUtil redisUtil;


    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/")
    public String logUp(User user) {
        ModelMap modelMap = new ModelMap();

        //判断账号是否已经存在
        QueryWrapper qw = new QueryWrapper();
        qw.eq("account", user.getAccount());
        if (iUserService.getOne(qw) != null){
            modelMap.addAttribute("code", serviceCodeInfo.LOGUP_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "该用户名已经存在");
            return JSON.toJSONString(modelMap);
        }

        //获取uuid
        CreateUUID createUUID = new CreateUUID();
        String userId = createUUID.create();
        user.setUserId(userId);

        //添加用户
        boolean res = iUserService.save(user);
        if (res) {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "添加用户成功");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.LOGUP_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "添加用户失败");
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * 登陆
     *
     * @param account
     * @param password
     * @return
     */
    @GetMapping("/")
    public String logIn(String account, String password) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("account", account);
        User user = iUserService.getOne(qw);
        if (user == null) {
            //用户不存在
            modelMap.addAttribute("code", serviceCodeInfo.LOGIN_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "");
        } else {
            if (user.getPassword().equals(password)) {

                //登陆成功
                String token = TokenUtil.createToken();

                //放入缓存<token, 用户id>并设置失效时间3600秒
                redisUtil.set(token, user.getUserId(), 3600);
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("token", token);
                infoMap.put("userId", user.getUserId());

                modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                modelMap.addAttribute("data", infoMap);
                modelMap.addAttribute("result", "success");
                modelMap.addAttribute("description", "登陆成功");

            } else {
                //密码错误
                modelMap.addAttribute("code", serviceCodeInfo.LOGIN_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "密码错误");
            }
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * 登出
     * @param token
     * @return
     */
    @GetMapping("/logOut")
    public String logOut(String token) {
        ModelMap modelMap = new ModelMap();
        //消除token缓存
        if (!redisUtil.hasKey(token)) {
            modelMap.addAttribute("code", serviceCodeInfo.UNKNOWN_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "token不存在");
        } else {
            redisUtil.del(token);
            modelMap.addAttribute("code", serviceCodeInfo.LOGOUT_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "退出成功");
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * 根据user_id查找用户信息
     *
     * @param user_id
     * @return
     */
    @GetMapping("/{user_id}")
    public String getUserById(@PathVariable("user_id") int user_id) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", user_id);
        User user = iUserService.getOne(qw);
        if (user == null) {
            modelMap.addAttribute("code", serviceCodeInfo.NO_USER);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "没有该用户");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", user);
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "查找成功");
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * 修改个人信息
     *
     * @param user
     * @return
     */
    @PutMapping("/")
    public String changeInfo(User user) {
        ModelMap modelMap = new ModelMap();
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("user_id", user.getUserId());
        boolean res = iUserService.update(user, uw);
        if (!res) {
            modelMap.addAttribute("code", serviceCodeInfo.NO_USER);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "用户不存在");
        } else {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("user_id", user.getUserId());
            User selectUser = iUserService.getOne(qw);
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", selectUser);
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "更改信息成功");
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * 修改密码
     * @param userId
     * @param password
     * @param question_id
     * @param answer
     * @return
     */
    @PutMapping("/password")
    public String changePWD(String userId, String password, int question_id, String answer) {
        ModelMap modelMap = new ModelMap();

        //获取用户信息
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        User user = iUserService.getOne(qw);
        int user_question = user.getQuestionId();
        String user_answer = user.getAnswer();

        //安全问题或者答案错误
        if (user_question != question_id || !user_answer.equals(answer)){
            modelMap.addAttribute("code", serviceCodeInfo.CHANGE_PASSWORD_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "安全问题或答案错误");
            return JSON.toJSONString(modelMap);
        }
        //修改密码
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("user_id", userId);
        uw.set("password", password);
        boolean res = iUserService.update(uw);
        if (res) {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "修改密码成功");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.CHANGE_PASSWORD_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "修改密码失败");
        }
        return JSON.toJSONString(modelMap);
    }

}
