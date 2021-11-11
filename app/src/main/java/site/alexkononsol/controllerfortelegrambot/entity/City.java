package site.alexkononsol.controllerfortelegrambot.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class City implements Serializable {
    private int id;
    private String name;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public City(String name, String text) {
        this.name = name;
        this.text = text;
    }
    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        String cityJson = "{\"id\":null,\"text\":null,\"name\":null}";
        try {
            cityJson = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return cityJson;
    }
}
