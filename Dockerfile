# ====== build ======
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# cache de dependências
COPY pom.xml ./
RUN mvn -q -e -DskipTests dependency:go-offline

# copia o código e compila
COPY src ./src
RUN mvn -q -e -DskipTests clean package

# ====== runtime ======
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"

# copia o jar final
COPY --from=build /app/target/*.jar app.jar

# porta padrão do Spring
EXPOSE 8080

# healthcheck leve (pode apontar para actuator quando adicionarmos)
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]