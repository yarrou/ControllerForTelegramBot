package site.alexkononsol.controllerfortelegrambot.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class UserForm implements Serializable {
    private final String userName;
    private final String password;

    public UserForm(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }


    public String getPassword() {
        return password;
    }
    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        String userFormJson = "{\"userName\":null,\"password\":null}";
        try {
            userFormJson = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userFormJson;
    }
}
