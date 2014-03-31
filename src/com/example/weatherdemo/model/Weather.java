package com.example.weatherdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather extends InfoEntity {
    @Override
    JSONObject toJson() throws JSONException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean fromJson(JSONObject aObject) {
        if( null == aObject )
            return false;
        
        mId = aObject.optString("id");
        mMain = aObject.optString("main");
        mDesc = aObject.optString("description");
        mIcon = aObject.optString("icon");
        
        return true;
    }
    
    public String getId() {
        return mId;
    }
    
    public String getMain() {
        return mMain;
    }
    
    public String getDesc() {
        return mDesc;
    }
    
    public String getIcon() {
        return mIcon;
    }
    
    private String mId;
    private String mMain;
    private String mDesc;
    private String mIcon;
}
