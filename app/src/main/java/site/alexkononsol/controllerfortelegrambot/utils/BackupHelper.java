package site.alexkononsol.controllerfortelegrambot.utils;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class BackupHelper {
    public static String createBackup(String name) throws IOException {
            String backupDirPath = Environment.getExternalStorageDirectory().getPath()+"/ControllerForTelegramBot";
        Log.d("DEBUG","backup dir path = " + backupDirPath);
            File programDir = new File(backupDirPath);
            if(!programDir.exists()){
                Log.d("DEBUG","created backup dir");
                programDir.mkdir();
            }
            File myFile = new File(backupDirPath + "/" + name + ".bp");
            Log.d("DEBUG","path backup file is "+ myFile.getPath());
            myFile.createNewFile();
            Log.d("DEBUG","backup file created");
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);

            myOutWriter.append(SettingsManager.getStringBackupSettings());
            myOutWriter.close();
            fOut.close();
            return myFile.getPath();
    }
}
