package com.example.weatherdemo.api;

import android.location.Location;
import android.text.TextUtils;

import com.crazybean.network.NetConfig;
import com.crazybean.network.NetTask;
import com.example.weatherdemo.model.AppUtils;

public class WeatherApi {
    private static final int WEATHER_API_BASE    = (NetConfig.NETTASK_ID_USER + 0x100);
    public static final int  WEATHER_CITY_NAME   = (WEATHER_API_BASE + 1);
    public static final int  WEATHER_LOCATION    = (WEATHER_API_BASE + 2);
    public static final int  WEATHER_CITY_ID     = (WEATHER_API_BASE + 3);
    public static final int  FORECAST_CITY_NAME  = (WEATHER_API_BASE + 4);
    public static final int  FORECAST_LOCATION   = (WEATHER_API_BASE + 5);
    public static final int  FORECAST_CITY_ID    = (WEATHER_API_BASE + 6);
    
    /**
     * Get weather information by city name
     * @param strCountry
     * @param strCity
     * @param aObserver
     */
    public static void getWeatherByName(String strCountry, String strCity, NetTask.IObserver aObserver) {
        NetTask pTask = AppUtils.obtainTask(OPENWEATHER_API_URL + API_MODULE_WEATHER, 80, WEATHER_CITY_NAME, NetConfig.HTTP_GET, aObserver);
        pTask.addParam("q", strCity + "," + strCountry);
        
        AppUtils.addTask(pTask);
    }
    
    public static void getWeatherByLocation(Location aLocation, NetTask.IObserver aObserver) {
        if( null == aLocation )
            return ;
        
        NetTask pTask = AppUtils.obtainTask(OPENWEATHER_API_URL + API_MODULE_WEATHER, 80, WEATHER_LOCATION, NetConfig.HTTP_GET, aObserver);
        pTask.addParam("lat", "" + aLocation.getLatitude());
        pTask.addParam("lon", "" + aLocation.getLongitude());
        
        AppUtils.addTask(pTask);
    }
    
    public static void getWeatherByCityId(String strCityId, NetTask.IObserver aObserver) {
        if( TextUtils.isEmpty(strCityId) )
            return ;
        
        NetTask pTask = AppUtils.obtainTask(OPENWEATHER_API_URL + API_MODULE_WEATHER, 80, WEATHER_CITY_ID, NetConfig.HTTP_GET, aObserver);
        pTask.addParam("id", strCityId);
        
        AppUtils.addTask(pTask);
    }
    
    public static void getForecastByName(String strCountry, String strCity, String strPeriod, NetTask.IObserver aObserver) {
        NetTask pTask = AppUtils.obtainTask(WeatherApi.getForecastUrl(strPeriod), 80, FORECAST_CITY_NAME, NetConfig.HTTP_GET, aObserver);
        pTask.addParam("q", strCity + "," + strCountry);
        
        AppUtils.addTask(pTask);
    }
    
    public static void getForecastByLocation(Location aLocation, String strPeriod, NetTask.IObserver aObserver) {
        if( null == aLocation )
            return ;
        
        NetTask pTask = AppUtils.obtainTask(WeatherApi.getForecastUrl(strPeriod), 80, FORECAST_LOCATION, NetConfig.HTTP_GET, aObserver);
        pTask.addParam("lat", "" + aLocation.getLatitude());
        pTask.addParam("lon", "" + aLocation.getLongitude());
        
        AppUtils.addTask(pTask);
    }
    
    public static void getForecastByCityId(String strCityId, String strPeriod, NetTask.IObserver aObserver) {
        if( TextUtils.isEmpty(strCityId) )
            return ;
        
        NetTask pTask = AppUtils.obtainTask(WeatherApi.getForecastUrl(strPeriod), 80, FORECAST_CITY_ID, NetConfig.HTTP_GET, aObserver);
        pTask.addParam("id", strCityId);
        
        AppUtils.addTask(pTask);
    }
    
    private static String getForecastUrl(String strPeriod) {
        String strUrl = OPENWEATHER_API_URL + API_MODULE_FORECAST;
        if( !TextUtils.isEmpty(strPeriod) ) {
            strUrl += ("/" + strPeriod);
        }
        
        return strUrl;
    }
    
    private WeatherApi() {
    }
    
    private static final String OPENWEATHER_API_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_MODULE_WEATHER  = "weather";
    private static final String API_MODULE_FORECAST = "forecast";
}
