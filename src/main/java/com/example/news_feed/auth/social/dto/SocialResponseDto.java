package com.example.news_feed.auth.social.dto;

import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialResponseDto {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String username;
    private UserRoleEnum role;

    public SocialResponseDto(String accessToken, String refreshToken) {
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }
}
