package com.example.weatherdemo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForecastInfo extends InfoEntity {

    @Override
    JSONObject toJson() throws JSONException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean fromJson(JSONObject aObject) {
        if( null == aObject )
            return false;
        
        // Parse city
        JSONObject city = aObject.optJSONObject("city");
        mCity = new City();
        mCity.fromJson(city);
        
        // Parse count.
        mCount = aObject.optInt("cnt");
        if( mCount > 0 ) {
            mForecast = new ArrayList<WeatherInfo>(mCount);
            JSONArray aArray = aObject.optJSONArray("list");
            for( int nIdx = 0; nIdx < mCount; nIdx++ ) {
                JSONObject child = aArray.optJSONObject(nIdx);
                WeatherInfo entity = new WeatherInfo();
                entity.fromJson(child);
                mForecast.add(entity);
            }
        }
        
        return true;
    }

    private int  mCount;
    private City mCity;
    private List<WeatherInfo> mForecast;
}
