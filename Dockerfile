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
COPY --from=builder /app/target/conversor-moedas-0.2.0.jar app.jar

# Porta exposta deve corresponder à porta em que a app escuta.
# Em plataformas como Render, a env var PORT geralmente é 10000.
# Nosso app lê PORT e usa 10000 lá; portanto, exponha 10000 aqui.
EXPOSE 10000

# Comando de inicialização
CMD ["java", "-jar", "app.jar", "--server"]
