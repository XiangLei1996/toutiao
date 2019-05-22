package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Author: XiangL
 * Date: 2019/5/22 18:20
 * Version 1.0
 * 实现 HandlerInterceptor 接口 ， 成为拦截器类
 * ，该类在用户访问前进行token的判断
 * 使用 @Component 注解将该类的bean对象交由Spring管理
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    // ctrl + o 快速重写

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //控制器处理之前
        String ticket = null;
        for(Cookie cookie : httpServletRequest.getCookies()){//在httpServletRequest对象的所有cookie中遍历
            if(cookie.getName().equals("ticket")){//有ticket这个键
                ticket = cookie.getValue();
                break;
            }
        }

        if(ticket != null){//请求对象的所有cookie中有ticket键值对，则进行验证
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getStatus() != 0 || loginTicket.getExpired().before(new Date())){
                //该ticket不存在 或者 失效，则直接返回
                return true;
            }

            //有效
            User user = userDAO.selectById((loginTicket.getUserId()));
            //使用HostHolder存储对象，实际上是使用HostHolder里的ThreadLocal存储，达到整个本次请求的线程共享该对象，则无需再重复登录
            hostHolder.setUsers(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //控制器处理完后，对页面进行加工修饰
        if(modelAndView != null && hostHolder.getUsers() != null){
            modelAndView.addObject("user", hostHolder.getUsers());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //每次请求的收尾工作。这次请求需要将ThreadLocal中存储的值清空
        hostHolder.clear();
    }
}
