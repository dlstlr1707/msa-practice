# Spring-boot security Board Practice

---

## 목차
[1. 개요](#1-개요)

[2. 주요 기능](#2-주요-기능)

[3. Flow Chart](#3-flow-chart)

[4. 인증/인가 흐름](#4-인증인가-흐름)

---

## 1. 개요
수업 때 진행 했었던 게시판에서 Jwt를 활용한 인증과 인가의 개념을 심화 하기 위해 진행

자체 사이트 가입은 Spring서버에서 Security를 활용해 자체적으로 Jwt토큰을 발급해서 인증/인가 진행

네이버 로그인 API를 통해 SNS로그인 추가로 진행

DB 이중화를 통해 이원화하여 DB 부담 경감

기존에 offset방식으로 진행한 페이징 처리 방식을 cursor방식으로 변경하여 성능 개선 

---

## 2. 주요 기능
+ JWT 토큰

  >로그인이나 회원가입 성공 시 accessToken과 refreshToken 생성하여 응답 해줌
  >사이트 재접속시 토큰 여부 확인하여 유효한 accessToken이 존재하면 인증/인가 후 토큰을 복호화해서 회원 정보 화면에 출력
  >accessToken 만료 시 refreshToken으로 유효성 검사 진행
  >유효하면 accessToken, refreshToken 둘다 재발행하고 인증/인가 처리
  >유효하지 않으면 로그인 페이지로 이동 시켜 로그인 다시 하게 만듬

+ Role에 따른 인가

   >ROLE_USER와 ROLE_ADMIN으로 나누고 게시글 삭제의 경우만 ROLE_ADMIN만 이용 가능하게 진행

+ 네이버 로그인

  >인증/인가를 다른 플랫폼에서 진행하여 본 서비스에서 책임을 지지 않아도 되는 장점이 있고 기존에 사용하던 계정으로 가입을 하면서 사용자가 추가 가입하지 않아도 되는 장점이 있어 진행

+ DB 이중화

  >로컬 환경에서 마스터-슬레이브1로 구성하여 수정/삭제/삽입의 경우 마스터에서 진행하고 조회 기능의 경우 슬레이브에서 진행
  >
  >데이터가 많아 졌을 때 DB의 부하 분산을 위해 이중화 진행 

+ Cursor방식의 페이징

  >기존의 offset방식은 비교적 구현이 간단하고 원하는 페이지로 쉽게 뛰어 넘어볼수 있는 장점이 있지만
  >user1이라는 사용자가 5~14번의 게시글 10개를 조회 할때 다른 사용자가 게시글을 수정하거나 삭제 했을때
  >user1이 이전이나 다음 페이지를 요청시 앞서 봤던 게시글을 다시 보게 되는 중복 현상이 일어나게 되기 때문에
  >Cursor방식의 페이징으로 진행

---

## 3. Flow Chart
### 전체 flow chart
![Image](https://github.com/user-attachments/assets/90aed935-63e2-43e5-9d91-0d1ea158c900)

---

### Naver flow chart
![Image](https://github.com/user-attachments/assets/50b8f732-ba51-4428-8eaf-503676577b8a)

---

## 4. 인증/인가 흐름
### 1. 메인 페이지로 접속시 토큰 여부 확인 후 로그인 상태가 아니라면 로그인 페이지로 이동
![Image](https://github.com/user-attachments/assets/2d3406fe-4ea5-4694-b881-ab13d34547b3)
### 2-1. 회원이 아니라면 회원가입을 통해 회원 정보 등록 
![Image](https://github.com/user-attachments/assets/9d769905-4ec9-4f18-912e-c6cd3c7556fe)
#### 2-2. 네이버 계정으로 로그인 원한다면 네이버 로그인 진행
![Image](https://github.com/user-attachments/assets/a8f72fd2-1029-4740-86bf-7554f40f20af)
### 3. 로그인 성공하면 토큰을 발급하고 전체 조회 페이지(메인) 이동 Cursor방식의 페이징 처리
![Image](https://github.com/user-attachments/assets/14455519-2e23-4814-890e-5f9268183895)
![Image](https://github.com/user-attachments/assets/245293d5-3e52-4a77-9f18-ea77b318fd3f)
![Image](https://github.com/user-attachments/assets/e6a95dfe-90b7-48d2-a638-ffed4d275898)
### 3-1. 게시글 작성 버튼 클릭 시 페이지 출력
![Image](https://github.com/user-attachments/assets/e131c5e3-ef6d-4b3a-b842-d45010af3fd4)
### 3-2. 로그아웃 버튼 클릭 시 로그아웃 요청 (토큰 제거 진행)
![Image](https://github.com/user-attachments/assets/c8b6ff22-730d-4eca-aa1e-cce78a153452)
### 3-3. 게시글 제목 클릭 시 게시글 상세 조회 페이지 출력
![Image](https://github.com/user-attachments/assets/f7586aaa-5ece-4b8c-8178-2c32f050edcb)
### 4-1. 본인이 작성한 게시글일 경우만 수정 버튼 활성화하고 버튼 클릭 시 페이지 출력
![Image](https://github.com/user-attachments/assets/eaf40055-8c63-480e-96fa-1fe46de895b3)
### 4-2. 삭제 버튼 클릭 시 ROLE_ADMIN일 경우에만 삭제 진행
![Image](https://github.com/user-attachments/assets/c8cd7982-f428-4bb6-8c34-4a3c76960df9)
### 4-3. 삭제 버튼 클릭 시 ROLE_ADMIN이 아닐 경우 서비스 거부 페이지 출력
![Image](https://github.com/user-attachments/assets/5e2db2b7-6675-4836-b00d-aee4c60d03b7)



