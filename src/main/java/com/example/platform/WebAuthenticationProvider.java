package com.example.platform;

import com.example.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by meijun on 2016/11/29.
 */
@Service
public class WebAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private WebUserDetailsService webUserDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = webUserDetailService.loadUserByUsername(username);

        if(user == null) {
            throw new BadCredentialsException("username not found!");
        }

        WebUserDetails webUser = (WebUserDetails)user;
        if( !user.getPassword().equals(MD5Util.MD5(password + webUser.getSalt()))) {
            throw new BadCredentialsException("username or password is invalid!");
        }
        return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
