package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Author: XiangL
 * Date: 2019/5/22 18:36
 * Version 1.0
 *
 * 封装存储线程共享变量的类
 * 线程共享的对象的引用存储在 ThreadLocal对象中
 * 达到整个请求中其他页面都能访问到该对象的目的
 * 用于实现记住密码等操作的 记住功能
 *
 * HostHolder用来封装 该 ThreadLocal对象 即存储 本次访问的用户对象
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUsers() {
        return users.get();
    }

    public void setUsers(User user) {
        users.set(user);
    }

    /**
     * 每次ThreadLocal使用完之后都应当清理
     * ThreadLocal的三个方法 get,set,remove
     */
    public void clear(){
        users.remove();
    }
}
