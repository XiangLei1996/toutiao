package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Author: XiangL
 * Date: 2019/5/22 12:40
 * Version 1.0
 * Ticket操作的DAO接口
 *
 * 注意 ---------  注解形式实现DAO层时，必须在接口上使用 @Mapper 进行注解，以标识这是一个mapper-dao接口类
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = "user_id, expired, status, ticket";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"Insert into " + TABLE_NAME + "(" + INSERT_FIELDS +
            ") Values (#{userId}, #{expired}, #{status}, #{ticket})"})
    int addTicket(LoginTicket ticket);

    //查询
    @Select({"Select " + SELECT_FIELDS + " from " + TABLE_NAME + " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    //更新ticket的状态
    @Update({"Update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
