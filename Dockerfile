FROM openjdk:17-jdk-slim
WORKDIR /app

# jar 복사
COPY build/libs/spaik-backend-0.0.1-SNAPSHOT.jar app.jar

# wait-for-it.sh 복사
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# DB 준비 후 앱 실행
CMD ["./wait-for-it.sh", "spaik-db-backend:3306", "--", "java", "-jar", "app.jar"]
