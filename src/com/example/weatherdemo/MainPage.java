package com.example.weatherdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.weatherdemo.views.SlideViewGroup;

public class MainPage extends AppPage implements OnClickListener {
	
	 public static final String PREFS_NAME = "MyPrefsFile";
	 
	 
	List<CityCountry> city_list = new ArrayList<MainPage.CityCountry>();

	View leftView, rightView, mainView;

	View menu_settingView, menu_skinsView, menu_voteView, menu_feedbackView,
			menu_donateView;

	View arrowView;

	SlideViewGroup svg;
	
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;


	protected MainPage() {
		super(R.layout.page_iweather);

	}
	
	

	@Override
	protected void onDestroy() {
		SharedPreferences settings =getContext().getSharedPreferences(PREFS_NAME, 0);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.clear();
		 for(CityCountry c:city_list){
			 editor.putString(c.city, c.countryCode);
		 }
		 editor.commit();

		super.onDestroy();
	}



	@Override
	protected boolean exitConfirm() {

		return super.exitConfirm();
	}



	@Override
	protected void onCreate() {

		super.onCreate();
		





		svg = (SlideViewGroup) findViewById(R.id.home_slidegroup);
		if (svg != null) {

			leftView = svg.setLeftView(R.layout.left_navigation);
			mainView = svg.setMainView(R.layout.middle_main_page);
			// svg.setRightView(R.layout.right_menu);
		}

		if (leftView != null) {
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

		if (mainView != null) {

		}



		setOptionsMenuRes(R.menu.main_page_menu, true);


		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivate() {
		
		super.onActivate();
		initActionBar();
		
		SharedPreferences settings =getContext().getSharedPreferences(PREFS_NAME, 0);
		
		 Map<String, String> cityMap = (Map<String, String>) settings.getAll();
		 for(Entry<String, String> entry:cityMap.entrySet()){
			 AddWeatherTab(entry.getValue(),entry.getKey());
		 }
		
		 
		

	}
	
	

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME, ActionBar.DISPLAY_SHOW_CUSTOM);
//		actionBar.setDisplayShowTitleEnabled(true);

	
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setTitle("Weather Demo" + getString(R.string.app_name));
		actionBar.setSubtitle("sub-title"); 

		actionBar.show();
		
		 mViewPager = (ViewPager) mainView.findViewById(R.id.mScrollLayout);
		 mTabsAdapter = new TabsAdapter(getFragmentManager(), actionBar);
		 TabListener tabListener = new TabListener() {
			
			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction arg1) {
				Object tag = tab.getTag();
				int position = MainPage.this.mTabsAdapter.findTagPositon(tag);
				if (position>=0)
					 MainPage.this.mViewPager.setCurrentItem(position);
	
			}
			
			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		mTabsAdapter.setTabListener(tabListener);

		 mViewPager.setAdapter(mTabsAdapter);
		 mViewPager.setOnPageChangeListener(mTabsAdapter);


//		 AddWeatherTab("AU","Brisbane");
	




	    }


	
	
private void AddWeatherTab( String country, String cityName) {
	 mTabsAdapter.addTab(getActionBar().newTab().setText(cityName).setContentDescription(country), WeatherFragment.class, null);
//	 mTabsAdapter.notifyDataSetChanged();
	 MainPage.this.city_list.add(new CityCountry(cityName, country));
		
	}

	//
    @Override 
	protected boolean onOptionsItemSelected(int nItemId) {
        Toast.makeText(this.getContext(), "Selected Item: " + nItemId, Toast.LENGTH_SHORT).show();
        switch (nItemId) {
		case R.id.action_add:
			showAddPage();
			break;

		default:
			break;
		}
        return true;
	}


	private void showAddPage() {
		AddCityDialog addCityDialog = new AddCityDialog();
		addCityDialog.setAddCityDialogListener(new AddCityDialogListener() {
			

			
			@Override
			public void userCanceled() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void userSelectedAValue(String coutry, String city) {
				MainPage.this.AddWeatherTab( coutry, city);
				
				
			}
		});
		addCityDialog.ShowDialog(getContext());
		
	}

	@Override
	public void onClick(View aView) {
		// TODO Auto-generated method stub
		super.onClick(aView);

		if (aView == menu_settingView) {
			Log.d("test", "aview = button1");
		}

		if (aView == arrowView) {
			// Log.d("test","aview = button1");
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
	





	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */

	public static class TabsAdapter extends FragmentPagerAdapter implements
			/*ActionBar.TabListener, */ViewPager.OnPageChangeListener {
		public TabsAdapter(FragmentManager fm,  ActionBar ac) {
			super(fm);
			mTabs = new ArrayList<TabInfo>();
//			mReference = new WeakReference<Context>(activity);
			//mViewPager.setAdapter(this);
			mActionBar = ac;
		}

public int findTagPositon(Object tag) {
	for (int i = 0; i < mTabs.size(); i++) {
		if (mTabs.get(i) == tag)  {
		return i;
		}
	}
	return -1;
		}


//		private final WeakReference<Context> mReference;
		private final ActionBar mActionBar;
		private ArrayList<TabInfo> mTabs;
		TabListener tabListener;
		
		

		public void setTabListener(TabListener tabListener) {
			this.tabListener = tabListener;
		}

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;

			}
		}



		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(tabListener);
			
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}



		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);

		}

//		@Override
//		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onTabSelected(Tab tab, FragmentTransaction arg1) {
//			Object tag = tab.getTag();
//			for (int i = 0; i < mTabs.size(); i++) {
//				if (mTabs.get(i) == tag)  {
//				//	mViewPager.setCurrentItem(i);
//				}
//			}
//
//		}
//
//		@Override
//		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
//			// TODO Auto-generated method stub

//		}

		@Override
		public Fragment getItem(int position) {

//			return Fragment.instantiate(mContext, info.clss.getName(),
//					info.args);
			return WeatherFragment.newInstance(mActionBar.getTabAt(position));
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}
	}
	
	
	public class CityCountry{
		String city;
		String countryCode;
		public CityCountry(String city, String countryCode) {
			super();
			this.city = city;
			this.countryCode = countryCode;
		}
		public String getCity() {
			return city;
		}
		public String getCountryCode() {
			return countryCode;
		}
		
	}

}
