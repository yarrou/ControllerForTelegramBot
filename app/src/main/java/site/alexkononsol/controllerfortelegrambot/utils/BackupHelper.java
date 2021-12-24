package site.alexkononsol.controllerfortelegrambot.utils;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class BackupHelper {
    public static void createBackup(String name) throws IOException {
            String backupDirPath = Environment.getExternalStorageDirectory().getPath()+"/ControllerForTelegramBot";
            File programDir = new File(backupDirPath);
            if(!programDir.exists()){
                programDir.mkdir();
            }
            File myFile = new File(backupDirPath + "/backup.bp");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);

            myOutWriter.append(SettingsManager.getStringSettings());
            myOutWriter.close();
            fOut.close();

    }
}
