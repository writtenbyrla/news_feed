package com.example.news_feed.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigateController {

    // 회원가입 페이지 이동
    @GetMapping("/signup-page")
    public String signupPage() {
        return "user/signup";
    }

    // 홈페이지 이동
    @GetMapping("/home")
    public String home() {
        return "main/home";
    }
}
