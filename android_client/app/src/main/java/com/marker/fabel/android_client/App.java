package com.marker.fabel.android_client;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.widget.Toast;

import okhttp3.Interceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marker.fabel.android_client.REST.API;
import com.marker.fabel.android_client.models.User;

import java.security.SecureRandom;

import java.io.IOException;


public class App extends Application {

    private static API serverAPI;
    private static void makeAPI(Long user_id){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.217.129.113:8080/")
                //.baseUrl("http://172.17.4.2:8080/")
                .client(getHeader(user_id.toString()))
                .addConverterFactory(GsonConverterFactory.create(gson)) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        serverAPI = retrofit.create(API.class); //Создаем объект, при помощи которого будем выполнять запросы
    }
    public static API getApi() {
        return serverAPI;
    }

    private static SharedPreferences preferences;
    private static User user = null;
    private static void loadUser() {
        Long uid = preferences.getLong("U_ID",0);
        String phone = preferences.getString("U_PHONE","");
        if( uid>0 ){
            user = new User();
            user.setId(uid);
            user.setPhone(phone);
        }
        makeAPI(uid);
    }
    public static void setUser(User u) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("U_ID", u.getId());
        editor.putString("U_PHONE", u.getPhone());
        editor.commit();
        user = u;
        makeAPI(u.getId());
    }
    public static User getUser() {
        return user;
    }

    public static void Message(ContextWrapper cw, String text ) {
        Toast toast = Toast.makeText(cw.getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = this.getSharedPreferences("com.marker.fabel.android_client", Context.MODE_PRIVATE);
        loadUser();
    }


    private static OkHttpClient getHeader(final String authorizationValue) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request = null;
                                if (authorizationValue != null) {
                                    Request original = chain.request();
                                    // Request customization: add request headers
                                    Request.Builder requestBuilder = original.newBuilder()
                                            .addHeader("User", authorizationValue);

                                    request = requestBuilder.build();
                                }
                                return chain.proceed(request);
                            }
                        })
                .build();
        return okClient;
    }
}