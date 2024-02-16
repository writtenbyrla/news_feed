package com.example.news_feed.multimedia.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class MultiMediaDto {
    private Long multiFileId;

    @JsonProperty("post_id")
    private Long postId;

    private String fileUrl;

    public MultiMediaDto(String url, Long postId) {
        this.fileUrl = url;
        this.postId = postId;
    }
}
