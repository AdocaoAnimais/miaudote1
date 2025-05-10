FROM eclipse-temurin:21-jdk-ubi9-minimal
COPY ./build/libs/miaudote-0.0.1-SNAPSHOT.jar /miaudote/
WORKDIR /miaudote/
ENTRYPOINT [ "java", "-jar", "miaudote-0.0.1-SNAPSHOT.jar" ]