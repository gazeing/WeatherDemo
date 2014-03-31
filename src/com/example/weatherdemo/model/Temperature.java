package com.example.weatherdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Temperature extends InfoEntity {

    @Override
    JSONObject toJson() throws JSONException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String getDay() {
        return mDay;
    }
    
    public void setDay(String strDay) {
        mDay = strDay;
    }
    
    public String getMorning() {
        return mMorning;
    }
    
    public String getEvening() {
        return mEvening;
    }
    
    public String getNight() {
        return mNight;
    }
    
    public String getMinimal() {
        return mMinimal;
    }
    
    public void setMinimal(String strMinimal) {
        mMinimal = strMinimal;
    }
    
    public String getMaximal() {
        return mMaximal;
    }
    
    public void setMaximal(String strMaximal) {
        mMaximal = strMaximal;
    }

    public boolean fromJson(JSONObject aObject) {
        if( null == aObject )
            return false;
        
        mDay = aObject.optString("day");
        mMorning = aObject.optString("morn");
        mEvening = aObject.optString("eve");
        mNight = aObject.optString("night");
        mMinimal = aObject.optString("min");
        mMaximal = aObject.optString("max");
        
        return true;
    }
    
    private String mDay;
    private String mMorning;
    private String mEvening;
    private String mNight;
    private String mMinimal;
    private String mMaximal;
}
