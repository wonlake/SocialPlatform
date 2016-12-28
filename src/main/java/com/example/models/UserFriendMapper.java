package com.example.models;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by meijun on 2016/11/16.
 */
@Mapper
public interface UserFriendMapper {
    @Select("select * from tbl_userfriend where userId = #{userId}")
    List<UserFriend> getFriends(@Param("userId") long userId);

    @Insert("insert into tbl_userfriend(userId, friendId) values(#{userId}, #{friendId})")
    void addFriend(UserFriend friend);

    @Select("select * from tbl_userfriend where userId = #{userId} and friendId = #{friendId}")
    UserFriend getFriend(@Param("userId") long userId, @Param("friendId") long friendId);
}
