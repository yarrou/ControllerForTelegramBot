package site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests;

public enum RequestType {
    GET("GET"),
    DELETE("DELETE"),
    POST("POST"),
    PUT("PUT");

    public final String type;

    private RequestType(String type) {
        this.type = type;
    }
}
