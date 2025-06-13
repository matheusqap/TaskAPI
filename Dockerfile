FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de configuração do Maven
COPY pom.xml .

# Copia o código-fonte da aplicação
COPY src ./src

# Executa o comando do Maven para limpar, instalar dependências e empacotar o projeto
# O -DskipTests pula os testes aqui, pois teremos uma etapa dedicada para isso no pipeline
RUN mvn clean install -DskipTests

# Criação da imagem
FROM eclipse-temurin:17-jre-focal

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado no estágio anterior para a imagem final
COPY --from=builder /app/target/Token-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação quando o container for executado
ENTRYPOINT ["java", "-jar", "app.jar"]