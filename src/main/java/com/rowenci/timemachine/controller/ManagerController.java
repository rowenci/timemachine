package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rowenci.timemachine.entity.Manager;
import com.rowenci.timemachine.service.IManagerService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import com.rowenci.timemachine.util.CreateUUID;
import com.rowenci.timemachine.util.TokenUtil.TokenUtil;
import com.rowenci.timemachine.util.redis.RedisUtil;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-09
 */
@RestController
@RequestMapping("/timemachine/manager")
public class ManagerController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IManagerService iManagerService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 添加管理员
     * @param manager
     * @return
     */
    @PostMapping("/")
    public String managerLogup(Manager manager){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        //判断账号是否存在
        qw.eq("account", manager.getAccount());
        if (iManagerService.getOne(qw) != null){
            modelMap.addAttribute("code", serviceCodeInfo.LOGUP_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "该账号已经存在");
            return JSON.toJSONString(modelMap);
        }
        //获取uuid
        CreateUUID createUUID = new CreateUUID();
        String managerId = createUUID.create();
        manager.setManagerId(managerId);

        //添加管理员
        boolean res = iManagerService.save(manager);
        if (res) {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "添加管理员成功");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.LOGUP_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "添加管理员失败");
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * manager 登陆
     * @param account
     * @param password
     * @return
     */
    @GetMapping("/")
    public String managerLogin(String account, String password){
        ModelMap modelMap = new ModelMap();

        //判断管理员是否存在
        QueryWrapper qw = new QueryWrapper();
        qw.eq("account", account);
        Manager manager = iManagerService.getOne(qw);

        if (manager == null){
            //管理员不存在
            modelMap.addAttribute("code", serviceCodeInfo.LOGIN_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "");
        }else {
            //管理员存在
            //判断密码是否正确
            if (manager.getPassword().equals(password)){
                //正确 登陆成功
                String token = TokenUtil.createToken();

                //放入缓存<token, 用户id>并设置失效时间3600秒
                redisUtil.set(token, manager.getManagerId(), 3600);
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("token", token);
                infoMap.put("managerId", manager.getManagerId());

                modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
                modelMap.addAttribute("data", infoMap);
                modelMap.addAttribute("result", "success");
                modelMap.addAttribute("description", "登陆成功");

            }else {
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
     * manager 登出
     * @param token
     * @return
     */
    @GetMapping("/managerLogout")
    public String managerLogout(String token){
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
            modelMap.addAttribute("description", "管理员退出成功");
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * 根据managerId获取manager信息
     * @param managerId
     * @return
     */
    @GetMapping("/{manager_id}")
    public String getManagerByManagerId(@PathVariable("manager_id") String managerId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("manager_id", managerId);
        Manager manager = iManagerService.getOne(qw);
        if (manager == null){
            modelMap.addAttribute("code", serviceCodeInfo.NO_USER);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "没有该管理员");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", manager);
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "查找成功");
        }
        return JSON.toJSONString(modelMap);
    }

}
