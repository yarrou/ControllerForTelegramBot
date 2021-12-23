package site.alexkononsol.controllerfortelegrambot.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class BackupHelper {
    public static void createBackup() throws IOException {

            File programDir = new File("/sdcard/ControllerForTelegramBot");
            if(!programDir.exists()){
                programDir.mkdir();
            }
            File myFile = new File("/sdcard/ControllerForTelegramBot/backup.bp");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);

            myOutWriter.append(SettingsManager.getStringSettings());
            myOutWriter.close();
            fOut.close();

    }
}
