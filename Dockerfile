# etapa de build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# só copiar pom e baixar dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# copiar código e empacotar
COPY src ./src
RUN mvn clean package -DskipTests -B

# etapa de runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# copia o JAR gerado pelo builder
COPY --from=builder /app/target/*.jar app.jar

# expõe porta
EXPOSE 8080

# variáveis de ambiente (podem ser sobrescritas no docker-compose)
ENV SPRING_WEB_RESOURCES_STATIC_LOCATIONS="classpath:/static/" \
    SPRING_DATASOURCE_URL="jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres" \
    SPRING_DATASOURCE_USERNAME="postgres.mlyfewbshxtuifqykroi" \
    SPRING_DATASOURCE_PASSWORD="7c3Du2l1n4Yqsohi" \
    SPRING_JPA_HIBERNATE_DDL_AUTO="update" \
    SPRING_JPA_SHOW_SQL="false" \
    SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL="true" \
    SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT="org.hibernate.dialect.PostgreSQLDialect" \
    SPRINGDOC_SWAGGER_UI_PATH="/swagger-ui" \
    JWT_SECRET="1c685795-bd46-40fe-872a-492805d179a3"

ENTRYPOINT ["java", "-jar", "app.jar"]

