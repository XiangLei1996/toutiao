package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: XiangL
 * Date: 2019/6/9 14:24
 * Version 1.0
 *
 * 事件模型
 *
 * 消息队列是由事件驱动的，放入事件
 *
 */
public class EventModel {
    private EventType type;//事件种类,通过枚举类EventType的值区分
    private int actorId;//实现事件的行为者的id
    private int entityId;//事件对象的id
    private int entityType;//事件对象的所属类（如新闻类资讯还是娱乐类资讯等）
    private int entityOwnerId;//这条资讯的发布者id
    private Map<String, String> exts = new HashMap<>();

    public Map<String, String> getExts() {
        return exts;
    }
    public EventModel() {

    }
    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String name) {
        return exts.get(name);
    }

    public EventModel setExt(String name, String value) {
        exts.put(name, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
