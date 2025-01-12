FROM gradle:8-jdk-alpine as builder

COPY --chown=gradle:gradle . /
WORKDIR /
RUN gradle bootJar

FROM amazoncorretto:21-alpine-jdk

COPY --from=builder /build/libs/novisign-0.0.1-SNAPSHOT.jar novisign-0.0.1-SNAPSHOT.jar
COPY --from=builder /src/main/resources/application.properties application.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar" , "/novisign-0.0.1-SNAPSHOT.jar", "--spring.config.location=file:./application.properties"]
