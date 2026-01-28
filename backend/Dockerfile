# --- 第一阶段：构建 (Build) ---
# 使用 Maven 官方镜像作为构建环境
FROM maven:3.9-eclipse-temurin-17 AS build

# 设置工作目录
WORKDIR /app

# 把 pom.xml 先复制进去，下载依赖 (利用 Docker 缓存，这样下次编译更快)
COPY pom.xml .
# 这一步会下载所有 jar 包，比较慢，耐心等待
RUN mvn dependency:go-offline

# 把源代码复制进去
COPY src ./src

# 编译！(跳过测试，为了快一点)
RUN mvn package -DskipTests

# --- 第二阶段：运行 (Run) ---
# 使用轻量级的 Java 17 运行环境
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 从第一阶段把编译好的 jar 包复制过来
# 注意：这里假设你的 jar 包名字叫 slot-machine-backend-0.0.1-SNAPSHOT.jar
# 如果改了 pom.xml 里的名字，这里也要改
COPY --from=build /app/target/*.jar app.jar

# 暴露 8080 端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]