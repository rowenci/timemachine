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
@Configuration
public class RegistConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter(){
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new UserHandlerInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/timemachine/user");
            }
        };
    }
}
