package xyz.android.pc4_calderon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface jokeAPI{
    @GET("joke/{category}")
    Call<joke> getJoke(
            @Path("category") String category,
            @Query("lang") String language
    );
}