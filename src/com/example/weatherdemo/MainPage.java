package com.example.weatherdemo;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.crazybean.utils.Logger;
import com.example.weatherdemo.api.WeatherApi;
import com.example.weatherdemo.model.WeatherInfo;
import com.example.weatherdemo.views.SlideViewGroup;

public class MainPage extends AppPage{
	
	View leftView, rightView, mainView;
	
	protected MainPage() {
		super(R.layout.page_iweather);
		
	}
	
	@Override
	protected void onCreate() {
		
		super.onCreate();

		leftView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.left_navigation, null);
		rightView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.right_menu, null);
		mainView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.middle_main_page, null);
		
		SlideViewGroup svg = (SlideViewGroup) findViewById(R.id.home_slidegroup);
		if(svg != null){
			svg.setMainView(R.layout.middle_main_page);
			svg.setLeftView(R.layout.left_navigation);
			svg.setRightView(R.layout.right_menu);
		}
		
		if(leftView != null){
			View menu_settingView = leftView.findViewById(R.id.setting);
			View menu_skinsView = leftView.findViewById(R.id.skins);
			View menu_voteView = leftView.findViewById(R.id.vote);
			View menu_feedbackView = leftView.findViewById(R.id.feedback);
			View menu_donateView = leftView.findViewById(R.id.donateApp);
			
			addOnClick(menu_settingView);
			addOnClick(menu_skinsView);
			addOnClick(menu_voteView);
			addOnClick(menu_feedbackView);
			addOnClick(menu_donateView);
		}

		WeatherApi.getWeatherByName("AU", "Sydney", this);
	}

	
	
	@Override
	protected void onViewClick(View aView, int nViewId) {
		// TODO Auto-generated method stub
		super.onViewClick(aView, nViewId);
		
		switch(nViewId) 
		{
		case R.id.setting:
			{
				// Start input activity.
				// Save the info.
				Log.d("test","button1");
				
//				InputFactory.startInput(null, this);
			}
			break;
			
		case R.id.skins:
		{
			// Start history activity.
			// Save the info.
			Bundle pBundle = new Bundle();
			// Show main page.
			this.postEvent(AppConfig.KHistory, pBundle);
		}
		break;
		
		
		case R.id.vote:
		{
			// Start generate activity.
			// Save the info.
			Bundle pBundle = new Bundle();
			// Show main page.
			this.postEvent(AppConfig.KGenerate, pBundle);
		}
		break;
		
		case R.id.donateApp:
		{
			// Start dividends activity.
			// Save the info.
			Bundle pBundle = new Bundle();
			// Show main page.
			this.postEvent(AppConfig.KDividend, pBundle);
		}
		break;
		}
	}

	@Override
	protected void onResponse(JSONObject aObject, int aType, int aErrCode) {
	    WeatherInfo weather = new WeatherInfo();
	    weather.fromJson(aObject);
	    Logger.log(weather.toString());
    }

}
