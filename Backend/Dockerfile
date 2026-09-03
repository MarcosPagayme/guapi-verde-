FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline -Dmaven.test.skip=true

COPY src ./src
RUN mvn -B package -Dmaven.test.skip=true \
    && cp target/*.jar target/guapi-verde.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/guapi-verde.jar ./guapi-verde.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "guapi-verde.jar"]
