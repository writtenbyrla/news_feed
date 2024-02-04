package com.example.news_feed.user.dto.request;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.user.domain.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class SignupReqDto {

    private Long userId;

    @Pattern(regexp = "[a-z0-9]{4,10}", message = "이름은 알파벳 소문자, 숫자를 포함하여 4~10자여야 합니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$^!%*#?&])[A-Za-z\\d$@$^!%*#?&]{8,15}$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.")
    private String pwd;

    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    private String phone;

    private Date createdAt;
}
