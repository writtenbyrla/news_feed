package com.example.news_feed.follow.dto.request;

import com.example.news_feed.follow.domain.Follow;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class CreateFollowDto {

    private Long followId;

    @JsonProperty("following_id")
    private Long following_id;

    @JsonProperty("follower_id")
    private Long follower_id;


    public static CreateFollowDto createFollowDto(Follow follow) {
        return new CreateFollowDto(
                follow.getFollowId(),
                follow.getFollowingId().getUserId(),
                follow.getFollowerId().getUserId()
        );
    }
}
