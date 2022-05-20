package site.alexkononsol.controllerfortelegrambot.connectionsUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import site.alexkononsol.controllerfortelegrambot.entity.City;

public class ServerResponse {
    private String data;
    private int code;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ServerResponse() {
    }

    public ServerResponse(String data, int code) {
        this.data = data;
        this.code = code;
    }
    private Object getObjectData(Type type){
        Gson gson = new Gson();
        Object object = gson.fromJson(data, type);
        return object;
    }
    public List<String> getCitiesList(){
        return  (List<String>) getObjectData(new TypeToken<ArrayList<String>>() {}.getType());
    }
}
