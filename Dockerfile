# 1. Amazon Corretto 17 베이스 이미지 사용
FROM amazoncorretto:17

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 프로젝트 파일 복사 (.dockerignore에 정의된 파일 제외)
COPY . .

# 4. Gradle Wrapper를 사용하여 애플리케이션 빌드
# (실행 권한 부여 후 빌드 수행, 테스트는 속도를 위해 제외)
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# 5. 80 포트 노출 설정
EXPOSE 80

# 6. 프로젝트 정보 환경 변수 설정
ENV PROJECT_NAME=""
ENV PROJECT_VERSION=""

# 7. JVM 옵션 환경 변수 설정 (기본값 빈 문자열)
ENV JVM_OPTS=""

# 시간대 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 8. 애플리케이션 실행 명령어 설정
# 빌드된 jar 파일 위치: build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar
#ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar build/libs/*.jar"]
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -Duser.timezone=Asia/Seoul -jar build/libs/$(ls build/libs | grep -v 'plain' | head -n 1)"]
#ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]