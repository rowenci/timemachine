package com.rowenci.timemachine.interceptor;

import com.rowenci.timemachine.util.TokenUtil.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
//@Component
public class UserHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request != null) {
            String token = request.getParameter("token");
            if (token == null) {
                return false;
            } else {
                if (TokenUtil.judegToken(token)) {
                    //token对比失败
                    return false;
                } else {
                    //token对比成功
                    return true;
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
