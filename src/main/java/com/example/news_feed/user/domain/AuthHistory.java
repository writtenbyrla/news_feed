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
public class AuthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="history_id")
    private Long History;

    @Column(name="oldPwd")
    private String oldPwd;

    @Column(name="updated_at")
    private Date updated_at;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
