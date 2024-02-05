package com.example.news_feed.security;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ToString
public class UserDetailsImpl implements UserDetails {
//
//    private final Long userId;
//    private final String email;
//    private final String pwd;
//    private final String username;
//    private final UserRoleEnum role;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    @Builder
//    public UserDetailsImpl(Long userId, String email, String pwd, String username, UserRoleEnum role, final Collection<? extends GrantedAuthority> authorities) {
//        this.userId = userId;
//        this.email = email;
//        this.pwd = pwd;
//        this.username = username;
//        this.role = role;
//        this.authorities = authorities;
//    }
//
////    public static UserDetailsImpl from(User user) {
////
////        //log.info("UserDetailsImpl : {} " , user.getRole().getAuthority()); // user.getRole() : USER , user.getRole().getAuthority() : ROLE_USER
////        Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getRole().getAuthority()));
////
////        //log.info("UserDetailsImpl : {} " , authorities); ROLE_USER
////        return UserDetailsImpl.builder()
////                .userId(user.getUserId())
////                .email(user.getEmail())
////                .pwd(user.getPwd())
////                .authorities(authorities)
////                .build();
////    }
//
//
//    public Long getId(){ return userId; }
//
//    public String getEmail(){ return email; }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        UserRoleEnum role = this.role;
//        String authority = role.getAuthority();
//
//        log.info("UserDetailsImpl role: {} " , role);
//        log.info("UserDetailsImpl authority: {} " , authority);
//
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(simpleGrantedAuthority);
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return pwd;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }


    public User getUser(){
        return user;
    }


    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }


//    public String getEmail(){ return user.getEmail(); }

    public Long getId(){ return user.getUserId(); }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
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
