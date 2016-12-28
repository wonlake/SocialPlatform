package com.example.models;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by meijun on 2016/11/16.
 */
@Mapper
public interface AccountMapper {
    @Select("select * from tbl_account where username = #{name}")
    Account getAccountByUsername(@Param("name") String name);

    @Select("select * from tbl_account where id = #{id}")
    Account getAccountById(@Param("id") long id);

    @Insert("insert into tbl_account(username, password, salt) values(#{username}, #{password}, #{salt})")
    void saveAccount(Account account);
}
