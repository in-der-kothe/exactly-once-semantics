FROM openjdk:21-slim

WORKDIR /app

# nur das fertige JAR aus dem Build-Stage übernehmen
COPY ./target/payment.jar payment.jar

# Startkommando
ENTRYPOINT ["java", "-jar", "payment.jar"]
EXPOSE 8080