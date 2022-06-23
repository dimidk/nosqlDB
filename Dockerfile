FROM openjdk:11
ADD target/nosqlDB-0.0.1-SNAPSHOT.jar nosqlDB-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","nosqlDB-0.0.1-SNAPSHOT.jar"]