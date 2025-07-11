FROM gradle:8.4-jdk17-focal AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY scrapper ./scrapper
COPY link-parser ./link-parser
COPY bot ./bot

RUN gradle :scrapper:dependencies --no-daemon

COPY . .

RUN gradle :scrapper:build --no-daemon -x test

FROM eclipse-temurin:17-jre-focal
WORKDIR /app

COPY --from=builder /app/scrapper/build/libs/scrapper-*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
