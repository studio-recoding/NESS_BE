## 🪧 About source code
해당 소스 코드는 NESS 서비스의 Springboot 백엔드 서버를 위한 소스 코드입니다. Springboot 백엔드의 전체 아키텍쳐는 다음과 같습니다.
<div>
  <img alt="Spring Boot" src ="https://img.shields.io/badge/spring boot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=white"/>
  <img alt="GCP OAuth" src ="https://img.shields.io/badge/GCP OAuth-4285F4.svg?style=for-the-badge&logo=googlecloud&logoColor=white"/>
  <img alt="Redis" src ="https://img.shields.io/badge/Redis-DC382D.svg?style=for-the-badge&logo=redis&logoColor=white"/>
  <img alt="Docker" src ="https://img.shields.io/badge/docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white"/>
  <img alt="Amazon S3" src ="https://img.shields.io/badge/AWS S3-569A31.svg?style=for-the-badge&logo=amazons3&logoColor=white"/>
  <img alt="Amazon RDS" src ="https://img.shields.io/badge/AWS RDS-527FFF.svg?style=for-the-badge&logo=amazonrds&logoColor=white"/>
  <img alt="Github Actions" src ="https://img.shields.io/badge/Github Actions-232F3E.svg?style=for-the-badge&logo=githubactions&logoColor=white"/>
</div>

<img width="1920" alt="ness-architecture-backend" src="https://github.com/studio-recoding/NESS_BE/assets/89632139/e60789b9-c05c-4baf-9486-fed1bf5b6bc0">

## 👩‍💻 Prerequisites
NESS의 백엔드 서버는 Spring Boot 애플리케이션으로, `JDK 17` & `Spring Boot 3.X` 버전이 만족되어야 합니다. 코드를 실행하기 전 아래의 종속성이 만족되었는지 확인해주세요.
- java version: "17.0.6" 2023-01-17 LTS
- Spring boot version: 3.2.2
- Project: Gradle-Groovy

또한 해당 서비스는 `AWS RDS`, `AWS S3`를 사용합니다. 해당 서비스를 직접 AWS에서 생성하고, application.yml 파일에 다음과 같이 설정해야 합니다. 설정이 `ddl-auto: create`로 되어 있으므로, AWS RDS를 생성하고 연결하기만 하면 DB 테이블 및 어트리뷰트들이 자동으로 생성됩니다.
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  datasource:
    url: jdbc:mysql://<DB 엔드포인트>:3306/<DB 이름> # 엔드포인트 및 DB 이름 입력
    username: <DB 유저네임>
    password: <DB 비밀번호>
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
cloud:
  aws:
    s3:
      bucket: <버킷 이름>
    stack.auto: false
    region.static: <버킷 리전>
    credentials:
      access-key: <AWS 엑세스 키>
      secret-key: <AWS 시크릿 키>
```
메일 전송을 위해서는 지메일의 설정에서 앱 비밀번호를 생성한 후, 지메일 계정과 앱 비밀번호를 모두 입력해줍니다.
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: <지메일 계정>
    password: <지메일 앱 비밀번호>
    properties:
      mail.smtp.debug: true
      mail.smtp.connectiontimeout: 1000 #1초
      mail.starttls.enable: true
      mail.smtp.auth: true
```
디스코드 알림 전송을 위해서는 디스코드 채널을 생성 후, 해당 채널의 웹훅 URL을 생성해 입력해줍니다.
```yaml
discord:
  webhook:
    url: <디스코드 웹훅 URL>
```
구글 소셜 로그인을 위해서는 GCP에서 OAuth 인증을 생성한 후, 클라이언트 ID, 클라이언트 시크릿 그리고 `redirect url`로 사용할 백엔드 도메인 이름을 입력합니다.
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: <OAuth 클라이언트 ID>
            client-secret: <OAuth 클라이언트 시크릿>
            redirect-uri: https://<백엔드 도메인 이름>/login/oauth2/code/google
            authorization-grant-type: authorization_code
            client-authentication-method: client_secret_post
            client-name: Google
            scope:
              - profile
              - email
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://www.googleapis.com/oauth2/v2/userinfo
            user-name-attribute: id
front-end:
  redirect-url: https://<프론트 도메인 이름>
back-end:
  server-name: https://<백엔드 도메인 이름>
```
마지막으로 NESS AI 백엔드의 도메인 이름을 입력합니다. AI 백엔드를 외부 API로 호출하고 있으므로, 반드시 먼저 AI 백엔드가 실행되어야 합니다.
```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          fastapi:
            url: https://<AI 백엔드 도메인 이름>
```

## 🔧 How to build
이 레포지토리는 해당 명령어로 Clone 가능합니다.
```bash
https://github.com/studio-recoding/NESS_BE.git
```
Clone이 완료된 후에는 먼저 위에서 설명한대로 application.yml 파일을 변경합니다. 그 후 프로젝트 경로로 이동한 후, 다음 명령어를 통해서 빌드할 수 있습니다.
```bash
# In Window
gradlew build

# In MacOS or Linux
./gradlew build
```

##  🚀 How to run
빌드가 완료된 후에는 다음 명령어로 로컬의 `8080` 포트에서 실행할 수 있습니다.
```bash
java -jar 빌드된파일명.jar
```

## ✅ How to test
백엔드 서버는 다음과 같은 프로그램을 통해서 로컬에서 API를 테스트할 수 있습니다. `http://localhost:8080/API엔드포인트`로 API를 호출하면 동작을 확인할 수 있습니다.
- POSTMAN

## 📌 Description of used open source
NESS 백엔드 서버는 다음과 같은 오픈 소스를 사용하고 있습니다.
<div>
<img alt="Spring Boot" src ="https://img.shields.io/badge/spring boot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=white"/>
</div>