FROM openjdk:11
WORKDIR /app
#RUN sudo apt update
COPY ./target/ebuisness-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT java -jar "ebuisness-0.0.1-SNAPSHOT.jar"