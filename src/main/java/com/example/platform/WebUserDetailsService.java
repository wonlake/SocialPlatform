package com.example.platform;

import com.example.MD5Util;
import com.example.models.Account;
import com.example.models.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by meijun on 2016/11/29.
 */
@Service
public class WebUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountMapper.getAccountByUsername(username);

        if(account == null) {
            throw new UsernameNotFoundException("username not found!");
        }
        else {
            return new WebUserDetails(account);
        }
    }
}
