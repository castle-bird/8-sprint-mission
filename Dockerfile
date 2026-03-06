#########
# 경량화 단계
#########
FROM amazoncorretto:17 AS builder
WORKDIR /app

# 의존성 먼저 (의존성은 딱히 변경 별로 없고 캐시 됨)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x ./gradlew
RUN ./gradlew build -x test --stacktrace || true

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean build -x test

# apline으로 레이어 변경
FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 80
ENV JVM_OPTS=""
ENV TZ=Asia/Seoul

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -Duser.timezone=Asia/Seoul -jar app.jar"]