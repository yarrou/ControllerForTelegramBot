package site.alexkononsol.controllerfortelegrambot.utils;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class BackupHelper {
    public static String createBackup(String name, Context context) throws IOException {
        String backupDirPath = getBackupDirPath(context);
        Log.d("DEBUG", "backup dir path = " + backupDirPath);
        File programDir = new File(backupDirPath);
        if (!programDir.exists()) {
            Log.d("DEBUG", "created backup dir");
            programDir.mkdir();
        }
        File myFile = new File(backupDirPath + "/" + name + ".bp");
        Log.d("DEBUG", "path backup file is " + myFile.getPath());
        myFile.createNewFile();
        Log.d("DEBUG", "backup file created");
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter =
                new OutputStreamWriter(fOut);

        myOutWriter.append(SettingsManager.getStringBackupSettings());
        myOutWriter.close();
        fOut.close();
        return myFile.getPath();
    }

    public static String createTempBackup( Context context) throws IOException {
        File cacheDir = context.getCacheDir();
        Log.d("DEBUG", "backup dir path = " + cacheDir.getPath());
        File tempBackupFile = File.createTempFile(getBackupName(), ".bp", cacheDir);
        Log.d("DEBUG", "path backup file is " + tempBackupFile.getPath());
        FileOutputStream fOut = new FileOutputStream(tempBackupFile);
        OutputStreamWriter myOutWriter =
                new OutputStreamWriter(fOut);

        myOutWriter.append(SettingsManager.getStringBackupSettings());
        myOutWriter.close();
        fOut.close();
        return tempBackupFile.getPath();
    }
    public static String getBackupDirPath(Context context) throws IOException {
        return context.getExternalFilesDir(null).getCanonicalPath();
    }
    public static String getBackupName(){
        String backupName = SettingsManager.getSettings().getBackupName();
        if (backupName == null || backupName == "") {
            backupName = SettingsManager.getSettings().getHostName().split("://")[1].split("/")[0];
        }
        return backupName;
    }
}
