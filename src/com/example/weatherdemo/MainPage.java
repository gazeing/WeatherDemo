package com.example.weatherdemo;

import org.json.JSONObject;

import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.crazybean.utils.Logger;
import com.example.weatherdemo.api.WeatherApi;
import com.example.weatherdemo.model.WeatherInfo;
import com.example.weatherdemo.views.SlideViewGroup;

public class MainPage extends AppPage implements OnClickListener{
	
	View leftView, rightView, mainView;
	
	View menu_settingView,menu_skinsView,menu_voteView,menu_feedbackView,menu_donateView;
	
	View arrowView;
	
	SlideViewGroup svg;

	
	protected MainPage() {
		super(R.layout.page_iweather);
		
	}
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
//		leftView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.left_navigation, null);
//		rightView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.right_menu, null);
//		mainView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.middle_main_page, null);
		
		svg = (SlideViewGroup) findViewById(R.id.home_slidegroup);
		if(svg != null){
			
			leftView =svg.setLeftView(R.layout.left_navigation);
			mainView =svg.setMainView(R.layout.middle_main_page);
			//svg.setRightView(R.layout.right_menu);
		}
		
		if(leftView != null){
			 menu_settingView = leftView.findViewById(R.id.setting);
			 menu_skinsView = leftView.findViewById(R.id.skins);
			 menu_voteView = leftView.findViewById(R.id.vote);
			 menu_feedbackView = leftView.findViewById(R.id.feedback);
			 menu_donateView = leftView.findViewById(R.id.donateApp);
			
			addOnClick(menu_settingView);
			addOnClick(menu_skinsView);
			addOnClick(menu_voteView);
			addOnClick(menu_feedbackView);
			addOnClick(menu_donateView);
			
			menu_settingView.setOnClickListener(this);
		}
		
		if(mainView!=null){
			arrowView = mainView.findViewById(R.id.backIcon);
			arrowView.setOnClickListener(this);
		}
		
//		View arrowView = mainView.findViewById(R.id.backIcon);
//		arrowView.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				svg.slideView(true);
//			}
//		});
		ActionBar actionBar = this.getActionBar();

		WeatherApi.getWeatherByName("AU", "Sydney", this);
		
	}

	
	@Override
	public void onClick(View aView) {
		// TODO Auto-generated method stub
		super.onClick(aView);
		
		if (aView == menu_settingView){
			Log.d("test","aview = button1");
		}
		
		if (aView == arrowView){
//			Log.d("test","aview = button1");
			svg.slideView(true);
		}
	}
	
	@Override
    public boolean handleBack() {
        if( SlideViewGroup.STATUS_SHOW_LEFT == svg.getStatus() ) {
            return svg.slideView(true);
        }
        
        return super.handleBack();
    }
	
//	@Override
//	protected void onViewClick(View aView, int nViewId) {
//		// TODO Auto-generated method stub
//		super.onViewClick(aView, nViewId);
//		
//		
////		switch(aView) {
////		case menu_settingView:
////			
////		{
////			// Start input activity.
////			// Save the info.
////			Log.d("test","aview = button1");
////			
//////			InputFactory.startInput(null, this);
////		}
////		break;
////		}
//		
//		switch(nViewId) 
//		{
//		case R.id.setting:
//			{
//				// Start input activity.
//				// Save the info.
//				Log.d("test","button1");
//				
////				InputFactory.startInput(null, this);
//			}
//			break;
//			
//		case R.id.skins:
//		{
//			// Start history activity.
//			// Save the info.
//			Bundle pBundle = new Bundle();
//			// Show main page.
//			this.postEvent(AppConfig.KHistory, pBundle);
//		}
//		break;
//		
//		
//		case R.id.vote:
//		{
//			// Start generate activity.
//			// Save the info.
//			Bundle pBundle = new Bundle();
//			// Show main page.
//			this.postEvent(AppConfig.KGenerate, pBundle);
//		}
//		break;
//		
//		case R.id.donateApp:
//		{
//			// Start dividends activity.
//			// Save the info.
//			Bundle pBundle = new Bundle();
//			// Show main page.
//			this.postEvent(AppConfig.KDividend, pBundle);
//		}
//		break;
//		}
//	}

	@Override
	protected void onResponse(JSONObject aObject, int aType, int aErrCode) {
	    WeatherInfo weather = new WeatherInfo();
	    weather.fromJson(aObject);
	    Logger.log(weather.toString());
    }

}
