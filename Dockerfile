FROM maven:3.9-eclipse-temurin-26-alpine AS builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:26-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
