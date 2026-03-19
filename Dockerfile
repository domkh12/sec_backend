# PHASE 1 - Download & Install JDK
#FROM ghcr.io/graalvm/jdk-community:21
FROM eclipse-temurin:21-jre-alpine
WORKDIR app
ADD ./build/libs/secApi-1.0.jar /app/
EXPOSE 16800
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/secApi-1.0.jar"]