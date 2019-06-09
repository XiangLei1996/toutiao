package com.nowcoder.async;

import java.util.List;

/**
 * Author: XiangL
 * Date: 2019/6/9 14:24
 * Version 1.0
 *
 * 事件处理器
 * 还需要创建一个事件处理器的实现类
 * 根据传入的事件模型来选择不同的时间处理器处理时间
 *
 * 将所有的事件处理器实现类存放在Handler包中
 */
public interface EventHandler {
    void doHandle(EventModel model);

    //获取获得该处理器处理的所有事件类型
    List<EventType> getSupportEventTypes();
}