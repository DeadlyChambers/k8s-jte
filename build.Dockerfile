FROM gradle:jdk11 as gradleimage
COPY . /home/gradle/source
WORKDIR /home/gradle/source
RUN ./gradlew jte

FROM openjdk:11-jre-slim
COPY --from=gradleimage /home/gradle/source/ /app/
WORKDIR /app
RUN "ls -la /app"
ENTRYPOINT ["java", "-jar", "demo.jar"]