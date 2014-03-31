package com.example.weatherdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.weatherdemo.views.SlideViewGroup;



public class MainPage extends AppPage{
	
	
	View leftView, rightView, mainView;

	
	protected MainPage() {
		super(R.layout.page_iweather);
		
	}
	
	@Override
	protected void onCreate() {
		
		super.onCreate();

//		ActionBar actionBar = getActionBar();
//		if(actionBar!=null){
//			int i =0;
//			i++;
//		}
		
		leftView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.left_navigation, null);
		rightView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.right_menu, null);
		mainView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.middle_main_page, null);
		
		SlideViewGroup svg = (SlideViewGroup) findViewById(R.id.home_slidegroup);
		if(svg != null){
			
			svg.setMainView(R.layout.middle_main_page);
			svg.setLeftView(R.layout.left_navigation);
			svg.setRightView(R.layout.right_menu);
		}

	}
}
