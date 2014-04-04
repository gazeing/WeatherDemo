package com.crazybean.framework;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.crazybean.utils.Logger;

public final class UiManager {
	/**
	* method Name:createInstance    
	* method Description:  
	* @return   
	* UiManager  
	* @exception   
	* @since  1.0.0
	 */
	static synchronized UiManager create(UiActivity aActivity) {
		if ( null == mManager ) {
			// Create a new instance of UiManager.
			mManager = new UiManager();
			
			// Set the activity.
			mManager.setActivity(aActivity);
			
			// Initialize the event.
			mManager.initialize();
		}
		
		return mManager;
	}
	
	/**
	* method Name:getInstance    
	* method Description:  
	* @return   
	* UiManager  
	* @exception   
	* @since  1.0.0
	 */
	static synchronized UiManager getInstance() {
		return mManager;
	}
	
	/**
	* method Name:getStorage    
	* method Description:  
	* @return   
	* UiStorage  
	* @exception   
	* @since  1.0.0
	 */
	static synchronized UiStorage getStorage() {
		if ( null == mManager )
			return null;
		
		if ( null == mManager.mStorage ) {
			mManager.mStorage = new UiStorage(UiManager.getContext());
		}
		
		return mManager.mStorage;
	}
	
	/**
	* method Name:setActivity    
	* method Description:  
	* @param aActivity   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void setActivity(final UiActivity aActivity) {
		if ( null == aActivity )
			return ;
		
		mActivity = aActivity;
		
		// Update the context for UiStorage.
		if ( null != mStorage ) {
			mStorage.setContext(mActivity);
		}
	}
	
	/**
	* method Name:getStates    
	* method Description:  
	* @return   
	* StateMachine  
	* @exception   
	* @since  1.0.0
	 */
	StateMachine getStates() {
		return mStates;
	}
	
	/**
	* method Name:getContext    
	* method Description:  
	* @return   
	* Context  
	* @exception   
	* @since  1.0.0
	 */
	protected static Context getContext() {
		UiManager pSelf = UiManager.getInstance();
		return (null != pSelf ? pSelf.mActivity : null);
	}
	
	/**
	* method Name:getVersionName    
	* method Description:  
	* @return   
	* String  
	* @exception   
	* @since  1.0.0
	 */
	public static String getVersionName() {
		UiManager pSelf = UiManager.getInstance();
		return (null != pSelf ? pSelf.mVersionName : "");
	}
	
	/**
	* method Name:getVersionCode    
	* method Description:  
	* @return   
	* int  
	* @exception   
	* @since  1.0.0
	 */
	public static int getVersionCode() {
		UiManager pSelf = UiManager.getInstance();
		return (null != pSelf ? pSelf.mVersionCode : 0);
	}
	
	/**
	* method Name:reset    
	* method Description:     
	* 
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void reset(boolean bExit) {
		// 1. Firstly, we should clear current state machine.
    	if ( null != mStates && bExit ) {
    		mStates.reset();
    	}
    	
    	// 2. Clean up the global data.
    	if ( null != mStorage ) {
    		mStorage.cleanup();
    	}
	}
	
	/**
	 * 
	* Create a new Instance UiManager.  
	 */
	private UiManager() {
		mStates = null;
	}
	
	/**
	* method Name:initialize    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void initialize() {   
        // Load debuggable info.
        loadPackageInfo(mActivity);
		
		// Load states.
        mStates = new StateMachine();
        
		final StateMachine.StateError pError = mStates.loadStates(loadStateTable(mActivity), mDebuggable);
		Logger.log(pError.toString());
		
		// Try to show state machine error.
		if ( (mDebuggable) && (StateMachine.StateError.KErrNone != pError.mErrCode) ) {
			mActivity.postMessage(UiConfig.KEventInit, pError, KErrReport, 0);
		}
	}
	
	/**
	* method Name:loadStateTable    
	* method Description:  
	* @param ctx
	* @return   
	* int[][]  
	* @exception   
	* @since  1.0.0
	 */
	private int[][] loadStateTable(Context ctx) {
		Class<?> pClass = null;
		Field pField = null;
		int[][] pStateTable = null;
		String className = null; 
		String tableName = null;
		
		try {
			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			
			/*	AndroidManifest.xml
			 *  <meta-data android:name="state_table_class_name" android:value="com.soso.features.AppConfig" />
			 *  <meta-data android:name="state_table_variable_name" android:value="KStateTable" />
			 */
			className = bundle.getString("state_table_class_name");
			tableName = bundle.getString("state_table_variable_name");
			pClass = Class.forName(className);
			pField = pClass.getField(tableName);
			
			pStateTable = (int[][])pField.get(null);

		} catch (Exception e) {
			pStateTable = null;
			e.printStackTrace();
		}
			
		Logger.log("class: " + className +", table: " + tableName, Logger.KDebug);
		return 	pStateTable;
	}
	
	
	/**
	* method Name:isEmulator    
	* method Description:  
	* @param context
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	private void loadPackageInfo(final Context aContext) {
		// Get package info.
		mDebuggable = false;
		mVersionName = "";
		mVersionCode = 0;
		if ( null == aContext )
			return ;
		
		try {
			final PackageInfo pInfo = aContext.getPackageManager().getPackageInfo(aContext.getPackageName(), 0);
			if ( null != pInfo ) {
				// Get the debug information.
				final int nFlags = pInfo.applicationInfo.flags;
				mDebuggable = (0 != (nFlags & ApplicationInfo.FLAG_DEBUGGABLE));
				
				// Get the version name.
				mVersionName = pInfo.versionName;
				
				// Save the version code.
				mVersionCode = pInfo.versionCode;
			}
		} catch ( Exception aException ) {
			aException.printStackTrace();
		}
	}
	
	// Static instance for global usage.
	private static UiManager    mManager;
	private StateMachine        mStates;
	private boolean             mDebuggable;  // Indicates whether is running in emulator.
	private String              mVersionName; // Version name for the application.
	private int                 mVersionCode; // Version code for the application used in Google Market.
	private UiActivity          mActivity;
	private static final int    KErrReport = 100; // by ms.
	private UiStorage           mStorage;
}
