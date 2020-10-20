FROM vertx/vertx3

ENV VERTICLE_NAME com.openklaster.filerepository.app.FileRepositoryVerticle
ENV VERTICLE_FILE target/FileRepositoryService.jar

ENV VERTICLE_HOME /usr/verticles

COPY $VERTICLE_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/* --cluster"]