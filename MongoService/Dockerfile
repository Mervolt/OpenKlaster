FROM vertx/vertx3

ENV VERTICLE_NAME com.openklaster.mongo.app.MongoVerticle
ENV VERTICLE_FILE target/MongoService.jar

ENV VERTICLE_HOME /usr/verticles

COPY $VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/* --cluster"]