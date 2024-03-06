# 💻 NewsFeed Project
### 집단적 독백이 허용되는 공간, 모든 일상적인 이야기를 나누는 커뮤니티 사이트
<br><br>

## 프로젝트 소개
### ◾개발 환경
ㅤㅤ`Java(JDK) 17`, `Spring Boot 3.2.2`, `MySQL 8.0.36`, `JPA`, `Gradle`
### ◾API 명세서 - <a href="https://documenter.getpostman.com/view/29502573/2sA2rGvzCW"> 보러가기 </a>

### ◾ERD
![ERD_ver3](https://github.com/writtenbyrla/news_feed/assets/133620285/301c0847-1551-4119-92d3-fe2883785c7f)


<br><br>

## 주요 기능
### 요구 사항에 맞춰 기능 구현을 진행하였습니다. **[[요구사항]](https://bubble-bladder-d73.notion.site/NewsFeed-fe5539abcf45477e8804ade8a28573fb?pvs=4)  [[Issues]](https://github.com/writtenbyrla/news_feed/issues?q=is%3Aissue+is%3Aclosed)**
<br>

### ◾회원가입 및 로그인
- 패스워드 암호화, `JWT` 토큰 발급과 `Spring Security`를 통한 인증 처리
- OAuth 2.0 기반 `카카오 소셜 로그인` 연동
<br>

### ◾마이페이지
- 패스워드 이력 관리, `AWS S3` 이용한 프로필 이미지 저장
<br>
  
### ◾게시글
- `AWS S3`를 이용한 멀티미디어 파일 저장
- `QueryDSL` 이용한 검색 기능(키워드 / 유저네임)
<br>

### ◾백오피스 회원관리
- 회원 권한 관리, 강제 탈퇴, 게시물 수정 및 삭제 기능
<br>

### ◾댓글 / 좋아요(게시글, 댓글) / 팔로우

<br><br>

## 트러블 슈팅
**게시글 조회 API 응답 시간 `42.97%` 단축, 응답 시간 격차 `99.39%` 개선** 
<br>- `부하 테스트`와 `코드 리팩토링`을 통해 N+1 문제 해결 [[핸들링 기록]](https://writtenbyrla.tistory.com/78)
  
<br><br>

## 단위 / 통합 테스트
Service 단위 테스트 [[핸들링 기록]](https://writtenbyrla.tistory.com/75)
<br>
Controller 통합 테스트 [[핸들링 기록]](https://writtenbyrla.tistory.com/74)
