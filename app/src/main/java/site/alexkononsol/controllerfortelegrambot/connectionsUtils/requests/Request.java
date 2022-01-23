package site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class Request {


    private HttpURLConnection connection;
    private String body;
    private String basicUrl = SettingsManager.getSettings().getHostName();
    private Map<String,String> params;
    private Map<String,String> headers;
    private RequestType type;

    public void setBody(Object object) {
        Gson gson = new Gson();
        this.body = gson.toJson(object);
    }


    public Request(String endpoint, RequestType type){
        this.basicUrl += endpoint;
        this.type = type;
        headers = new HashMap<>();
        params = new HashMap<>();
    }
    public void addParam(String key,String value){
        params.put(key,value);
    }
    public void addLangParam(){
        params.put("lang", Locale.getDefault().getLanguage());
    }
    public void addHeader(String key, String value){
        headers.put(key,value);
    }
    public void addAuthHeader(){
        String authToken = SettingsManager.getSettings().getAuthToken();
        headers.put("auth-token",authToken);
    }
    public void addJsonHeaders(){
        headers.put("Content-Type","application/json; utf-8");
        headers.put("Accept", "application/json");
    }
    public ServerResponse send(){
        ServerResponse serverResponse = new ServerResponse();
        BufferedReader reader=null;
        InputStream stream = null;
        OutputStream os = null;
        try{
            try {
                setAllParams();
                URL url=new URL(basicUrl);
                connection =(HttpURLConnection)url.openConnection();
                connection.setRequestMethod(type.toString());
                connection.setReadTimeout(10000);
                setAllHeaders();
                connection.connect();
                if(body!=null){
                    os = connection.getOutputStream();
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                if(connection.getResponseCode()==200){
                    stream = connection.getInputStream();
                }else {
                    stream = connection.getErrorStream();

                }

                reader= new BufferedReader(new InputStreamReader(stream));
                StringBuilder buf=new StringBuilder();
                String line;
                while ((line=reader.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                serverResponse.setCode(connection.getResponseCode());
                serverResponse.setData(buf.toString());
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
        }catch (IOException e){
            serverResponse.setCode(400);
            serverResponse.setData(e.getLocalizedMessage());
        }
        return serverResponse;
    }
    private void setAllHeaders(){
        if(!headers.isEmpty()){
            for(Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(),header.getValue());
            }
        }
    }
    private String setAllParams(){
        if(!params.isEmpty()){
            basicUrl+="?";
            for(Map.Entry<String, String> param : params.entrySet()) {
                basicUrl = basicUrl + param.getKey() + "=" + param.getValue() + "&";
            }
        }
        return basicUrl;
    }
}
