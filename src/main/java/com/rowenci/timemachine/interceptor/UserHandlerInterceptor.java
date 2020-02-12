package com.rowenci.timemachine.interceptor;

import com.rowenci.timemachine.util.TokenUtil.TokenUtil;
import com.rowenci.timemachine.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
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
 * @since 2020/2/3 18:32
 */
//@Slf4j
//@Component
public class UserHandlerInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request != null) {
            String token = request.getParameter("token");
            if (token == null || token == "") {
                return false;
            } else {
                try {
                    if (redisUtil.get(token) == null){
                        return false;
                    }
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
