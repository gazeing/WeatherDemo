package com.example.weatherdemo;



import android.os.Bundle;
import android.os.Handler;


public class SplashPage extends AppPage {
	
	private static final int SPLASH_DISPLAY_TIME = 3000;

	protected SplashPage() {
		super(R.layout.page_splash);
		
	}

	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
//				goMainPage();

			}
			
		}, SPLASH_DISPLAY_TIME);
			
	}

	protected void goMainPage() {
		// Save the info.
		Bundle pBundle = new Bundle();
		// Show main page.
		this.postEvent(AppConfig.KShowMain, pBundle);
		
	}
	
	

}
