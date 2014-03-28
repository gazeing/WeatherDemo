package com.example.weatherdemo.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;




import com.example.weatherdemo.R;
import com.example.weatherdemo.views.UiUtils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;

public class LocationProvider implements LocationListener {
	/**
	 * LocationUpdateListener
	 * @author lorenchen
	 */
	public interface LocationUpdateListener {
		/**
		 * onLocationUpdate
		 * @param aLocation
		 */
		public void onLocationUpdate(Location aLocation);
	}
	
	/*
	 * Constructor of LocationProvider
	 */
	public LocationProvider(Context aContext, LocationUpdateListener aListener) {
		mContext = aContext;
		mListener = aListener;
		mManager = (LocationManager)aContext.getSystemService(Context.LOCATION_SERVICE);
		Criteria pCriteria = new Criteria();
		mProvider = mManager.getBestProvider(pCriteria, false);
		if( TextUtils.isEmpty(mProvider) ) {
			mProvider = LocationManager.GPS_PROVIDER;
		}
		
		mLastKnown = mManager.getLastKnownLocation(mProvider);
		mGeocoder = new Geocoder(aContext, Locale.getDefault());
	}
	
	public Location getLastKnown() {
		return mLastKnown;
	}
	
	public void start() {
		if( null != mManager ) {
			mManager.requestLocationUpdates(mProvider, 2000, 50, this);
		}
	}
	
	public void stop() {
		if( null != mManager ) {
			mManager.removeUpdates(this);
		}
	}

	public Address reverseGeocode(Location aLocation) {
		if ((null == mGeocoder) || (null == aLocation))
			return null;
		
		Address pAddress = null;
		try {
			List<Address> aResults = mGeocoder.getFromLocation(aLocation.getLatitude(), aLocation.getLongitude(), 1);
			final int nSize = (null != aResults ? aResults.size() : 0);
			if( nSize > 0 ) {
				pAddress = aResults.get(0);
			}
		} catch (IOException aException) {
			aException.printStackTrace();
			pAddress = null;
		}
		
		return pAddress;
	}

	@Override
	public void onLocationChanged(Location aLocation) {
		mLastKnown = aLocation;
		if ( null != mListener ) {
			mListener.onLocationUpdate(aLocation);
		}
	}

	@Override
	public void onProviderDisabled(String aProvider) {
		UiUtils.makeToast(mContext, R.string.location_disabled);
	}

	@Override
	public void onProviderEnabled(String aProvider) {
		UiUtils.makeToast(mContext, R.string.location_enabled);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	private LocationManager mManager;
	private String mProvider;
	private Location mLastKnown;
	private Geocoder mGeocoder;
	private Context mContext;
	private LocationUpdateListener mListener;
}
