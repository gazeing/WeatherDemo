package com.example.weatherdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

public class City extends InfoEntity {

    @Override
    JSONObject toJson() throws JSONException {
        return null;
    }
    
    public void setLocation(double lat, double lon) {
        if( null == mCoord ) {
            mCoord = new Location("");
        }
        
        mCoord.setLatitude(lat);
        mCoord.setLongitude(lon);
    }
    
    public String getId() {
        return mId;
    }
    
    public void setId(String strId) {
        mId = strId;
    }
    
    public String getCountry() {
        return mCountry;
    }
    
    public void setCountry(String strCountry) {
        mCountry = strCountry;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(String strName) {
        mName = strName;
    }
    
    public int getPopulation() {
        return mPopulation;
    }
    
    public boolean fromJson(JSONObject aObject) {
        if( null == aObject )
            return false;
        
        mId = aObject.optString("id");
        JSONObject coord = aObject.optJSONObject("coord");
        if( null != coord ) {
            mCoord = new Location("");
            mCoord.setLatitude(coord.optDouble("lat"));
            mCoord.setLongitude(coord.optDouble("lon"));
        }
        
        mCountry = aObject.optString("country");
        mName = aObject.optString("name");
        mPopulation = aObject.optInt("population");
        
        return true;
    }

    private String    mId;
    private Location  mCoord;
    private String    mCountry;
    private String    mName;
    private int       mPopulation;
}
