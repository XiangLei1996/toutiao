package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: XiangL
 * Date: 2019/6/2 10:45
 * Version 1.0
 * 接口绑定的两个特点
 * 1.@Mapper修饰 2.接口interface
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 增加消息
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
    ") values(#{fromId}, #{toId}, #{content}, #{hasRead}, #{conversationId}, #{createdDate})"})
    int addMessage(Message message);

    /**
     * 查询与userId进行通信的所有消息,且分页
     * 子查询查询所有消息，且排序，最新的显示最前
     * tt的作用？
     */
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) " +
            "tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询所有未读消息数量
     */
    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /**
     * 查询本次通信中未读消息数量
     */
    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);


    /**
     * 查询此次通信的一半消息
     * conversation_id = from_id  + to_id组成，小的在前，大的在后，即可得到本次通信的所有消息
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);
}
