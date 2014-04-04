package com.example.weatherdemo;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.crazybean.utils.Logger;
import com.example.weatherdemo.api.WeatherApi;
import com.example.weatherdemo.model.WeatherInfo;
import com.example.weatherdemo.views.SlideViewGroup;

public class MainPage extends AppPage implements OnClickListener {

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
	protected void onCreate() {

		super.onCreate();

		// leftView =
		// ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.left_navigation,
		// null);
		// rightView =
		// ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.right_menu,
		// null);
		// mainView =
		// ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.middle_main_page,
		// null);

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
//			arrowView = mainView.findViewById(R.id.backIcon);
//			arrowView.setOnClickListener(this);
		}

		// View arrowView = mainView.findViewById(R.id.backIcon);
		// arrowView.setOnClickListener(new OnClickListener(){
		// @Override
		// public void onClick(View v) {
		// svg.slideView(true);
		// }
		// });

		initActionBar();

		WeatherApi.getWeatherByName("AU", "Sydney", this);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		 mViewPager = (ViewPager) mainView.findViewById(R.id.mScrollLayout);
		 mTabsAdapter = new TabsAdapter(getFragmentManager(), mViewPager, actionBar, getContext());

		//actionBar.hide();

	        


		 mTabsAdapter.addTab(actionBar.newTab().setText("Sydney"), WeatherFragment.class, null);
//	        mTabsAdapter = new TabsAdapter(this, mViewPager);
//	        mTabsAdapter.addTab(bar.newTab().setText("Simple"),
//	                CountingFragment.class, null);
//	        mTabsAdapter.addTab(bar.newTab().setText("List"),
//	                FragmentPagerSupport.ArrayListFragment.class, null);
//	        mTabsAdapter.addTab(bar.newTab().setText("Cursor"),
//	                CursorFragment.class, null);
//
//	        if (savedInstanceState != null) {
//	            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
//	        }
	    }

//	    @Override
//	    protected void onSaveInstanceState(Bundle outState) {
//	        super.onSaveInstanceState(outState);
//	        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
//	    }

	
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Menu items default to never show in the action bar. On most devices this means
//        // they will show in the standard options menu panel when the menu button is pressed.
//        // On xlarge-screen devices a "More" button will appear in the far right of the
//        // Action Bar that will display remaining items in a cascading menu.
//        menu.add("Normal item");
//
//        MenuItem actionItem = menu.add("Action Button");
//
//        // Items that show as actions should favor the "if room" setting, which will
//        // prevent too many buttons from crowding the bar. Extra items will show in the
//        // overflow area.
//        MenuItemCompat.setShowAsAction(actionItem, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
//
//        // Items that show as actions are strongly encouraged to use an icon.
//        // These icons are shown without a text description, and therefore should
//        // be sufficiently descriptive on their own.
//        actionItem.setIcon(android.R.drawable.ic_menu_share);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//        return true;
//    }

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
	protected void onResponse(JSONObject aObject, int aType, int aErrCode) {
		WeatherInfo weather = new WeatherInfo();
		weather.fromJson(aObject);
		Logger.log(weather.toString());
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
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		public TabsAdapter(FragmentManager fm, ViewPager pager, ActionBar ac,
				Context activity) {
			super(fm);
			mViewPager = pager;
			mContext = activity;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
			mActionBar = ac;
		}

		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

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
			tab.setTabListener(this);
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

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction arg1) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}

		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}
	}

}
