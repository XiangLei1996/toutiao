package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Author: XiangL
 * Date: 2019/5/21 14:33
 * Version 1.0
 * 由于忘记写 @Controller 导致DispatcherServlet找不到对应的Handler
 */
@Controller
public class LoginController {
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    /**
     * *****需要会操作 cookie 存储 ticket 的部分 ****
     * 注解  @ResponseBody 用于返回Json串--首先在pom.xml中添加alibaba的json对象依赖包
     * 注意是 @ResponseBody
     * @param model 存储返回视图中要用的数据
     * @param username 用户名
     * @param password 密码
     * @param rememberme 用于标识是否要记住密码
     * @return 通过使用 @ResponseBody，这里返回的是JSON字符串
     * path应当和 前端一致，前期报错因为和前端的 action提交的网页不一致
     * 因此，在开发过程中，应当商定每个网页的请求URL以及参数形式
     */
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(Model model, @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "rember", defaultValue = "0") int rememberme,
                           HttpServletResponse response){
        //通过@RequestParam注解从请求中获取参数, 第二种形式@RequestParam则可以设置默认值
//        System.out.println(username + password);
        try{
            Map<String, Object> map = userService.register(username, password);
            if(map.containsKey("ticket")){//根据是否返回ticket来判断是否成功即可
                /*创建Cookie存储ticket，并将cookie存放到 response对象中
                 * 使得返回时，浏览器获得cookie并保存*/
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");//设置路径全战有效
                if(rememberme > 0){//如果用户有需求，则将有效时间设置的更长
                    cookie.setMaxAge(3600*24*5);//设置5天时间，不写默认浏览器关闭即失效
                }
                response.addCookie(cookie);

                return ToutiaoUtil.getJsonString(0,"登录成功");
            }else{
                return ToutiaoUtil.getJsonString(1, map);
            }

        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJsonString(1, "注册异常");
        }
    }


    /**
     * 登录控制器   ---  需要会操作 cookie 存储 ticket 的部分
     * @param model
     * @param username
     * @param password
     * @param rememberme  -- 是否记住我
     * @param response --- HttpServletResponse对象的实例，用于存储cookie对象，并在响应时返回给浏览器
     * @return
     */
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response){

        try{
            Map<String, Object> map = userService.login(username, password);
            if(map.containsKey("ticket")){//根据是否返回ticket来判断是否成功即可
                /*创建Cookie存储ticket，并将cookie存放到 response对象中
                * 使得返回时，浏览器获得cookie并保存*/
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");//设置路径全战有效
                if(rememberme > 0){//如果用户有需求，则将有效时间设置的更长
                    cookie.setMaxAge(3600*24*5);//设置5天时间，不写默认浏览器关闭即失效
                }
                return ToutiaoUtil.getJsonString(0,"登录成功");
            }else{
                return ToutiaoUtil.getJsonString(1,map);
            }
        }catch (Exception e){
            logger.error("登录异常" + e.getMessage());
            return ToutiaoUtil.getJsonString(1, "登录异常");
        }
    }


    /**
     * 通过 @CookieValue 获得 Cookie中对应的键 的值对象
     * @param ticket
     * @return
     */
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);//改变ticket的status

        //登出后，重定向到首页
        return "redirect:/";
    }
}
