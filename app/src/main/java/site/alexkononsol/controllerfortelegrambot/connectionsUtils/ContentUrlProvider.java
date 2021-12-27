package site.alexkononsol.controllerfortelegrambot.connectionsUtils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class ContentUrlProvider {

    public static List<City> getContentSearch(String path) throws IOException {

        BufferedReader reader=null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        String host = SettingsManager.getSettings().getHostName();
        ArrayList<City> cities = new ArrayList<>();
        String query =  Constants.SEARCH_PATH + RequestEncoder.getRequest(path);
        try {
            URL url=new URL(host+query);
            connection =(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            if(connection.getResponseCode()==200){
                stream = connection.getInputStream();
                reader= new BufferedReader(new InputStreamReader(stream));
                StringBuilder buf=new StringBuilder();
                String line;
                while ((line=reader.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                String json = buf.toString();
                Type listOfCityObject = new TypeToken<ArrayList<City>>() {}.getType();
                Gson gson = new Gson();
                cities = gson.fromJson(buf.toString(), listOfCityObject);
            }//else stream = connection.getErrorStream();
        }

        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
            return cities;
        }
    }

    public static String getContentNameBot(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            if(connection.getResponseCode()==200){
                stream = connection.getInputStream();
            }else stream = connection.getErrorStream();

            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }

        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String getContentGet(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            if(connection.getResponseCode()==200){
                stream = connection.getInputStream();
            }else stream = connection.getErrorStream();

            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }

        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public static String getContentDel(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setReadTimeout(10000);
            connection.connect();
            if (connection.getResponseCode()==200){
                stream = connection.getInputStream();
            }else stream = connection.getErrorStream();

            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String getContentPost(String path,String name,String description) throws IOException {

        City city = new City(name, description);
        BufferedReader reader = null;
        InputStream stream = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(path + "/city");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.connect();

            os = connection.getOutputStream();
            byte[] input = city.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            if (connection.getResponseCode()==200){
                stream = connection.getInputStream();
            }else stream = connection.getErrorStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String getContentPut(String path,String name,String description) throws IOException {
        City city = new City(name, description);
        BufferedReader reader = null;
        InputStream stream = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(path + "/city");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.connect();

            os = connection.getOutputStream();
            byte[] input = city.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            if (connection.getResponseCode()==200){
                stream = connection.getInputStream();
            }else stream = connection.getErrorStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
