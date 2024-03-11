
FROM maven:3.9.3-amazoncorretto-17 as stage1
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
WORKDIR /usr/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN echo $(ls -1)
RUN mvn clean install -Dmaven.test.skip=true
RUN echo $(ls -1)

FROM openjdk:17
WORKDIR /usr/app
RUN echo $(ls -1)
COPY --from=stage1 /usr/app/target/apipostgres.war /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "apipostgres.war"]




