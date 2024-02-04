package com.example.news_feed.user.domain;

// 사용자의 권한의 종류를 Enum을 사용해서 관리
// JWT를 생성할 때 사용자의 정보로 해당 사용자의 권한을 넣어줄 때 사용
public enum UserRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
