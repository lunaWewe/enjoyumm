# 使用 Maven 作為構建階段
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 使用 OpenJDK 17 作為運行階段
FROM openjdk:17-jdk-alpine
WORKDIR /app

# 從構建階段複製應用程序 jar 文件
COPY --from=build /app/target/FinalTest-0.0.1-SNAPSHOT.jar /app/my-app.jar

# 複製 keystore.jks 文件
COPY keystore.jks /app/keystore.jks

# 暴露應用程序的 HTTPS 端口
EXPOSE 443

# 啟動應用程序
CMD ["java", "-jar", "/app/my-app.jar"]
