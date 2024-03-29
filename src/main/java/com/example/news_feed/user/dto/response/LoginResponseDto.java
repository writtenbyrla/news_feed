package com.example.news_feed.user.dto.response;

import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String username;
    private UserRoleEnum role;
}