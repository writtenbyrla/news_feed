package com.example.news_feed.multimedia.service;

import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.multimedia.repository.MultiMediaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MultiMediaService {
    private final MultiMediaRepository multiMediaRepository;

    // 멀티미디어 등록
    @Transactional
    public MultiMedia uploadFiles(MultiMedia multiMedia) {
        return multiMediaRepository.save(multiMedia);
    }

}
