package com.example.news_feed.auth.dto;

import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RefreshTokenDto {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String name;
    private UserRoleEnum role;
}
