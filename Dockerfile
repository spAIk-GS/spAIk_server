# JDK 17을 사용하는 슬림 이미지를 베이스로 사용
FROM openjdk:17-jdk-slim

# 작업 디렉터리 생성 및 이동
WORKDIR /app

# 빌드된 jar 파일을 컨테이너 내부로 복사
COPY build/libs/*.jar app.jar

# 앱 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
