package site.alexkononsol.controllerfortelegrambot.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;

public class FileUtils {

    public static void writeLog( String logFilePath , String log){

        try {
            File myFile = new File(logFilePath);
            FileWriter fw = new FileWriter(myFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(log);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LogHelper.TAG,e.getMessage(),e);
        }
    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    public static void cleanFiles(String extension, File dir){
        File[] listFiles = dir.listFiles(new ExtensionFileNameFilter(extension));
        if (listFiles.length > 0){
            for(File file : listFiles){
                Log.d(LogHelper.TAG,"delete cash file " + file.getName());
                file.delete();
            }
        }
    }
    public static class ExtensionFileNameFilter implements FilenameFilter{
        private String ext;
        public ExtensionFileNameFilter(String ext){
            this.ext = ext.toLowerCase();
        }
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(name);
        }
    }

}
