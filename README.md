# [MZC팀] MZTI 애플리케이션

# 소개

#### 💡 MZTI : **MZ + MBTI** 의 합성어

### 애플리케이션 LOGO
![로고움직임](https://github.com/TEAMMZC/MZTI/assets/57055730/f170e070-96ea-4ecb-8109-516b429c9546)

## 프로젝트 설명
#### MBTI에 과몰입하는 MZ세대를 위해, 나와 다른 MBTI를 이해할 수 있도록 도와주는 어플리케이션

### 프로젝트 아키텍처

![MZTI drawio](https://github.com/TEAMMZC/MZTI/assets/57055730/fd9a6e82-b805-4955-9cf3-59a64c36af89)

### 사용 기술 스택
#### 안드로이드
<a href="" target="_blank"><img src="https://img.shields.io/badge/ANDROID-3DDC84?style=for-the-badge&logo=Android&logoColor=FFFFFF"/></a>
<a href="" target="_blank"><img src="https://img.shields.io/badge/KOTLIN-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=FFFFFF"/></a>

#### 서버
![Spring](https://img.shields.io/badge/Spring-6DB33F.svg?&style=for-the-badge&logo=Spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white)
![AWS EC2](https://img.shields.io/badge/amazonec2-FF9900.svg?&style=for-the-badge&logo=amazonec2&logoColor=white)
![AWS S3](https://img.shields.io/badge/amazons3-569A31.svg?&style=for-the-badge&logo=amazons3&logoColor=white)
![Docker](https://img.shields.io/badge/docker-2496ED.svg?&style=for-the-badge&logo=docker&logoColor=white)

### 1. MBTI 유형 비교
- 두 개의 MBTI의 유형을 비교할 수 있습니다
- 유형의 종류는 MBTI 요약, 어울리는 일, 연애 성향, 잘맞는 사람, Best 조합, Worst 조합, 자주하는 말, 키워드, 가상인물 입니다.
- 유형은 캐릭터를 선택하여 변경할 수 있습니다

![유형비교](https://github.com/TEAMMZC/MZTI/assets/57055730/63ec8196-abb4-42e0-8d1e-3b6ed0985ff1)

### 2. MBTI 학습
- 16가지 MBTI 중 하나를 선택하여 해당 MBTI를 학습합니다
- 상황과 상황에 대한 행동이 제시되며, 상황 및 행동은 서버로부터 가져와서 보여줍니다
- MBTI 학습이 종료되면 4가지 유형(E/I, N/S, F/T, J/P)에 대한 점수를 바탕으로, 서로 다른 모양의 MBTI 뱃지를 보여줍니다
- MBTI별 뱃지는 프로필 화면에서도 확인할 수 있습니다

![테스트화면](https://github.com/TEAMMZC/MZTI/assets/57055730/11edac11-2928-4fb3-9159-b605b59279ad)


### 3. 프로필
- MBTI 학습 결과로 얻은 MBTI 뱃지를 확인할 수 있습니다
- MBTI 뱃지는 유형별 점수에 따라 모양이 다르게 나타납니다
<img width="417" alt="image" src="https://github.com/TEAMMZC/MZTI/assets/57055730/952673c0-65ec-44f6-944b-64de605379c9">

### 4. 회원 정보 수정
- mbti, 회원 이름, 프로필 이미지를 수정할 수 있습니다

![프로필이미지변경new](https://github.com/TEAMMZC/MZTI/assets/57055730/4e9709c4-938f-414f-9f15-6e310d3c7b95)


### 5. 친구 추가 및 조회, 삭제
- 친구 아이디로 친구 추가를 할 수 있습니다
- 추가한 친구들의 mbti 및 유저이름, 프로필 이미지를 확인할 수 있습니다
- 친구들을 클릭하면 친구의 MBTI 정보를 확인할 수 있습니다
- 친구 프로필을 눌러서 삭제할 수 있습니다
  
![친구추가추가](https://github.com/TEAMMZC/MZTI/assets/57055730/7267ac71-784c-413f-b7ad-0799a97bef00)


### 6. 회원가입
- id, password, 회원 이름, mbti를 입력받아 회원가입합니다
- 비밀번호는 암호화된 상태로 DB에 저장됩니다
- 프로필 사진이 없는 회원은 아래의 mbti별 캐릭터로 프로필 이미지가 표시됩니다

<img width="664" alt="image" src="https://github.com/TEAMMZC/MZTI/assets/57055730/2725be2b-7b0d-472e-b55a-53d868ef2b4b">

### 7. 로그인
- id와 password를 통해 회원가입합니다
- 회원 인증을 위해 JWT 토큰을 사용합니다

![로그인](https://github.com/TEAMMZC/MZTI/assets/57055730/0b529013-027b-4aed-8999-804bca007cfd)


## 설계


## 역할 분담

|파트|이름|
|----|---|
|개발|[김태헌(SERVER)](https://github.com/Jake-huen), [김병수(AOS)](https://github.com/](https://github.com/kimbsu00))|
|기획, 디자인|정다은|

## 협업
- Figma, Notion, Swagger, Discord 를 이용하여 협업하였습니다.
- <img width="587" alt="image" src="https://github.com/TEAMMZC/MZTI/assets/57055730/73bc01f3-533f-4f58-a2ed-43f434a85d57">
- <img width="1448" alt="KakaoTalk_Photo_2023-08-17-05-55-48" src="https://github.com/TEAMMZC/MZTI/assets/57055730/ba7c9fb0-c781-4639-9e04-fb5a1e9f23f9">
- <img width="1451" alt="image" src="https://github.com/TEAMMZC/MZTI/assets/57055730/efb129ae-71fc-4bae-b28c-6327b65af665">





