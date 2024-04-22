FROM amazoncorretto:21
LABEL maintainer="email"
ARG JAR_FILE=build/libs/ClipStudio-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} ClipStudio.jar
ENV docker-app 'Hello, docker!'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ClipStudio.jar"]