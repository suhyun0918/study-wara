FROM openjdk:11-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/studyolle-0.0.1-SNAPSHOT.jar study-wara.jar
ENTRYPOINT ["java", "-jar", "study-wara.jar"]