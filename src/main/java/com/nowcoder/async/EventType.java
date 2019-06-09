package com.nowcoder.async;

/**
 * Author: XiangL
 * Date: 2019/6/9 14:25
 * Version 1.0
 *
 * 枚举类。用来设置存放不同的事件对应的类型id
 *
 * 赞踩为0，评论事件为1，登录事件为2，邮件事件为3
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
    EventType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
