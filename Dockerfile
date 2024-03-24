FROM openjdk:18.0.2
EXPOSE 8080
ADD target/foreign-exchange-application.jar foreign-exchange-application.jar
ENTRYPOINT ["java", "-jar", "/foreign-exchange-application.jar"]