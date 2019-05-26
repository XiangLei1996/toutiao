package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: XiangL
 * Date: 2019/5/26 9:15
 * Version 1.0
 *
 * 注意：记住Mapper注解
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //添加
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{userId}, #{content}, #{createdDate}, #{entityId}, #{entityType}, #{status})"})
    int addComment(Comment comment);

    //查询 --- 通过评论所属的对像选出该对象的所有评论，并根据时间排序
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} Order By id DESC"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    //查询 --- 获得评论的总数
    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

}
