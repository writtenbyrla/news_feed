package com.example.news_feed.user.dto.request;

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
