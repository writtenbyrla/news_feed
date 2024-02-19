package com.example.news_feed.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DebuggingAspect {

    // 대상 메소드 선택
    @Pointcut("execution(* com.example.news_feed.user.serviceImpl..*.*(..))")
    private void userCut(){}
    @Pointcut("execution(* com.example.news_feed.post.serviceImpl..*.*(..))")
    private void postCut(){}
    @Pointcut("execution(* com.example.news_feed.comment.serviceImpl..*.*(..))")
    private void commentCut(){}
    @Pointcut("execution(* com.example.news_feed.admin.serviceImpl..*.*(..))")
    private void adminCut(){}
    @Pointcut("execution(* com.example.news_feed.follow.serviceImpl..*.*(..))")
    private void followCut(){}
    @Pointcut("execution(* com.example.news_feed.multimedia.serviceImpl..*.*(..))")
    private void multiMediaCut(){}

    // 실행 시점 설정: cut()의 대상이 수행되기 이전에 수행
    @Before("userCut() || postCut() || commentCut() || adminCut() || followCut() || multiMediaCut()")
    public void before(JoinPoint joinPoint){
        // 입력값 가져오기
        Object[] args = joinPoint.getArgs();

        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // 메소드명
        String methodName = joinPoint.getSignature()
                .getName();

        // 입력값 로깅하기
        for (Object obj : args) {
            log.info("{}#{}의 입력값 => {}", className, methodName, obj);
        }

    }

    // 실행 시점 설정: cut()에 지정된 대상 호출 성공 후!
    @AfterReturning(value="userCut() || postCut() || commentCut() || adminCut() || followCut() || multiMediaCut()", returning = "returnObj")
    public void afterReturn(JoinPoint joinPoint, // cut()의 대상 메소드
                                   Object returnObj) { // 리턴값
        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // 메소드명
        String methodName = joinPoint.getSignature()
                .getName();

        // 반환값 로깅
        log.info("{}#{}의 반환값 => {}", className, methodName, returnObj);
    }
}
