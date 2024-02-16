package com.example.news_feed.multimedia.dto;

import com.example.news_feed.multimedia.domain.MultiMedia;
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


    public static MultiMediaDto createMultimediaDto(MultiMedia file) {
        return new MultiMediaDto(
                file.getMultimediaId(),
                file.getPost().getPostId(),
                file.getFileUrl()
        );
    }
}
