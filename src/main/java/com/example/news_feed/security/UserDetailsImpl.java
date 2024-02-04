package com.example.news_feed.security;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private final Long userId;
    private final String email;
    private final String pwd;
    private final String username;
//    private final UserRoleEnum role;
    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    public UserDetailsImpl(Long userId, String email, String pwd, String username, final Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.pwd = pwd;
        this.username = username;
//        this.role = role;
        this.authorities = authorities;
    }

    public static UserDetailsImpl from(User user) {
        return UserDetailsImpl.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .pwd(user.getPwd())
                .authorities(Set.of())
                .build();
    }

    public Long getId(){ return userId; }

    public String getEmail(){ return email; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        UserRoleEnum role = this.getRole();
//        String authority = role.getAuthority();
//
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(simpleGrantedAuthority);
        return authorities;
    }
    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return username;
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
