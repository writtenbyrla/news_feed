package com.example.news_feed.user.domain;

import com.example.news_feed.common.TimeStamp;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
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

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="description")
    private String description;

    @Column(name="profile_img")
    private String profileImg;

    @Column(name = "status", columnDefinition = "varchar(1) default 'Y'")
    private String status;

    @Column(name="role", columnDefinition = "varchar(10) default 'USER'")
    @Enumerated(value=EnumType.STRING)
    private UserRoleEnum role;
    
    public User(Long userId, String username, String pwd, String email, String phone, String status, UserRoleEnum role) {
        this.userId = userId;
        this.username = username;
        this.pwd = pwd;
        this.email = email;
        this.phone = phone;
        this.status=status;
        this.role = role;
    }

    public User(String email, String pwd) {
        this.pwd = pwd;
        this.email = email;
    }

    public static User createUser(SignupReqDto signupReqDto) {
        return new User(
                signupReqDto.getUserId(),
                signupReqDto.getUsername(),
                signupReqDto.getPwd(),
                signupReqDto.getEmail(),
                signupReqDto.getPhone(),
                signupReqDto.getStatus(),
                signupReqDto.getRole()
        );

    }

    public void patchProfile(UserUpdateDto updateDto) {

        if (this.userId != updateDto.getUserId())
            throw new IllegalArgumentException("프로필 수정 실패! userId가 잘못 입력되었습니다.");

        if (updateDto.getUsername() != null)
            this.username = updateDto.getUsername();

        if (updateDto.getPhone() != null)
            this.phone = updateDto.getPhone();

        if (updateDto.getDescription() != null)
            this.description = updateDto.getDescription();
    }

    public void patchPwd(PwdUpdateDto pwdUpdateDto) {

        if (this.userId != pwdUpdateDto.getUserId())
            throw new IllegalArgumentException("암호 수정 실패! userId가 잘못 입력되었습니다.");

        this.pwd = pwdUpdateDto.getNewPwd();
    }


}
