package site.alexkononsol.controllerfortelegrambot.connectionsUtils;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import site.alexkononsol.controllerfortelegrambot.entity.City;

public interface ApiInterface {
        @GET("city")
        Call<City> getCity(@Query("city") String cityName, @Query("lang") String lang);

        @GET("update")
        Call<String> checkUpdate(@Query("version") String version,@Query("lang") String lang);

        @GET("name")
        Call<String> getNameBot(@Query("lang") String lang);

        @GET("find")
        Call<List<String>> findCity(@Query("city") String cityName, @Query("lang") String lang);

        @DELETE("city")
        Call<String> deleteCity(@Query("city") String cityName, @Header("auth-token") String token, @Query("lang") String lang);

        @POST("login")
        Call<String> login(@Body RequestBody body, @Query("lang") String lang);

        @POST("registration")
        Call<String> registration(@Body RequestBody body,@Query("lang") String lang);

        @Multipart
        @POST("city")
        Call<String> addCity(@Part MultipartBody.Part file, @Part("city") RequestBody city, @Header("auth-token") String token, @Query("lang") String lang);

        @Multipart
        @PUT("city")
        Call<String> changeCity(@Part MultipartBody.Part file,@Part("city") RequestBody city,@Header("auth-token") String token,@Query("lang") String lang);
}
