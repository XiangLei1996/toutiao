package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by rainday on 16/6/30.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    /**
     * @param username 用户名
     * @param password 密码
     * @return 返回的 map 存储前端视图所要用的提示信息
     */
    public Map<String, Object> register(String username, String password){
        //用于存储前台需要显示的信息并返回
        Map<String, Object> map = new HashMap<>();

        //这里使用了依赖包commons中的StringUtils工具类，这是一个经常用来判断字符串符合某种规则的类
        if(StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;//出错后直接返回
        }

        //合格的后端应当也进行一些检测
        if(StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msgname", "该用户名已被注册");
            return map;
        }

        //真正的注册用户
        user = new User();
        user.setName(username);
        //密码不能直接明文保存到数据中，需要加密，然后使用随机字符串拼接在之后
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//随机生成UUID然后转换成字符串，取前5位使用
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        //登录，一般注册完之后，会直接进行登录  ---  调用私有的addLoginTicket方法 -- 返回该对象的ticket值
        String ticket = addLoginTicket(user.getId());

        //将该对象的ticket值存储，返回到视图，由浏览器保存
        map.put("ticket", ticket);

        return map;
    }


    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msgname", "用户名不存在");
            return map;
        }

        //验证密码
        if(ToutiaoUtil.MD5(password +user.getSalt()) != user.getPassword()){
            map.put("msgpwd", "密码错误");
        }

        //用户登录成功，需要下发一个 ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;

    }

    /**
     * 给登录的 user 添加 ticket
     * 并返回ticket 的 字符串值
     */
    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);

        Date date = new Date();//用于获取当前时间，方便设定有效期
        date.setTime(date.getTime() + 1000*24*60);
        ticket.setExpired(date);
        ticket.setStatus(0);//0为有效
        //使用随机生成的UUID 作为 ticket的值，此外将UUID中的-使用空字符串替代
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));

        //将ticket和其相关信息存入数据库
        loginTicketDAO.addTicket(ticket);

        //返回的是生成的ticket字符串值
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public int addUser(User user){
        return userDAO.addUser(user);
    }
}
