package com.example.news_feed.follow.domain;

import com.example.news_feed.common.TimeStamp;
import com.example.news_feed.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name="follow")
public class Follow extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id")
    private Long followId;

    @ManyToOne
    @JoinColumn(name="following_id") // 팔로우 당하는 사람
    private User following;

    @ManyToOne
    @JoinColumn(name="follower_id") // 현재 유저(팔로우를 하는 사람)
    private User follower;


    public static Follow createFollow(User following, User follower) {
        return new Follow(
          null,
          following,
          follower
        );
    }
}
