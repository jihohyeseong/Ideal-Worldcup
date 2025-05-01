# Java 17 환경 준비
FROM openjdk:17-jdk-slim

# 임시 파일 저장소 설정
VOLUME /tmp

# build/libs 폴더에 있는 .jar 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# .jar 파일을 실행
ENTRYPOINT ["java","-jar","/app.jar"]
