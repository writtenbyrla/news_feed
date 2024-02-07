package com.example.news_feed.admin.service;

import com.example.news_feed.admin.dto.response.UserDetailDto;
import com.example.news_feed.admin.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;


    public List<UserDetailDto> showAll() {

        return adminUserRepository.findAll()
                .stream()
                .map(UserDetailDto::createUserDetailDto)
                .collect(Collectors.toList());


    }
}
