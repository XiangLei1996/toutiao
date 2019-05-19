package com.nowcoder.controller;

import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.ToutiaoService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nowcoder on 2016/6/26.
 * 主页入口
 */
@Controller
public class HomeController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ToutiaoService toutiaoService;

    @Autowired
    private UserService userService;

    /**
     * 拦截/index请求或者/请求，接受POST和GET方法
     * 通过@RequestParam注解获得请求对象request中的参数,且命名为userId
     * 当没有参数时，使用默认值 0  ---- 注意，这里 0 要用双引号包括
     * 其中 @RequestParam 用于将指定的请求参数赋予个方法中的形参
     */
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(@RequestParam(value = "userId", defaultValue = "0") int userId,
                        Model model) {
        //获取10条新闻并作为传入model
        model.addAttribute("vos", getNews(0, 0, 10));

        //返回主页
        return "home";
    }

    /**
     * 通过路径变量@PathVariable的方式获得请求路径中的userId，并赋值给其后所跟的方法形参
     *
     */
    @RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId, Model model) {

        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }

    /**
     * 私有化查询新闻信息，即在 真正的返回视图 和 Service层中 再加了一个处理
     * 这个方法专注于根据三个参数获得指定的新闻对象
     * 方便之后其他请求处理方法 复用 该部分
     */
    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = toutiaoService.getLatestNews(userId, offset, limit);

        //ViewObject的list用来存储news，用ViewObject是方便将数据传递给velocity
        List<ViewObject> vos = new ArrayList<>();

        //增强for循环，将newsList中的news对象依次存储vos中
        for (News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(userId));
            vos.add(vo);
        }

        return vos;
    }
}
