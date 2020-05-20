package service;

import com.datastax.driver.core.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.MappingManager;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public abstract class CassandraHandler {
    public static final String keyspace = "openklaster";
    protected final CassandraClient cassandraClient;
    protected final MappingManager mappingManager;
    protected String route;
    protected String table;
    protected String idType;

    public CassandraHandler(CassandraClient cassandraClient, String route, String table, String idType) {
        this.cassandraClient = cassandraClient;
        this.mappingManager = MappingManager.create(cassandraClient);
        this.route = route;
        this.table = table;
        this.idType = idType;
    }


    public String getRoute(){return this.route;}

    public abstract Handler<RoutingContext> postHandler();

    public Handler<RoutingContext> getHandler() {
        return routingContext -> {
            String receiverIdString = routingContext.request().getParam(idType);
            String startDateUserFormat = routingContext.request().getParam("startDate");
            String endDateUserFormat = routingContext.request().getParam("endDate");

            int receiverId;
            String startDate;
            String endDate;
            try {
                receiverId = Integer.parseInt(receiverIdString);
            } catch(NumberFormatException | NullPointerException e) {
                routingContext.response()
                        .setStatusCode(400)
                        .end();
                return;
            }
            if (startDateUserFormat == null) startDate = "2000-01-01";
            else startDate = parseDate(startDateUserFormat);
            if (endDateUserFormat == null) {
                SimpleDateFormat cassandraFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                endDate = cassandraFormat.format(now);
            }
            else endDate = parseDate(endDateUserFormat);

            String query = "SELECT JSON * FROM " + keyspace + " . " + table  + " WHERE " + idType.toLowerCase() +
                    " = "+ + receiverId + " AND timestamp >= '" +startDate + "' AND timestamp <= '" +endDate + "' ALLOW FILTERING";
            cassandraClient.executeWithFullFetch(query, resultHandler(routingContext));
        };
    }


    protected Handler<AsyncResult<Void>> handler(RoutingContext routingContext){
        return voidAsyncResult -> {
            if (voidAsyncResult.succeeded()) {
                System.out.println("Executed successfully");
                routingContext.response()
                        .setStatusCode(200)
                        .end();
            } else {
                System.out.println("Unable to execute");
                routingContext.response()
                        .setStatusCode(400)
                        .end();
            }
        };
    }

    protected Handler<AsyncResult<List<Row>>> resultHandler(RoutingContext routingContext) {
        return listAsyncResult -> {
            if (listAsyncResult.succeeded()) {
                List<Row> rows = listAsyncResult.result();
                StringBuilder result = new StringBuilder();
                result.append("[");
                for (int i = 0; i < rows.size(); i++) {
                    if (i > 0) result.append(",");
                    result.append(rows.get(i).getString(0));
                }
                result.append("]");
                System.out.println("Executed successfully");
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(result.toString());
            } else {
                System.out.println("Unable to execute");
                routingContext.response()
                        .setStatusCode(400)
                        .end();
            }
        };
    }

    protected String parseDate(String userDate) {
        SimpleDateFormat userFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat cassandraFormat = new SimpleDateFormat("yyyy-MM-dd");
        String cassandraDate = "";
        try {
            cassandraDate = cassandraFormat.format(userFormat.parse(userDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cassandraDate;
    }
}
