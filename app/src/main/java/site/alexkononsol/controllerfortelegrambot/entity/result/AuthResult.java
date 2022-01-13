package site.alexkononsol.controllerfortelegrambot.entity.result;

public class AuthResult {
    public static final int RESULT_STATUS_SUCCESS = 100;
    public static final int RESULT_STATUS_ERROR = 400;


    int status;
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
