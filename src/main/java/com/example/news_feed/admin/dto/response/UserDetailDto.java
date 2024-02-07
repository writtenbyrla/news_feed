package com.example.news_feed.admin.dto.response;

import com.example.news_feed.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserDetailDto {

    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String description;
    private String profileImg;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    public static UserDetailDto createUserDetailDto(User user) {
        return new UserDetailDto(
          user.getUserId(),
          user.getUsername(),
          user.getEmail(),
          user.getPhone(),
          user.getDescription(),
          user.getProfileImg(),
          user.getCreatedAt(),
          user.getCreatedAt(),
          user.getModifiedAt()
        );
    }
}
