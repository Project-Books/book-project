FROM openjdk:11
WORKDIR /
ADD target/*.jar app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8081
CMD java -jar app.jar
