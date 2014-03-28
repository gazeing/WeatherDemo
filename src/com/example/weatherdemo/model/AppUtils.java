package com.example.weatherdemo.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StatFs;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.crazybean.framework.UiStorage;
import com.crazybean.framework.UiStorage.Entity;
import com.crazybean.network.NetConfig;
import com.crazybean.network.NetTask;
import com.crazybean.network.NetworkAgent;
import com.crazybean.utils.Logger;
import com.crazybean.utils.Telephony;

public class AppUtils implements Entity
{
	/**
	* method Name:getInstance    
	* method Description:  
	* @param aContext
	* @return   
	* Preference  
	* @exception   
	* @since  1.0.0
	 */
	public synchronized static AppUtils getInstance()
	{
		AppUtils pUtils = null;
		Object pObject = UiStorage.getEntity(AppUtils.class);
		if ( null == pObject )
		{
			pUtils = new AppUtils();
			UiStorage.setEntity(pUtils);
			pUtils.initialize();
		}
		else
		{
			pUtils = (AppUtils)pObject;
		}
		
		return pUtils;
	}
	
	/**
	 * getPackageName
	 * @return
	 */
	public static String getPackageName() {
		AppUtils pSelf = AppUtils.getInstance();
		if( TextUtils.isEmpty(pSelf.mPackageName) ) {
			Context pContext = UiStorage.getContext();
			if( null != pContext ) {
				pSelf.mPackageName = pContext.getPackageName();
			}
		}
		
		return pSelf.mPackageName;
	}
	
	public static String getDateString() {
		Calendar pCalender = Calendar.getInstance();
		AppUtils pSelf = AppUtils.getInstance();
		return pSelf.mDateFormat.format(pCalender.getTime());
	}
	
