package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: XiangL
 * Date: 2019/6/9 14:23
 * Version 1.0
 *
 * 使用@Service注解
 * Redis实现消息队列中的生产者
 * 使用LIST实现
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            //事件模型信息通过JSON序列化
            String json = JSONObject.toJSONString(eventModel);

            //获取事件的消息队列
            String key = RedisKeyUtil.getEventQueueKey();
            //事件加入消息队列
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
