package site.alexkononsol.controllerfortelegrambot.dao;

import android.content.Context;

import java.io.Serializable;
import java.sql.Timestamp;

import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.utils.CityPictureService;

public class CityDao implements Serializable {
    private String name;
    private String text;
    private Timestamp dateCreated;
    private Timestamp dateLastModification;
    private String pictureFilePath;

    public CityDao(City city, Context context){
        this.name = city.getName();
        this.text = city.getText();
        this.dateCreated = city.getDateCreated();
        this.dateLastModification = city.getDateLastModification();
        CityPictureService service = new CityPictureService(context);
        this.pictureFilePath = service.createPictureFile(city);
    }
    public City getCityEquivalent(Context context){
        CityPictureService service = new CityPictureService(context);
        return new City(name,text,service.getPictureAlsStringFromTempFile(pictureFilePath));
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

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public Timestamp getDateLastModification() {
        return dateLastModification;
    }

    public String getPictureFilePath() {
        return pictureFilePath;
    }

    public void setPictureFilePath(String pictureFilePath) {
        this.pictureFilePath = pictureFilePath;
    }
}
