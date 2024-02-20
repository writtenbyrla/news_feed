package com.example.news_feed.user.dto.response;

import com.example.news_feed.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserDetailDto {

    private Long userId;
    private String username;
    private String email;
    private String description;
    private String profileImg;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;

    public static UserDetailDto createUserDetailDto(User user) {
        return new UserDetailDto(
          user.getUserId(),
          user.getUsername(),
          user.getEmail(),
          user.getDescription(),
          user.getProfileImg(),
          user.getCreatedAt(),
          user.getCreatedAt(),
          user.getStatus()
        );
    }
}
