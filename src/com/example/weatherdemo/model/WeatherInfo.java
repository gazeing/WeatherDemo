package com.example.weatherdemo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        
        mCity = new City();
        JSONObject coord = aObject.optJSONObject("coord");
        if( null != coord ) {
            mCity.setLocation(coord.optDouble("lat"), coord.optDouble("lon"));
        }
        
        JSONObject sys = aObject.optJSONObject("sys");
        if( null != sys ) {
            mCity.setCountry(sys.optString("country"));
        }
        
        mCity.setName(aObject.optString("name"));
        mCity.setId(aObject.optString("id"));
        
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
            mPressure = main.optString("preesure");
            
            // Create temperature instance.
            mTemp = new Temperature();
            mTemp.setDay(main.optString("temp"));
            mTemp.setMinimal(main.optString("temp_min"));
            mTemp.setMaximal(main.optString("temp_max"));
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
    
    private City      mCity;
    private String    mBase;
    private String    mPressure;
    private String    mHumidity;
    private String    mWindSpeed;
    private String    mWindDeg;
    private String    mTimetag;
    private Temperature mTemp;
    private List<Weather> mWeather;
}
