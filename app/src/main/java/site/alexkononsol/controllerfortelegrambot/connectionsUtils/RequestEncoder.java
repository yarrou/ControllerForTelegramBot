package site.alexkononsol.controllerfortelegrambot.connectionsUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RequestEncoder {
    public static String getRequest(String request) throws UnsupportedEncodingException {
        return URLEncoder.encode(request, "UTF-8");
    }
}
