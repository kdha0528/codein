# STUDY

### Refresh tokens 과 access tokens

    1. 사용자가 로그인 요청 시, 로그인 정보가 맞다면 refresh token과 access token을 생성해 쌍으로 db에 보관

    2. response 시 쿠키에 http 보안 설정과 기간설정을 하여 둘 다 쿠키를 생성

    3. 클라이언트는 서버에게 요청 시 access tokens 만으로 검증 요청

    4. 서버는 access tokens 확인 후, 만약 만료된 토큰이라면 클라이언트에게 만료됐다고 전달

    5. 클라이언트는 access token이 만료됐다는 response를 받으면 refesh token을 보냄

    6. 서버는 refesh token을 받아 검사 후, 비정상일 시 로그아웃 처리를, 정상일 시 refesh token과 access token을 둘 다 새로 발급하여 쿠키 재생성

    7. 클라이언트는 재발급받은 token으로 재요청을 보낸다.

### jwt oauth..? spring security?

### 각종 수정 정보에 대한 로그는 추후 추가 예정

### 신고 기능 추가 예정