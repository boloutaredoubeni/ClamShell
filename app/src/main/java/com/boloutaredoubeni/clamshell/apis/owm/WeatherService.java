package com.boloutaredoubeni.clamshell.apis.owm;

import com.boloutaredoubeni.clamshell.apis.owm.models.CurrentWeather;
import com.boloutaredoubeni.clamshell.apis.owm.models.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public interface WeatherService {

  @GET("/data/2.5/weather")
  Call<CurrentWeather> getCurrentWeather(@Query("lat") double latitude,
                                         @Query("lon") double longitude,
                                         @Query("APPID") String key);

  @GET("/data/2.5/forecast")
  Call<Forecast> getWeeklyForcecast(@Query("lat") double latitude,
                                    @Query("lon") double longitude,
                                    @Query("APPID") String key);
}
