package com.example.news_feed.user.service;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.user.domain.PwdHistory;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
import com.example.news_feed.user.repository.AuthHistoryRepository;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final AuthHistoryRepository historyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    // 기본 프로필 수정
    @Transactional
    public UserUpdateDto updateProfile(Long userId, UserUpdateDto updateDto) {

        updateDto.setUserId(userId);

        // 본인 닉네임 제외해서 중복확인
        Optional<User> checkUsername = userRepository.findByNameAndUserIdNot(updateDto.getUsername(), userId);
        if (checkUsername.isPresent()) {
            throw new HttpException(false, "중복된 사용자가 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        // 기존 유저정보 조회 및 예외 처리
        User target = userRepository.findById(userId)
                .orElseThrow(() ->
                        new HttpException(false, "프로필 수정 실패! 유저 정보가 없습니다.", HttpStatus.BAD_REQUEST)
                );

        // 프로필 수정
        target.patchProfile(updateDto);

        // DB
        User updated = userRepository.save(target);

        // 엔티티를 DTO로 변환해서 반환
        return UserUpdateDto.createUserDto(updated);
    }


    // 패스워드 수정
    @Transactional
    public PwdUpdateDto updatePwd(Long userId, PwdUpdateDto pwdUpdateDto) {

        pwdUpdateDto.setUserId(userId);
        String oldPwd = pwdUpdateDto.getOldPwd(); // 현재 비밀번호 입력값

        // 기존 유저정보 조회 및 예외 처리
        User target = userRepository.findById(userId)
                .orElseThrow(() ->
                        new HttpException(false, "비밀번호 수정 실패! 유저 정보가 없습니다.", HttpStatus.BAD_REQUEST)
                );

        // 기존 등록된 비밀번호와 현재 비밀번호 입력값이 일치하는지 확인
        String currnetPwd = target.getPwd();

        if (!bCryptPasswordEncoder.matches(oldPwd, currnetPwd)) {
            throw new HttpException(false, "현재 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 1.최근 사용한 패스워드 3개 일치여부 확인 후
        // 2. 히스토리에 현재 패스워드 저장
        // 3. 새로운 비밀번호로 업데이트

        // 1. pwds 리스트의 각 요소의 oldPwd 필드와 입력받은 새로운 비밀번호 비교
        List<PwdHistory> pwds = showPwd(userId);
        String newPwd = pwdUpdateDto.getNewPwd();

        boolean newPasswordValid = pwds
                .stream()
                .noneMatch(history -> bCryptPasswordEncoder.matches(newPwd, history.getOldPwd()));

        if(newPasswordValid) {
            // 2. 암호 수정 전에 히스토리 저장
            saveHistory(target, currnetPwd);

            // 암호 수정
            pwdUpdateDto.setNewPwd(bCryptPasswordEncoder.encode(newPwd));
            target.patchPwd(pwdUpdateDto);

            // 3. 새로운 비밀번호로 업데이트
            User updated = userRepository.save(target);

            // 엔티티를 DTO로 변환해서 반환
            return PwdUpdateDto.createResponseDto(updated);
        } else {
            throw new HttpException(false, "최근에 사용한 비밀번호입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 회원정보 수정 시 현재 패스워드 히스토리 테이블에 저장
    @Transactional
    public void saveHistory(User user, String pwd){
        PwdHistory history = new PwdHistory();
        history.setUser(user);
        history.setOldPwd(pwd);
        historyRepository.save(history);
    }

    // 패스워드 최근 3개 조회
    private List<PwdHistory> showPwd(Long userId){
        List<PwdHistory> pwds = historyRepository.findByUserId(userId);
        return pwds;
    }
}