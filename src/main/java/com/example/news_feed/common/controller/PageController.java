package com.example.news_feed.common.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.post.service.PostService;
import com.example.news_feed.user.dto.response.UserDetailDto;
import com.example.news_feed.user.service.serviceImpl.MyPageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@RequiredArgsConstructor
public class PageController {

    private final MyPageServiceImpl mypageServiceImpl;
    private final PostService postService;
    private final PostRepository postRepository;

    // 회원가입 페이지 이동
    @GetMapping("/signup-page")
    public String signupPage() {
        return "user/signup";
    }

    // 홈페이지 이동
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null){
            UserDetailDto user = mypageServiceImpl.findById(userDetails, userDetails.getId());
            model.addAttribute("user", user);
        }

        // 10개까지만 받아오기
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<PostDetailDto> postList = postService.showAll(pageable);

        model.addAttribute("postList", postList);
        return "main/home";
    }

    // 피드 페이지
    @GetMapping("/feed")
    public String feed(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null){
            UserDetailDto user = mypageServiceImpl.findById(userDetails, userDetails.getId());
            model.addAttribute("user", user);
        }
        // 10개까지만 받아오기
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<PostDetailDto> postList = postService.showAll(pageable);
        model.addAttribute("postList", postList);
        return "post/feed";
    }



}
