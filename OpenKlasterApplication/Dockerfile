FROM openjdk:8-jdk-alpine
RUN apk update && apk add bash
ARG JAR_FILE=target/OpenklasterApplication-1.0.0.jar
ADD ${JAR_FILE} app.jar
COPY docker/start.sh start.sh
COPY docker/wait-for-it.sh wait-for-it.sh
COPY docker/application.yml application.yml
ENTRYPOINT ["sh", "./start.sh"]

