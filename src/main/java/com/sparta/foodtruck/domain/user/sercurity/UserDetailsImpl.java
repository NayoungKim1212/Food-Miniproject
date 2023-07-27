package com.sparta.foodtruck.domain.user.sercurity;


import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import com.sparta.foodtruck.domain.user.entity.User;
import com.sparta.foodtruck.domain.user.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final AccountInfo accountInfo;

    public UserDetailsImpl(User user, AccountInfo accountInfo) {
        this.user = user;
        this.accountInfo = accountInfo;
    }


    public User getUser() {
        return user;
    }

    public AccountInfo getAccountInfo() { return this.accountInfo; }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return accountInfo.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = accountInfo.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

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