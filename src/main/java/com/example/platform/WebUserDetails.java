package com.example.platform;

import com.example.models.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by meijun on 2016/11/29.
 */
public class WebUserDetails implements UserDetails {
    private Account account;

    public WebUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(account.privileges == null) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }

        return AuthorityUtils.commaSeparatedStringToAuthorityList(account.privileges);
    }

    @Override
    public String getPassword() {
        return account.password;
    }

    @Override
    public String getUsername() {
        return account.username;
    }

    public String getSalt() { return account.salt; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
