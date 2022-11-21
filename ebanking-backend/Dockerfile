FROM openjdk:17
EXPOSE 8085
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ebanking-backend.jar
ENTRYPOINT ["java","-jar","/ebanking-backend.jar"]