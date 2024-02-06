package com.example.news_feed.user.domain;

import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class User {

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

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name="role")
    @Enumerated(value=EnumType.STRING)
    private UserRoleEnum role;

    public User(Long userId, String username, String pwd, String email, String phone, Date createdAt, UserRoleEnum role) {
        this.userId = userId;
        this.username = username;
        this.pwd = pwd;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.role = role;
    }

    public User(String email, String pwd) {
        this.pwd = pwd;
        this.email = email;
    }

    public static User createUser(SignupReqDto signupReqDto) {
        // 예외 처리
        if (signupReqDto.getUserId() != null)
            throw new IllegalArgumentException("회원가입 실패! 유저 id가 없어야 합니다.");

        return new User(
                signupReqDto.getUserId(),
                signupReqDto.getUsername(),
                signupReqDto.getPwd(),
                signupReqDto.getEmail(),
                signupReqDto.getPhone(),
                signupReqDto.getCreatedAt(),
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


        this.updatedAt = updateDto.getUpdatedAt();

    }

    public void patchPwd(PwdUpdateDto pwdUpdateDto) {

        if (this.userId != pwdUpdateDto.getUserId())
            throw new IllegalArgumentException("암호 수정 실패! userId가 잘못 입력되었습니다.");

        this.pwd = pwdUpdateDto.getNewPwd();
        this.updatedAt = pwdUpdateDto.getUpdatedAt();
    }
}
