package com.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.ErrorCode;
import com.example.MD5Util;
import com.example.ResponseMessage;
import com.example.models.*;
import com.example.platform.SocialUserDetail;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by meijun on 2016/11/14.
 */
@RestController
@CrossOrigin
public class UserInfoController {
    private AccountMapper accountMapper;
    private UserMapper userMapper;
    private UserFriendMapper friendMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ObjectMapper jsonMapper;

    MessageDigest md5 = null;
    Random random = new Random();

    @Resource(name="stringRedisTemplate")
    ValueOperations<String, String> userCache;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private SocialUserDetail getUserDetail(String username) {
        String strSocialUserDetail = userCache.get(username);
        if( strSocialUserDetail != null && strSocialUserDetail.length() > 0)
        {
            return parseJson(strSocialUserDetail, SocialUserDetail.class);
        }
        else {
            User user = userMapper.getUserByUsername(username);
            if(user != null) {
                SocialUserDetail socialUserDetail = new SocialUserDetail();
                socialUserDetail.setUsername(user.username);
                socialUserDetail.setNickname(user.nickname);
                Account account = accountMapper.getAccountByUsername(username);
                socialUserDetail.setUserId(account.id);
                userCache.set(username, toJson(socialUserDetail));
                return socialUserDetail;
            }
        }
        return null;
    }

    @RequestMapping("/user/get")
    public ResponseMessage getUser(@RequestParam("username") String username) {
        ResponseMessage response = new ResponseMessage();
        SocialUserDetail user = getUserDetail(username);
        if( user != null )
        {
            response.setMsg(toJson(user));
            return response;
        }
        else
        {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("No user found!");
            return response;
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseMessage userRegister(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        ResponseMessage response = new ResponseMessage();
        SocialUserDetail userDetail = getUserDetail(username);

        if( userDetail != null )
        {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("User has registered!");
            return response;
        }
        else
        {
            if(password.length() < 6) {
                response.setErrorCode(ErrorCode.EC_Error);
                response.setMsg("password is too short, the length should >= 6!");
                return response;
            }
            Account account = new Account();
            account.username = username;
            account.salt = MD5Util.MD5(Integer.toString(random.nextInt()));
            account.password = MD5Util.MD5(password + account.salt);
            account.privileges = "ROLE_USER";
            accountMapper.saveAccount(account);

            User user = new User();
            user.username = username;
            user.nickname = username;
            userMapper.saveUser(user);

            return response;
        }
    }

    @Autowired
    private void initController(AccountMapper accountMapper,
                                UserMapper userMapper, UserFriendMapper friendMapper) {
        this.accountMapper = accountMapper;
        this.userMapper = userMapper;
        this.friendMapper = friendMapper;
        if( md5 == null)
        {
            try {
                md5 = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException e) {
                logger.error(e.toString());
            }
        }
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseMessage userLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password) {
        ResponseMessage response = new ResponseMessage();
        SocialUserDetail userDetail = getUserDetail(username);
        if(userDetail == null) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("User dosen't exist!");
            return response;
        }

        Account account = accountMapper.getAccountByUsername(username);
        if(account.password.equals(MD5Util.MD5(password + account.salt))) {
            response.setMsg(toJson(userDetail));
        }
        else {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("Username or password is invalid!");
        }
        return response;
    }

    @RequestMapping("/user/friends")
    public ResponseMessage userFriends() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        ResponseMessage response = new ResponseMessage();
        SocialUserDetail userDetail = getUserDetail(username);
        if(userDetail == null ) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("User dosen't exist!");
            return response;
        }

        List<UserFriend> friends = friendMapper.getFriends(userDetail.getUserId());
        ArrayList<SocialUserDetail> userFriends = new ArrayList<>();
        for(UserFriend friend : friends) {
            Account account = accountMapper.getAccountById(friend.friendId);
            if( account == null) {
                continue;
            }

            SocialUserDetail userFriend = getUserDetail(account.username);
            if(userFriend != null) {
                userFriends.add(userFriend);
            }
            response.setMsg(toJson(userFriends));
        }
        return response;
    }

    protected <T> T parseJson(String info, Class<T> Cls) {
        T t = null;
        if(info != null && info.length() > 0) {
            try {
                t = jsonMapper.readValue(info, Cls);
            }
            catch (IOException e) {
                logger.error(e.toString());
            }
        }
        return t;
    }

    protected <T> String toJson(T obj) {
        String strRet = null;
        try {
            strRet = jsonMapper.writeValueAsString(obj);
        }
        catch (JsonProcessingException e) {
            logger.error(e.toString());
        }
        return strRet;
    }

    @RequestMapping("/user/addFriend")
    public ResponseMessage addFriend(@RequestParam("username") String username,
                                     @RequestParam("friendId") long friendId) {
        ResponseMessage response = new ResponseMessage();
        if( friendId < 1) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("Invalid Friend ID!");
            return response;
        }

        SocialUserDetail userDetail = getUserDetail(username);
        if( userDetail == null ) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("username dosen't exist!");
            return response;
        }

        if( userDetail.getUserId() == friendId ) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("user can't add self as friend!");
            return response;
        }

        UserFriend userFriend = friendMapper.getFriend(userDetail.getUserId(), friendId);
        if( userFriend != null ) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("You're already friends");
        }
        else {
            Account account = accountMapper.getAccountById(friendId);
            if( account == null ) {
                response.setErrorCode(ErrorCode.EC_Error);
                response.setMsg("friendId dose't exit!");
                return response;
            }
            userFriend = new UserFriend();
            userFriend.userId = userDetail.getUserId();
            userFriend.friendId = friendId;
            friendMapper.addFriend(userFriend);
        }

        return response;
    }
}
