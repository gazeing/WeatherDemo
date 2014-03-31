package com.example.weatherdemo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

public class WeatherInfo extends InfoEntity {
    public WeatherInfo() {
    }
    
    @Override
    JSONObject toJson() throws JSONException {
        return null;
    }
    
    public boolean fromJson(JSONObject aObject) {
        if( null == aObject )
            return false;
        
        JSONObject coord = aObject.optJSONObject("coord");
        if( null != coord ) {
            mCoord = new Location("");
            mCoord.setLatitude(coord.optDouble("lat"));
            mCoord.setLongitude(coord.optDouble("lon"));
        }
        
        JSONObject sys = aObject.optJSONObject("sys");
        if( null != sys ) {
            mCountry = sys.optString("country");
        }
        
        mName = aObject.optString("name");
        
        JSONArray weather = aObject.optJSONArray("weather");
        final int length = (null != weather ? weather.length() : 0);
        if( length > 0 ) {
            mWeather = new ArrayList<Weather>(length);
            for( int nIdx = 0; nIdx < length; nIdx++ ) {
                Weather entity = new Weather();
                entity.fromJson(weather.optJSONObject(nIdx));
                mWeather.add(entity);
            }
        }
        
        mBase = aObject.optString("base");
        
        // Wind
        JSONObject main = aObject.optJSONObject("main");
        if( null != main ) {
            mTemp = main.optString("temp");
            mPressure = main.optString("preesure");
            mMinTemp = main.optString("temp_min");
            mMaxTemp = main.optString("temp_max");
        }
        
        // Wind
        JSONObject wind = aObject.optJSONObject("wind");
        if( null != wind ) {
            mWindSpeed = wind.optString("speed");
            mWindDeg = wind.optString("deg");
        }
        
        // DT
        mTimetag = aObject.optString("dt");
        
        return true;
    }
    
    private Location  mCoord;
    private String    mCountry;
    private String    mName;
    private String    mBase;
    private String    mTemp;
    private String    mPressure;
    private String    mHumidity;
    private String    mMinTemp;
    private String    mMaxTemp;
    private String    mWindSpeed;
    private String    mWindDeg;
    private String    mTimetag;
    private List<Weather> mWeather;
}
