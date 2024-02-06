package com.example.news_feed.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="auth_history")
public class PwdHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="history_id")
    private Long History;

    @Column(name="old_pwd")
    private String oldPwd;

    @Column(name="updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
