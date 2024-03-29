package com.example.news_feed.user.dto.request;

import com.example.news_feed.user.domain.User;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserUpdateDto {

    private Long userId;

    @Pattern(regexp = "[a-z0-9가-힣]{4,10}", message = "이름은 한글, 알파벳 소문자, 숫자를 포함하여 4~10자여야 합니다.")
    private String username;
    private String description;
    private String profileImg;

    public static UserUpdateDto createUserDto(User user) {
        return new UserUpdateDto(
                user.getUserId(),
                user.getUsername(),
                user.getDescription(),
                user.getProfileImg()
        );

    }


}
