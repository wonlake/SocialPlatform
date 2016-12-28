package com.example.models;

import org.apache.ibatis.annotations.*;

/**
 * Created by meijun on 2016/11/14.
 */
@Mapper
public interface UserMapper {
    @Select("select * from tbl_user where username = #{name}")
    User getUserByUsername(@Param("name") String name);

    @Select("select * from tbl_user where userId = #{id}")
    User getUserById(@Param("id") long id);

    @Insert("insert into tbl_user(nickname, username) values(#{nickname}, #{username})")
    void saveUser(User user);
}
