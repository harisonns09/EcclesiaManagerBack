# ESTÁGIO 1: Compilar o código (Build)
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /workspace/app

# Copia os arquivos do projeto do GitHub para dentro do Google Cloud
COPY pom.xml .
COPY src src

# Roda o build do Maven lá na nuvem
RUN mvn clean package -DskipTests

# ESTÁGIO 2: Rodar a aplicação (Run)
FROM registry.access.redhat.com/ubi8/openjdk-21:1.18

ENV LANGUAGE='en_US:en'

# Copia os arquivos compilados do Estágio 1 para a imagem final
COPY --chown=185 --from=build /workspace/app/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=build /workspace/app/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=build /workspace/app/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=build /workspace/app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"