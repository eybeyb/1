package com.ApiConnect;
import com.thundersoft.trash.Model.TrashResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("lajifenlei/index")
    Call<TrashResponse> getTrashClassification(
            @Query("key") String apiKey,
            @Query("word") String keyword
    );
}
