package com.example.weatherdemo;

import android.support.v7.app.ActionBar;



public class MainPage extends AppPage{

	
	protected MainPage() {
		super(R.layout.page_iweather);
		
	}
	
	@Override
	protected void onCreate() {
		
		super.onCreate();

		ActionBar actionBar = getActionBar();
		if(actionBar!=null){
			int i =0;
			i++;
		}

	}
}
