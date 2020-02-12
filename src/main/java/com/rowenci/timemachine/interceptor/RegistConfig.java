package com.rowenci.timemachine.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/3 18:35
 */
//@Configuration
public class RegistConfig implements WebMvcConfigurer {

    @Bean
    public UserHandlerInterceptor getInterceptor(){
        return new UserHandlerInterceptor();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter(){
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(getInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/timemachine/vip-user/**")
                        .excludePathPatterns("/timemachine/manager/**")
                        .excludePathPatterns("/timemachine/message/uploadImage")
                        .excludePathPatterns("/timemachine/share/**")
                        .excludePathPatterns("/timemachine/user/**");
            }
        };
    }
}
