package site.alexkononsol.controllerfortelegrambot.entity;

import com.google.gson.Gson;

public class Settings {
    String textSize;
    String hostName;
    boolean viewHelpOnStart;
    String authToken;
    String userName;

    public Settings() {
        this.textSize = "normal";
        this.viewHelpOnStart = true;
    }
    public Settings(Settings settings){
        this.textSize = settings.getTextSize();
        this.viewHelpOnStart = settings.isViewHelpOnStart();
        this.hostName = settings.getHostName();
    }

    public String getAuthToken() {

        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public boolean isViewHelpOnStart() {
        return viewHelpOnStart;
    }

    public void setViewHelpOnStart(boolean viewHelpOnStart) {
        this.viewHelpOnStart = viewHelpOnStart;
    }
    public static Settings getSettingsFromString(String value){
        Gson gson = new Gson();
        Settings settings = gson.fromJson(value, Settings.class);
        return settings;
    }
}
