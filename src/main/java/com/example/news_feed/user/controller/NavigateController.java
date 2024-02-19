package com.example.news_feed.user.controller;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NavigateController {

    private final MypageService mypageService;

    // 회원가입 페이지 이동
    @GetMapping("/signup-page")
    public String signupPage() {
        return "user/signup";
    }

    // 홈페이지 이동
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null){
            UserDetailDto user = mypageService.showUser(userDetails.getId());
            model.addAttribute("user", user);
        }
        return "main/home";
    }
}
