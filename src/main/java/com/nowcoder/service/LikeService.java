package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: XiangL
 * Date: 2019/6/9 13:59
 * Version 1.0
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 赞返回1，讨厌返回-1，无返回0
     * SET类型实现赞踩功能，资讯
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public int getLikeStatus(int userId, int entityId, int entityType){
        //找到该资讯所属的赞的SET键
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        //使用Redis时，最需要注意的就是Redis中全是字符串，需要进行相应转换
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            return 1;
        }
        //查看该资讯所属的踩的集合中是否有用户
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }


    /**
     * 点赞
     * @param userId 点赞的用户
     * @param entityType  entityType确定资讯所属类。目前仅有新闻类为1
     * @param entityId  被点赞的资讯在其所属类中的id
     * @return  返回该资讯获得的赞的总数
     */
    public long like(int userId, int entityType, int entityId) {
        //在喜欢的集合中添加
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        //同时从踩的集合中删除（如果存在的话）
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));

        //返回赞的总人数
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId) {
        // 在反对集合里增加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        // 从喜欢里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
