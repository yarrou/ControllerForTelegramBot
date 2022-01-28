package site.alexkononsol.controllerfortelegrambot.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
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
                Log.d("DEBUG","delete cash file " + file.getName());
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
