package com.example.news_feed.multimedia.domain;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.CreatePostDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@BatchSize(size = 10)
@Table(name="multimedia")
public class MultiMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="multimedia_id")
    private Long multimediaId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name="file_url", nullable=false)
    private String fileUrl;


    public MultiMedia(String url, Post post) {
        this.fileUrl = url;
        this.post = post;
    }
}
