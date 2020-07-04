package handler;

public class HandlerConfig {
    static int requestDefaultTimeout = 15000;
    static String accessToken = "access_token";
    static String processingErrorMessage = "<h1>Errors during processing the request</h1>";
    static String unprocessableEntityMessage = "<h1>Request entity is unprocessable for this request</h1>";
    static String successfulRequestMessage = "<h1>Successful request</h1>";
}
