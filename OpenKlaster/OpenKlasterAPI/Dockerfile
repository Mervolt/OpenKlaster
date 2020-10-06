FROM vertx/vertx3

ENV VERTICLE_NAME com.openklaster.api.app.ApiVerticle
ENV VERTICLE_FILE target/OpenKlasterAPI.jar

ENV VERTICLE_HOME /usr/verticles

COPY logging.properties logging.properties

# uncomment this when solution for log4j problems with classpath resolved
# ENV JAVA_OPTS "-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory"
ENV JAVA_OPTS "-Djava.util.logging.config.file=logging.properties"

EXPOSE 8082

COPY $VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/* --cluster"]