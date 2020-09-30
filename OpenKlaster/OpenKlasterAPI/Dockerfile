# Extend vert.x image
FROM vertx/vertx3

#                                                       (1)
ENV VERTICLE_NAME com.openklaster.api.app.OpenKlasterAPIVerticle
ENV VERTICLE_FILE target/OpenKlasterAPI.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

# uncomment this when solution for log4j problems with classpath resolved
# ENV JAVA_OPTS "-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory"

EXPOSE 8082

# Copy your verticle to the container                   (2)
COPY $VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/* --cluster"]