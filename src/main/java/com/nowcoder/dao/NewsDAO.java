package com.nowcoder.dao;

import com.nowcoder.model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by rainday on 16/6/30.
 * 注解形式写UserDao  @Mapper注解类，@SELECT等注解抽象方法
 */
@Mapper
public interface NewsDAO {

    //将不变的常量统一提取并命名，方便维护修改
    String TABLE_NAME = "news";

    //字段有错误，数据库的字段是like_count而不是likeCount，likeCount是实体类中的属性名
    String INSERT_FIELDS = "title, link, image, like_count, comment_count, created_date, user_id";

    String SELECT_FIELDS = "id" + INSERT_FIELDS;

//    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELDS,
//            ") VALUES (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"
//    })
    @Insert({
            "insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{title},#{link},#{image},#{likeCount}, #{commentCount},#{createdDate},#{userId})"
    })
    int addNews(News news);

    //这里字符串拼接可以使用逗号分开，交由mybatis自己完成
    @Select({"SELECT ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    News selectById(int id);

    /**
     * Param注解能简化xml配置，用于给参数命名，之后则可直接通过名字获得参数
     * 这里为查询用户发布的新闻消息并 分页操作-----是通过xml配置接口实现的
     * userId为查询条件，offset和limit组成需要多少条  limit a,b 返回从 a开始的b行数据
    */
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
