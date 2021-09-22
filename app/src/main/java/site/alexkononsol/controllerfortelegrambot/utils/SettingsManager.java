package site.alexkononsol.controllerfortelegrambot.utils;

import com.google.gson.Gson;

import site.alexkononsol.controllerfortelegrambot.entity.Settings;

public class SettingsManager {

    private static Settings settings = new Settings();

    public SettingsManager(){

    }

    public static void initSettings() {

        settings = SharedPreferenceAssistant.getSettingFromSharedPreferences();

        if (settings == null) {
            settings = new Settings();
        }
        settings = getSettings();
    }

    public static Settings getSettings() {

        return settings;
    }

    public static void save() {

        SharedPreferenceAssistant.save(settings);
    }
    public static String getStringSettings(){
        return SharedPreferenceAssistant.getStringSettings(settings);
    }

    public static void restoreSettings(String stringSettings){
        Gson gson = new Gson();
        Settings settings = gson.fromJson(stringSettings, Settings.class);
        SettingsManager.settings = settings;
        save();
    }
}
