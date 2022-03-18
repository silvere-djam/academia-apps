FROM openjdk:17-alpine
VOLUME /tmp
COPY target/academia-souscription-0.0.1-SNAPSHOT.jar  app.jar
EXPOSE 9965
ENTRYPOINT ["java",  "-Djava.security.egd=file:/dev/./urandom", "-jar",   "app.jar"]