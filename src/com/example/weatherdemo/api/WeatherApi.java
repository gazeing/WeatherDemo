package com.example.weatherdemo.api;

import com.crazybean.network.NetConfig;
import com.crazybean.network.NetTask;
import com.example.weatherdemo.model.AppUtils;

public class WeatherApi {
    private static final int WEATHER_API_BASE   = (NetConfig.NETTASK_ID_USER + 0x100);
    public static final int  WEATHER_API_CITY   = (WEATHER_API_BASE + 1);
    
    /**
     * Get weather information by city name
     * @param strCountry
     * @param strCity
     * @param aObserver
     */
    public static void getWeatherByName(String strCountry, String strCity, NetTask.IObserver aObserver) {
        NetTask pTask = AppUtils.obtainTask(OPENWEATHER_API_URL, 80, WEATHER_API_CITY, NetConfig.HTTP_GET, aObserver);
        pTask.addParam("q", strCity + "," + strCountry);
        
        AppUtils.addTask(pTask);
    }
    
    private WeatherApi() {
    }
    
    private static final String OPENWEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather";
}
