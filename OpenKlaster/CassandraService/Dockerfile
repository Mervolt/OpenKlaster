FROM vertx/vertx3

ENV VERTICLE_NAME com.openklaster.cassandra.app.CassandraVerticle
ENV VERTICLE_FILE target/CassandraService.jar

ENV VERTICLE_HOME /usr/verticles

EXPOSE 8085

COPY $VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]