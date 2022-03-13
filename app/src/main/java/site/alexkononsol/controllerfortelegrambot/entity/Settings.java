package site.alexkononsol.controllerfortelegrambot.entity;

import com.google.gson.Gson;

public class Settings {
    String textSize;
    String hostName;
    boolean viewHelpOnStart;
    String authToken;
    String userName;
    String backupName;
    boolean isLogging;

    public Settings() {
        this.textSize = "normal";
        this.viewHelpOnStart = true;
        this.backupName = "backup";
    }
    public Settings(Settings settings){
        this.textSize = settings.getTextSize();
        this.viewHelpOnStart = settings.isViewHelpOnStart();
        this.hostName = settings.getHostName();
        this.backupName = settings.getBackupName();
        this.isLogging = settings.isLogging();
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

    public String getBackupName() {
        return backupName;
    }

    public void setBackupName(String backupName) {
        this.backupName = backupName;
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

    public boolean isLogging() {
        return isLogging;
    }

    public void setLogging(boolean logging) {
        isLogging = logging;
    }

    public static Settings getSettingsFromString(String value){
        Gson gson = new Gson();
        Settings settings = gson.fromJson(value, Settings.class);
        return settings;
    }
}
