package site.alexkononsol.controllerfortelegrambot.entity;

import com.google.gson.Gson;

public class Settings {
    String textSize;
    String hostName;
    boolean viewHelpOnStart;

    public Settings() {
        this.textSize = "normal";
        this.viewHelpOnStart = true;
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
