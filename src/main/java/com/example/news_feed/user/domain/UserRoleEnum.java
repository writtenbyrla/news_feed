package com.example.news_feed.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    USER("USER"),  // 사용자 권한
    ADMIN("ADMIN");  // 관리자 권한

    private final String authority;
}