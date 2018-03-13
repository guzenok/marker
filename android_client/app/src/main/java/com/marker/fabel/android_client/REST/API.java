package com.marker.fabel.android_client.REST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.Path;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Query;

import com.marker.fabel.android_client.models.User;
import com.marker.fabel.android_client.models.Mark;
import com.marker.fabel.android_client.models.Sheet;

public interface API {
    @POST("/users")
    Call<User> saveUser(@Query("phone") String phone);

    @GET("/sheets/{category}")
    Call<List<Sheet>> getSheets(@Path("category") String category, @Query("txt") String searchTxt);

    @GET("/sheet")
    Call<Sheet> getSheet(@Query("id") Long id);

    @DELETE("/sheet")
    Call<Integer> deleteSheet(@Query("id") Long id);

    @POST("/sheets")
    @FormUrlEncoded
    Call<Sheet> saveSheet(@Field("name") String name);

    @POST("/marks")
    @FormUrlEncoded
    Call<Mark> saveMark(@Field("sheet_id") Long sheet_id, @Field("value") int value, @Field("descr") String descr);

    @POST("/marks/sharing")
    @FormUrlEncoded
    Call<Mark> sharingMark(@Field("sheet_id") Long sheet_id, @Field("phone") String phone);
}
