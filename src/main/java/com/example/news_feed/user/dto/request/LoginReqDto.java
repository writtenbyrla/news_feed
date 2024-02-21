package com.example.news_feed.user.dto.request;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class LoginReqDto {

    private String email;
    private String pwd;

}
