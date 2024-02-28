package com.example.news_feed.user.domain;

import com.example.news_feed.common.timestamp.TimeStamp;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@DynamicInsert
public class User extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name="pwd", nullable = false)
    private String pwd;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="description")
    private String description;

    @Column(name="profile_img")
    private String profileImg;

    @Column(name = "status", columnDefinition = "varchar(1) default 'Y'")
    private String status;

    @Column(name="role", columnDefinition = "varchar(10) default 'USER'")
    @Enumerated(value=EnumType.STRING)
    private UserRoleEnum role;
    
    public User(String username, String pwd, String email) {
        this.username = username;
        this.pwd = pwd;
        this.email = email;
    }

    public User(String email, String pwd) {
        this.pwd = pwd;
        this.email = email;
    }

    public static User createUser(SignupReqDto signupReqDto) {
        return new User(
                signupReqDto.getUsername(),
                signupReqDto.getPwd(),
                signupReqDto.getEmail()
        );

    }

    public void patchProfile(UserUpdateDto updateDto) {

        if (!this.userId.equals(updateDto.getUserId()))
            throw new UserException(UserErrorCode.USER_NOT_EXIST);

        if (updateDto.getUsername() != null)
            this.username = updateDto.getUsername();

        if (updateDto.getDescription() != null)
            this.description = updateDto.getDescription();

        if (updateDto.getProfileImg() != null)
            this.profileImg= updateDto.getProfileImg();
    }

    public void patchPwd(PwdUpdateDto pwdUpdateDto) {

        if (!this.userId.equals(pwdUpdateDto.getUserId()))
            throw new UserException(UserErrorCode.USER_NOT_EXIST);

        this.pwd = pwdUpdateDto.getNewPwd();
    }


}
