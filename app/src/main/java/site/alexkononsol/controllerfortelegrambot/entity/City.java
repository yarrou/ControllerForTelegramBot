package site.alexkononsol.controllerfortelegrambot.entity;

import com.google.gson.Gson;

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
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
