package com.example.news_feed.user.dto.request;

import com.example.news_feed.user.domain.User;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PwdUpdateDto {

    private Long userId;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$^!%*#?&])[A-Za-z\\d$@$^!%*#?&]{8,15}$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.")
    private String oldPwd;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$^!%*#?&])[A-Za-z\\d$@$^!%*#?&]{8,15}$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.")
    private String newPwd;

    private LocalDateTime modifiedAt;

    // 새로운 비밀번호만을 받는 생성자 추가
    public PwdUpdateDto(Long userId, String newPwd) {
        this.userId = userId;
        this.newPwd = newPwd;
    }


    public static PwdUpdateDto createResponseDto(User user) {
        // 새로운 비밀번호만 반환
        return new PwdUpdateDto(
                user.getUserId(),
                user.getPwd()
        );
    }


}
