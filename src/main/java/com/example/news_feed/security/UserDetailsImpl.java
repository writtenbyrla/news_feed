package com.example.news_feed.security;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ToString
public class UserDetailsImpl implements UserDetails {

    private final Long userId;
    private final String email;
    private final String pwd;
    private final String username;
    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    public UserDetailsImpl(Long userId, String email, String pwd, String username, UserRoleEnum role, final Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.pwd = pwd;
        this.username = username;
        this.authorities = authorities;
    }

    public static UserDetailsImpl from(User user) {

        Set<SimpleGrantedAuthority> authorities = Optional.ofNullable(user.getRole())
                .map(role -> Set.of(new SimpleGrantedAuthority("ROLE_" + role.name())))
                .orElse(Collections.emptySet());

        return UserDetailsImpl.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .pwd(user.getPwd())
                .username(user.getUsername())
                .authorities(authorities) // 권한 설정 해줘야함.
                .build();
    }


    public Long getId(){ return userId; }

    public String getEmail(){ return email; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return email;
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
