package site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests;

public enum RetrofitRequestType {
    REGISTRATION(1),
    LOGIN(2),
    GET(3),
    UPDATE(4),
    NAME(5),
    SEARCH(6),
    POST(7),
    PUT(8),
    DELETE(9);

    public final int type;

    private RetrofitRequestType(int type) {
        this.type = type;
    }
}
