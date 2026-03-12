# Java 17 실행 환경이 포함된 베이스 이미지를 사용합니다.
FROM eclipse-temurin:17-jdk-jammy

# 컨테이너 내부 작업 디렉토리를 /app 으로 설정합니다.
WORKDIR /app

# 로컬에서 빌드된 jar 파일을 컨테이너 내부로 복사합니다.
COPY build/libs/*.jar app.jar

# 스프링 부트 애플리케이션이 사용하는 포트를 명시합니다.
EXPOSE 8080

# 컨테이너 시작 시 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]