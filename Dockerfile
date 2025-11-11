# Stage 1: Build com Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar pom.xml e baixar dependências (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fonte e compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (imagem menor)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR do stage anterior (agora é o original, não o shaded)
COPY --from=builder /app/target/conversor-moedas-0.1.0.jar app.jar

# Porta padrão (Render usa PORT env var)
EXPOSE 7000

# Comando de inicialização
CMD ["java", "-jar", "app.jar", "--server"]
