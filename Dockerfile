FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
COPY ./target/mini1.jar /app/target/mini1.jar
EXPOSE 8080
CMD ["java","-jar","/app/target/mini1.jar"]