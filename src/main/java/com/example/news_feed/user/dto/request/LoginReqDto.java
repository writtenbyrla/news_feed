package com.example.news_feed.user.dto.request;

import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.domain.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class LoginReqDto {

    private String email;
    private String pwd;

}