	public static String getTimeString() {
		Calendar pCalender = Calendar.getInstance();
		AppUtils pSelf = AppUtils.getInstance();
		return pSelf.mTimeformat.format(pCalender.getTime());
	}
	
	
	public static String getMD5(String strInput) {
		String res = "";
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(strInput.getBytes());
			byte[] md5 = algorithm.digest();
			String tmp = "";
			for (int i = 0; i < md5.length; i++) {
				tmp = (Integer.toHexString(0xFF & md5[i]));
				if (tmp.length() == 1) {
					res += "0" + tmp;
				} else {
					res += tmp;
				}
			}
		} catch (NoSuchAlgorithmException ex) {
		}
		return res;
	}
	
	/**
	 * getCurrentTime
	 * @return
	 */
	public static String getCurrentTime()
	{
		SimpleDateFormat pFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");     
		Date pDate = new Date(System.currentTimeMillis());
		return pFormat.format(pDate);
	}
	
	public static boolean hasExternalStorage() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * obtainTask
	 * @param aHost
	 * @param aPort
	 * @param aType
	 * @param aObserver
	 * @return
	 */
	public static NetTask obtainTask(final String aAddress, int aPort, int aType, NetTask.IObserver aObserver)
	{
		return AppUtils.obtainTask(aAddress, aPort, aType, NetConfig.PROTOCOL_HTTP, NetConfig.HTTP_POST, aObserver);
	}
	
	/**
	 * obtainTask
	 * @param aHost
	 * @param aPort
	 * @param aType
	 * @param aObserver
	 * @return
	 */
	public static NetTask obtainTask(final String aAddress, int aPort, int aType, int nMethod, NetTask.IObserver aObserver)
	{
		return AppUtils.obtainTask(aAddress, aPort, aType, NetConfig.PROTOCOL_HTTP, nMethod, aObserver);
	}
	
	/**
	 * obtainTask
	 * @param aHost
	 * @param aPort
	 * @param aType
	 * @param aProtocol
	 * @param aMethod
	 * @param aObserver
	 * @return
	 */
	public static NetTask obtainTask(final String aAddress, int aPort, int aType, int aProtocol, int aMethod, NetTask.IObserver aObserver)
	{
		AppUtils pSelf = AppUtils.getInstance();
		NetworkAgent pAgent = (null == pSelf ? null : pSelf.mAgent);
		
		return (null != pAgent ? pAgent.obtainTask(aAddress, aPort, aType, aProtocol, aMethod, aObserver) : null);
	}
	
	/**
	 * addTask
	 * @param aTask
	 * @return
	 */
	public static boolean addTask(final NetTask aTask)
	{
		return AppUtils.addTask(aTask, true);
	}
	
	/**
	 * addTask
	 * @param aTask
	 * @param bCancelable
	 * @param bExtraInfo
	 * @return
	 */
	public static boolean addTask(final NetTask aTask, boolean bCancelable)
	{
		if ( null == aTask )
			return false;
		
		AppUtils pSelf = AppUtils.getInstance();
		NetworkAgent pAgent = pSelf.mAgent;
		
		if ( bCancelable )
		{
			pSelf.mTaskType = aTask.getType();
		}
		
		// Set the proxy for the network task.
		AppUtils.processTask(UiStorage.getContext(), aTask);
		
		return pAgent.addTask(aTask);
	}
	
	/**
	 * setProxy
	 * @param aTask
	 * @param aContext
	 */
	public static void processTask(Context aContext, NetTask aTask)
	{
		if ( null == aContext || null == aTask )
			return ;
		
		
		NetworkInfo pActive = AppUtils.getAvailableInfo(aContext);
		if ( null == pActive )
			return ;
		
		// Get network type.
		final int nType = pActive.getType();
		
		if ( ConnectivityManager.TYPE_MOBILE == nType )
		{
			String strApnName = pActive.getExtraInfo();
			//maybe null 
			strApnName = (null == strApnName) ? "" : strApnName;
			if(APN_3GWAP.equalsIgnoreCase(strApnName) || APN_CMWAP.equalsIgnoreCase(strApnName))
			{
				aTask.setProxy(PROXY_3GWAP_CMWAP, PROXY_PORT);
			}
			else if(APN_CTWAP.equalsIgnoreCase(strApnName))
			{
				aTask.setProxy(PROXY_CTWAP, PROXY_PORT);
			}
		}
	}
	
	/**
	 * isMobileNetwork
	 * @return
	 */
	public static boolean isMobileNetwork()
	{
		NetworkInfo pInfo = AppUtils.getAvailableInfo(UiStorage.getContext());
		return (null != pInfo && ConnectivityManager.TYPE_MOBILE == pInfo.getType());
	}
	
	/**
	 * 
	* method Name:isWifiNetwork    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	public static boolean isWifiNetwork()
	{
		NetworkInfo pInfo = AppUtils.getAvailableInfo(UiStorage.getContext());
		return (null != pInfo && ConnectivityManager.TYPE_WIFI == pInfo.getType());
	}
	
	/**
	 * getAvailableInfo
	 * @param aContext
	 * @return
	 */
	private static NetworkInfo getAvailableInfo(Context aContext)
	{
		if ( null == aContext )
			return null;
		
		// Get the connectivity manager.
		ConnectivityManager pManager = (ConnectivityManager)aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if ( null == pManager )
			return null;
		
		// Get current active network information.
		NetworkInfo pInfo = pManager.getActiveNetworkInfo();
		if ( null == pInfo )
		{
			// Get current active network information.
			NetworkInfo[] aAllInfo = pManager.getAllNetworkInfo();
			final int nSize = (null != aAllInfo ? aAllInfo.length : 0);
			for ( int nIdx = 0; nIdx < nSize; nIdx++ )
			{
				NetworkInfo pEntity = aAllInfo[nIdx];
				if ( (null != pEntity) && (pEntity.isAvailable() && (pEntity.isConnectedOrConnecting())) )
				{
					pInfo = pEntity;
					break;
				}
			}
			
			aAllInfo = null;
		}
		
		return pInfo;
	}
	
	/**
	 * doCancel
	 */
	public static void doCancel()
	{
		AppUtils pSelf = AppUtils.getInstance();
		doCancel(pSelf.mTaskType);
	}
	
	/**
	 * doCancel
	 * @param nType
	 */
	public static void doCancel(int nType)
	{
		AppUtils pSelf = AppUtils.getInstance();
		NetworkAgent pAgent = pSelf.mAgent;
		if ( (null != pAgent) && (nType >= 0) )
		{
			pAgent.doCancel(nType);
		}
		
		if ( nType == pSelf.mTaskType )
		{
			pSelf.mTaskType = -1;
		}
	}
	
	/**
	 * addShortcut
	 * Add a new shortcut to home screen.
	 * Need permission: android.permission.GET_TASKS
	 * @return
	 */
	public static boolean addShortcut(int nIconId, int nAppName)
	{
		if ( (0 >= nIconId) || (0 >= nAppName) )
			return false;
		
		Context pContext = UiStorage.getContext();
		if ( null == pContext )
			return false;
		
		ActivityManager pManager = (ActivityManager)pContext.getSystemService(Context.ACTIVITY_SERVICE);
		// Need permission: android.permission.GET_TASKS
		List<RunningTaskInfo> aTasks = (null != pManager ? pManager.getRunningTasks(1) : null);
		if ( (null != aTasks) && (aTasks.size() > 0) )
		{
			// Retrieve the component name
			ComponentName pComponent = ((RunningTaskInfo)(aTasks.get(0))).topActivity;
			
			Intent pShortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			ShortcutIconResource pIconRes = Intent.ShortcutIconResource.fromContext(pContext, nIconId);
			String strAppName = pContext.getString(nAppName);
			pShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, strAppName);
			pShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, pIconRes);
			
			// Set the launch intent.
			Intent pAction = new Intent(Intent.ACTION_MAIN);
			Intent pLaunch = pAction.setComponent(pComponent);
			pAction = null;
			pShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, pLaunch);
			pShortcut.putExtra("duplicate", false);
			
			// Send the broadcast for adding shortcut.
			pContext.sendBroadcast(pShortcut);
			
			// Clean up.
			aTasks = null;
			pLaunch = null;
			strAppName = null;
			pIconRes = null;
			pShortcut = null;
			pComponent = null;
			
			return true;
		}
		
		return false;
	}

	@Override
	public void doFinalize() 
	{
		// Stop the network.
		if ( null != mAgent )
		{
			mAgent.doStop();
			mAgent = null;
		}
		
		// Release the screen awake status.
		this.releaseWake();
		
		this.closeInputStream();
	}
	
	/**
	 * closeInputStream
	 */
	private void closeInputStream()
	{
		if( null != mInputStream )
		{
			try 
			{
				mInputStream.close();
			}catch (IOException aException) {
				aException.printStackTrace();
				Logger.log(aException.toString());
			}
			mInputStream = null;
		}
	}
	
	/**
	 * initialize
	 */
	private void initialize()
	{
		mContext = UiStorage.getContext();
		mAgent = new NetworkAgent();
		mMediaReady = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		
		// Get the power manager.
		if ( null != mContext )
		{
			mPowerMgr = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
			mWakeLock = mPowerMgr.newWakeLock(PowerManager.FULL_WAKE_LOCK, AppUtils.class.toString());
		}
		
		// Format.
		mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		mTimeformat = new SimpleDateFormat("HH:mm");
		
		mTaskType = -1;
	}
	
	
	/**
	 * isAutoBrightness
	 * @return
	 */
	public boolean isAutoBrightness()
	{
		final int KAutoBrightnessMode = 0x01; // Refer to definition: Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
		
		return (KAutoBrightnessMode == this.getAutoBrightnessMode());
	}
	
	/**
	 * getAutoBrightnessMode
	 * @return
	 */
	public int getAutoBrightnessMode()
	{
		// We only check the auto brightness mode in API Level 8 or later.
		if ( (null == mContext) || (ANDROID_2_2_INT != Build.VERSION.SDK_INT) )
		{
			Logger.log("getAutoBrightnessMode, SDK Version: " + Build.VERSION.SDK_INT);
			return -1;
		}
		
		int nAutoBrightnessMode = -1;
	    try 
	    {
	    	// Only available for Android 2.2 (API Level 8) or later.
	    	// To avoid the compile error, we use the manual definition.
	    	final String strBrightnessMode = "screen_brightness_mode"; // Refer to definition: Settings.System.SCREEN_BRIGHTNESS_MODE
	    	
	    	ContentResolver pResolver = mContext.getContentResolver();
	    	nAutoBrightnessMode = Settings.System.getInt(pResolver, strBrightnessMode);
	    	
	    	Logger.log("AutoBrightness: " + nAutoBrightnessMode);
	    }
	    catch (SettingNotFoundException aException) 
	    {
	    	Logger.log(aException.toString());
	    	aException.printStackTrace(); 
	    	nAutoBrightnessMode = -1;
	    }
	    
	    return nAutoBrightnessMode;  
	}
	
	/**
	 * setAutoBrightnessMode
	 * @param nAutoBrightnessMode
	 */
	public void setAutoBrightnessMode(int nAutoBrightnessMode)
	{
		// We only check the auto brightness mode in API Level 8 or later.
		if ( (null == mContext) || (ANDROID_2_2_INT != Build.VERSION.SDK_INT) || (0 > nAutoBrightnessMode) )
		{
			Logger.log("setAutoBrightnessMode, SDK Version: " + Build.VERSION.SDK_INT + ", Mode: " + nAutoBrightnessMode);
			return ;
		}
		
		// Only available for Android 2.2 (API Level 8) or later.
		// To avoid the compile error, we use the manual definition.
		final String strBrightnessMode = "screen_brightness_mode"; // Refer to definition: Settings.System.SCREEN_BRIGHTNESS_MODE
		
		ContentResolver pResolver = mContext.getContentResolver();
		Settings.System.putInt(pResolver, strBrightnessMode, nAutoBrightnessMode);
	}
	
	/**
	 * keepAwake
	 */
	public void acquireWake()
	{
		if ( null != mWakeLock )
		{
			if ( !mAcquired )
			{
				mWakeLock.acquire();
				mAcquired = true;
			}
		}
	}
	
	/**
	 * releaseWake
	 */
	public void releaseWake()
	{
		if ( null != mWakeLock )
		{
			if ( mAcquired )
			{
				mWakeLock.release();
				mAcquired = false;
			}
		}
	}
	
	public void setScreenBrightness(float brightness){
		Window window = ((Activity) mContext).getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = brightness;
		window.setAttributes(lp);
	}
	
	public float getScreenBrightness(){
		Window window = ((Activity) mContext).getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		return lp.screenBrightness;
	}
	
	/**
	 * networkAvailable
	 * @return
	 */
	public static boolean networkAvailable()
	{
		// 1. First, check whether is airplane mode.
		Context pContext = UiStorage.getContext();
		boolean bAirplaneMode = false;
		if ( null != pContext )
		{
			bAirplaneMode = (1 == Settings.System.getInt(pContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0));
		}

		return bAirplaneMode ? Telephony.isWifiConnected(pContext) : Telephony.isNetworkAvailable(pContext);
	}
	
	/**
	 * setMediaReady
	 * @param bMediaReady
	 */
	public void setMediaReady(boolean bMediaReady)
	{
		mMediaReady = bMediaReady;
	}
	
	/**
	 * isMediaReady
	 * @return
	 */
	public boolean isMediaReady()
	{
		return mMediaReady;
	}

	public boolean hasEnoughMassSapce()
	{
		final File path = Environment.getExternalStorageDirectory();
		final StatFs stat = new StatFs(path.getPath());

        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        int blockSize = stat.getBlockSize();

        // Maybe SD card not mount
        if(totalBlocks <= 0) {
        	return false;
        }

        return (availableBlocks * blockSize > 1024L * 1024L * 10L); // 10MB
	}

	public enum TObject { EProgress, EFeedback, EMenu }
	
	
	/**
	 * Constructor of AppUtils
	 */
	private AppUtils()
	{
		mContext = null;
		mPowerMgr = null;
		mWakeLock = null;
		mAcquired = false;
		mAgent = null;
	}
	
	// Member instances.
	private Context            mContext;
	private PowerManager       mPowerMgr;
	private WakeLock           mWakeLock;
	private boolean            mAcquired;
	private int                mTaskType;
	private NetworkAgent       mAgent;
	private boolean            mMediaReady;
	private InputStream        mInputStream;
	private String             mPackageName;
	
	private SimpleDateFormat   mDateFormat;
	private SimpleDateFormat   mTimeformat;
	
	// APN configuration.
	private static final String  APN_3GWAP ="3gwap";
	private static final String  APN_CMWAP ="cmwap";
	private static final String  APN_CTWAP ="ctwap";
	private static final String  PROXY_3GWAP_CMWAP = "10.0.0.172";
	private static final String  PROXY_CTWAP       = "10.0.0.200";
	private static final int 	 PROXY_PORT        = 80;
	private static final int     ANDROID_2_2_INT   = 8;
}
