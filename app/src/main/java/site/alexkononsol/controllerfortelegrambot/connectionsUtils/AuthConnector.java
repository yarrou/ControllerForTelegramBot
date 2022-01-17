package site.alexkononsol.controllerfortelegrambot.connectionsUtils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import site.alexkononsol.controllerfortelegrambot.entity.UserForm;
import site.alexkononsol.controllerfortelegrambot.entity.result.AuthResult;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class AuthConnector {
    private String path;
    private Context context;

    public AuthConnector(Context context) {
        this.context = context;
    }

    public AuthResult authRequest(String userName, String password, String endpoint) {
        path = SettingsManager.getSettings().getHostName();
        UserForm userForm = new UserForm(userName, password);
        AuthResult result = new AuthResult();
        result.setStatus(AuthResult.RESULT_STATUS_ERROR);

        BufferedReader reader = null;
        InputStream stream = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            try {
                URL url = new URL(path + endpoint + "?lang=" + Locale.getDefault().getLanguage());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setReadTimeout(10000);
                connection.connect();

                os = connection.getOutputStream();
                byte[] input = userForm.toString().getBytes("utf-8");
                os.write(input, 0, input.length);

                if (connection.getResponseCode() == 200) {
                    stream = connection.getInputStream();
                    result.setStatus(AuthResult.RESULT_STATUS_SUCCESS);
                } else {
                    stream = connection.getErrorStream();

                }
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buf = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                result.setMessage(buf.toString());

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
        }catch (IOException e){
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
