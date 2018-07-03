package com.example.root.popularmoviesapp2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Servant {
    @GET("movie/popular")
    Call<DataMovieResp> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<DataMovieResp> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<DataTrailerResp> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<DataReviewResp> getMovieReview(@Path("movie_id") int id, @Query("api_key") String apiKey);
}