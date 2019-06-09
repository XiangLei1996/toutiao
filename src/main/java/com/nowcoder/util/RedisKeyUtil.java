package com.nowcoder.util;

/**
 * Author: XiangL
 * Date: 2019/6/9 14:02
 * Version 1.0
 *
 * 异步处理的工具类
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    /**
     * 资讯所属赞集合SET的键名：形如  "LIKE <entityType> <entityId>"
     * @param entityId
     * @param entityType
     * @return
     */
    public static String getLikeKey(int entityId, int entityType) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    /**
     * 资讯所属讨厌集合SET的键名
     * @param entityId
     * @param entityType
     * @return
     */
    public static String getDisLikeKey(int entityId, int entityType) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}

