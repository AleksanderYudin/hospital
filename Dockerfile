From openjdk:8
copy ./target/hospital-0.0.1-SNAPSHOT.jar hospital-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","hospital-0.0.1-SNAPSHOT.jar"]