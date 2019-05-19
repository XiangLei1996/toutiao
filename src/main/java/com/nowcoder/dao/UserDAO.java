package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by rainday on 16/6/30.
 */
@Mapper
public interface UserDAO {

    String TABLE_NAME = "user";

    String INSERT_FIELDS= "name, password, salt, head_url";

    String SELECT_FIELDS = "id, name, password, salt, head_url";

    @Insert({
            "Insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{name}, #{password}, #{salt}, #{headUrl})"
    })
    int addUser(User user);

    @Select({"Select ", SELECT_FIELDS, " FROM ", TABLE_NAME, " WHERE id = #{id}"})
    User selectById(int id);

    //更新密码,传入User对象，id是用户对象的唯一身份标识
    @Update({"Update ", TABLE_NAME, "Set password = #{password} Where id = #{id}"})
    void updatePassword(User user);

    @Delete({"Delete From ", TABLE_NAME, "Where id = #{id}"})
    void deleteById(int id);
}
