package com.example.weatherdemo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;



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

public  class TabsAdapter extends FragmentPagerAdapter implements
		 ViewPager.OnPageChangeListener {
	
	private final WeakReference<Context> mReference;
	private final ActionBar mActionBar;
	private ArrayList<TabInfo> mTabs;
//	private ViewPager mPager;
	
	TabListener tListener;
	

	public void setTabListener(TabListener tListener) {
		this.tListener = tListener;
	}



	public ArrayList<TabInfo> getmTabs() {
		return mTabs;
	}



	public void setmTabs(ArrayList<TabInfo> mTabs) {
		this.mTabs = mTabs;
	}



	public TabsAdapter(FragmentManager fm, ViewPager pager, ActionBar ac,
			Context activity) {
		super(fm);
		mTabs = new ArrayList<TabInfo>();
		mReference = new WeakReference<Context>(activity);
		//mViewPager.setAdapter(this);
		mActionBar = ac;
//		mPager = pager;
	}



	final class TabInfo {
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
		tab.setTabListener(tListener);
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

//	@Override
//	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
//		Object tag = tab.getTag();
//		for (int i = 0; i < mTabs.size(); i++) {
//			if (mTabs.get(i) == tag)  {
//				mPager.setCurrentItem(i);
//			}
//		}
//
//	}
//
//	@Override
//	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public Fragment getItem(int position) {

//		return Fragment.instantiate(mContext, info.clss.getName(),
//				info.args);
		return WeatherFragment.newInstance(mActionBar.getTabAt(position));
	}

	@Override
	public int getCount() {
		
		return mTabs.size();
	}
}
