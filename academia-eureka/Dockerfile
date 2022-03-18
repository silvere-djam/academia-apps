FROM openjdk:17-alpine
VOLUME /tmp
COPY target/academia-eureka-0.0.1-SNAPSHOT.jar  app.jar
EXPOSE 8761
ENTRYPOINT ["java",  "-Djava.security.egd=file:/dev/./urandom", "-jar",   "app.jar"]