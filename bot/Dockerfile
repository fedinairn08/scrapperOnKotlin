FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY scrapper ./scrapper
COPY link-parser ./link-parser
COPY bot ./bot

RUN gradle :bot:dependencies --no-daemon

COPY . .

RUN gradle :bot:build --no-daemon -x test

FROM eclipse-temurin:17-jre-focal
WORKDIR /app

COPY --from=builder /app/bot/build/libs/bot-*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
