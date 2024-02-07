package com.example.news_feed.user.domain;

import com.example.news_feed.timestamp.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="pwd_history")
public class PwdHistory extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="history_id")
    private Long History;

    @Column(name="old_pwd")
    private String oldPwd;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
