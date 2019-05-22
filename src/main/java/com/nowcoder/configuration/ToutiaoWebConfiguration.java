package com.nowcoder.configuration;

import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Author: XiangL
 * Date: 2019/5/22 18:50
 * Version 1.0
 * 通过@Component提示Spring加载
 *
 * 配置类，通过Spring配置类的方式，添加写好的Interceptor类
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        super.addInterceptors(registry);
    }
}
