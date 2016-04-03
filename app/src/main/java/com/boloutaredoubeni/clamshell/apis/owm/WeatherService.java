package com.boloutaredoubeni.clamshell.apis.owm;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public interface WeatherService {

  @GET("/data/2.5/weather")
  Call<ResponseBody> getCurrentWeather(@Query("lat") double latitude,
                                       @Query("lon") double longitude,
                                       @Query("APPID") String key);

  @GET("/data/2.5/forecast")
  Call<ResponseBody> getWeeklyForcecast(@Query("lat") double latitude,
                                        @Query("lon") double longitude,
                                        @Query("APPID") String key);
}
