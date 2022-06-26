package site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.Api;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ApiInterface;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.entity.UserForm;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class RetrofitRequestToServer {
    private final String lang;
    private final String url;
    private ServerResponse serverResponse;
    private ApiInterface apiInterface;
    private final String token;

    public RetrofitRequestToServer() {
        serverResponse = new ServerResponse();
        token = SettingsManager.getSettings().getAuthToken();
        url = SettingsManager.getSettings().getHostName();
        lang = Locale.getDefault().getLanguage();
    }

    public ServerResponse getCity(String cityName) {
        apiInterface = Api.getJsonClient(url);
        Call<City> cityCall = apiInterface.getCity(cityName, lang);
        try {
            Response<City> response = cityCall.execute();
            if (response.isSuccessful()) {
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                serverResponse.setCode(response.code());
                try {
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
        }
        return serverResponse;
    }

    public ServerResponse findCity(String cityName) {
        apiInterface = Api.getJsonClient(url);
        Call<List<String>> cityCall = apiInterface.findCity(cityName, lang);
        try {
            Response<List<String>> response = cityCall.execute();
            if (response.isSuccessful()) {
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                serverResponse.setCode(response.code());
                try {
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return serverResponse;
    }

    public ServerResponse stringRequest(String data, RetrofitRequestType type) {
        Call<String> cityCall;
        apiInterface = Api.getStringClient(url);
        switch (type) {
            case NAME:
                apiInterface = Api.getStringClient(data);
                cityCall = apiInterface.getNameBot(lang);
                break;
            case UPDATE:
                cityCall = apiInterface.checkUpdate(data, lang);
                break;
            default:
                cityCall = apiInterface.deleteCity(data, token, lang);
        }
        try {
            Response<String> response = cityCall.execute();
            if (response.isSuccessful()) {
                LogHelper.logDebug(this, "response is successful");
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                LogHelper.logDebug(this, "response is unsuccessful");
                serverResponse.setCode(response.code());
                try {
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            LogHelper.logError(this, e.getMessage(), e);
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
        }
        return serverResponse;
    }

    public ServerResponse loginOrRegistration(UserForm userForm, RetrofitRequestType type) {
        Call<String> cityCall;
        apiInterface = Api.getStringClient(url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), userForm.toString());
        if (type.type == 2) cityCall = apiInterface.login(body, lang);
        else cityCall = apiInterface.registration(body, lang);
        try {
            Response<String> response = cityCall.execute();
            if (response.isSuccessful()) {
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                serverResponse.setCode(response.code());
                try {
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return serverResponse;
    }

    public ServerResponse addOrChangeCity(byte[] array, City city, RetrofitRequestType type) {
        Call<String> cityCall;
        apiInterface = Api.getStringClient(url);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", "cityImage.jpeg", RequestBody.create(MediaType.parse("image/*"), array));
        RequestBody cityBody = RequestBody.create(MediaType.parse("application/json"), city.toString());
        if (type.type == 7) cityCall = apiInterface.addCity(filePart, cityBody, token, lang);
        else cityCall = apiInterface.changeCity(filePart, cityBody, token, lang);
        try {
            Response<String> response = cityCall.execute();
            if (response.isSuccessful()) {
                serverResponse.setCode(response.code());
                serverResponse.setData(response.body());
            } else {
                LogHelper.logError(this, "unsuccessful response");
                serverResponse.setCode(response.code());
                try {
                    serverResponse.setData(response.errorBody().string());
                } catch (IOException e) {
                    LogHelper.logError(this, e.getLocalizedMessage());
                    serverResponse.setData(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            serverResponse.setCode(500);
            serverResponse.setData(e.getLocalizedMessage());
        }
        return serverResponse;
    }

}
