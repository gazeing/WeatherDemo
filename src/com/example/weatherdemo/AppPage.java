package com.example.weatherdemo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.crazybean.framework.UiPage;
import com.crazybean.network.NetTask;
import com.crazybean.utils.Logger;
import com.crazybean.utils.Telephony;
import com.example.weatherdemo.location.LocationProvider;
import com.example.weatherdemo.model.ImageCache;
import com.example.weatherdemo.model.UserInfo;
import com.example.weatherdemo.views.AppDialog;
import com.example.weatherdemo.views.UiUtils;


public class AppPage extends UiPage implements NetTask.IObserver,
                                               ImageCache.OnImageUpdateListener,
                                               LocationProvider.LocationUpdateListener{
	/**
	 * Create a new Instance AppPage.
	 * 
	 * @param nLayoutId
	 */
	protected AppPage(int nLayoutId) {
		super(nLayoutId, true);
		setCanRotate(false);
		setCacheable(false);
	}

	/**
	 * Create a new Instance AppPage.
	 * 
	 * @param nLayoutId
	 * @param bAnimating
	 */
	protected AppPage(int nLayoutId, boolean bCachable) {
		super(nLayoutId, true);
		setCanRotate(false);
		setCacheable(bCachable);
	}

	/**
	 * method Name:onCreate method Description: void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	@Override
	protected void onCreate() {
		super.onCreate();
		
		// Update animation.
		setAnimation();
		
		/*
		// Get the navigation bar.
		String strIdentifier = "navbar_" + this.getClass().getSimpleName();
		final int nNavbarResId = this.getResources().getIdentifier(strIdentifier, "id", AppUtils.getPackageName());
		if ( nNavbarResId > 0 ) {
			mNavBar = (NavigationBar)findViewById(nNavbarResId);
			if( null != mNavBar ) {
				mNavBar.setIitemClickListener(this);
			}
		} else {
			Logger.log("NavBar ResId (R.id." + strIdentifier + ") doesn't exists!");
		}*/
		
		// Initialize the instance.
		doInit();
	}

	/**
	 * method Name:onActivate method Description: void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	@Override
	protected void onActivate() {
		// Call the base implementation.
		super.onActivate();
		
		this.startGps();
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();

		this.stopGps();
	}
	
	@Override
	protected void onDestroy() {
		// Release the gps.
		this.stopGps(true);
		
		super.onDestroy();
	}

	/**
	 * method Name:checkNetwork method Description:
	 * 
	 * @return boolean
	 * @exception
	 * @since 1.0.0
	 */
	protected boolean checkNetwork() {
		// Check whether network is available.
		final boolean bAvailable = Telephony.isNetworkAvailable(getContext());
		if (!bAvailable) {
			// TODO: Show the error message for network.
		}

		return bAvailable;
	}

	/**
	 * onButtonClick
	 */
	@Override
	protected void onButtonClick(int nButtonId) {
		switch (nButtonId) {
		case KWirelessSetting:
			Telephony.startWirelessSetting(getContext());
			break;
		default:
			super.onButtonClick(nButtonId);
			break;
		}
	}

	/**
	 * setAnimation
	 */
	private void setAnimation() {
		// Set default animations.
		setForwardAnimIds(R.anim.in_rightleft, R.anim.out_rightleft, true);
		setBackwardAnimIds(R.anim.in_leftright, R.anim.out_leftright, true);
	}
	
	/*
	@Override
	public void onNavItemClick(boolean bLeft) {
		if( bLeft ) {
			postEvent(UiConfig.KEventBack);
		} else {
			onNavRightItemClick();
		}
	}*/

	/**
	 * sub-class can rewrite the function by add a special action.
	 */
	protected void onNavRightItemClick() {
		this.setForwardAnimIds(android.R.anim.fade_in, android.R.anim.fade_out, false);
		postEvent(AppConfig.KShowMain);
	}

	/**
	 * method Name:doInit method Description: void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void doInit() {
		DataManager pManager = DataManager.getInstance();
		if ((null == pManager) || (pManager.initialized()))
			return;

		// Load the preference content.
		Context pContext = getContext();
		Preference.getInstance(pContext);
		
		// Load the information from local.
		//pManager.loadFromLocal();

		// Set the flag.
		pManager.setInitialized(true);
	}
	
	@Override
	public void handleResult(byte[] aData, int aType, int aErrCode) {
	    JSONObject aObject = null;
        try {
            String strInput = new String(aData);
            aObject = new JSONObject(strInput);
        } catch (JSONException aException) {
            aException.printStackTrace();
        } finally {
            onResponse(aObject, aType, aErrCode);
        }
	}

	@Override
	public void notifyData(byte[] aData, int arg1, int arg2) {
	}

	@Override
	protected void handleMessage(int nMessageId, Object aObject) {
		switch (nMessageId) {
		case UiPage.KShowInputMethod:
			super.showInputMethod();
			break;
		default:
			break;
		}
	}
	
	protected void onResponse(JSONObject aObject, int aType, int aErrCode) {
	}
	
	protected void showProgress() {
		this.showProgress(R.string.network_waiting);
	}
	
	protected void addOnClick(int nViewId) {
		this.addOnClick(findViewById(nViewId));
	}
	
	protected boolean exitConfirm()
	{
		String strAppName = this.getString(R.string.app_name);
		String strExitMsg = this.getString(R.string.exit_confirm, strAppName);
		UiUtils.showDialog(this.getContext(), strAppName, strExitMsg, R.string.okay, R.string.cancel, new AppDialog.OnClickListener() {
			@Override
			public void onDialogClick(AppDialog aDialog, int nButtonId) {
				if (AppDialog.BUTTON_POSITIVE == nButtonId) {
					// Exit the application.
					exitApp();
				}
			}
		});
		return true;
	}
	
	protected void onImageSelected(Bitmap aBitmap) {
		// Inherited class should override the function to update.
	}
	
	protected void onContactSelected(UserInfo aUserInfo) {
		// Inherited class should override the function.
	}
	
	@Override
	public void onImageUpdate(String strId, Bitmap aBitmap) {
	}

	@Override
	public void onImageError(String strId) {
	}
	
	@Override
	protected void onActivityResult(int nRequestCode, int nResultCode, Intent aData) {
		if (REQUEST_TAKE_PHOTO == nRequestCode) {
	        if (Activity.RESULT_OK == nResultCode) 
			{
	        	Bundle pBundle = aData.getExtras();
	        	Bitmap pBitmap = (null != pBundle ? (Bitmap) pBundle.get("data") : null);
	        	if ( null != pBitmap ) {
	        		this.onImageSelected(pBitmap);
	        	}
	         }
		} else if ((REQUEST_SELECT_PHOTO) == nRequestCode) {
            Uri pImageUri = (null != aData ? aData.getData() : null);
            String strImagePath = getPath(pImageUri);
            if (!TextUtils.isEmpty(strImagePath)) {
            	Bitmap pBitmap = BitmapFactory.decodeFile(strImagePath); 
                if ( null != pBitmap ) {
                	this.onImageSelected(pBitmap);
                }
            }
		} else if (REQUEST_SEL_CONTACT == nRequestCode) {
			if (Activity.RESULT_OK == nResultCode) {
				Uri pContactUri = (null != aData ? aData.getData() : null);
				if( null != pContactUri ) {
					UserInfo pUserInfo = new UserInfo();
					ContentResolver pResolver = getContext().getContentResolver();
					
					// Phone number
					String strContactId = pContactUri.getLastPathSegment();
					Cursor cursor = pResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ strContactId, null, null);
		            if (cursor.moveToFirst()) {
		            	String strNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		            	pUserInfo.setPhone(strNumber);
		            }
		            cursor.close();
					
					cursor = pResolver.query(Email.CONTENT_URI,  null, Email.CONTACT_ID + "=?", new String[] { strContactId }, null);
					int nIndex = cursor.getColumnIndex(Email.DATA);
					
					// Get Email address
					if(cursor.moveToFirst()) {
						String strEmail = cursor.getString(nIndex);
						pUserInfo.setEmail(strEmail);
					} else {
						Logger.log("Retrieve failed");
					}
					cursor.close();
					
					// Get name parameters.
					String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = " + strContactId; 
					String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };
					cursor = pResolver.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
					
				    if(cursor.moveToFirst()) {
				    	String given = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
				    	pUserInfo.setFirstName(given);
				        String family = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
						pUserInfo.setLastName(family);
						String prefix = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
						pUserInfo.setPrefix(prefix);
						String suffix = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));
						pUserInfo.setSuffix(suffix);
					} else {
						Logger.log("Retrieve failed");
					}
				    cursor.close();
					
					// Notify the user information updated.
					this.onContactSelected(pUserInfo);
				}
			}
		}
	}
	
	private String getPath(Uri uri) {
		if(null == uri)
			return null;
		
        String[] projection = { MediaStore.Images.Media.DATA };
        String strPath = null;
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            strPath = cursor.getString(column_index); 
            //cursor.close();
        }
        
        return strPath;
    }
	
	protected void showPhotoOptions() {
		UiUtils.showDialog(this.getContext(), R.string.options, R.string.set_avatar_message, R.string.take_picture, R.string.from_album, new AppDialog.OnClickListener() {
			@Override
			public void onDialogClick(AppDialog aDialog, int nButtonId) {
				if(AppDialog.BUTTON_POSITIVE == nButtonId) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
			        startActivity(intent, REQUEST_TAKE_PHOTO);
				} else if (AppDialog.BUTTON_NEGATIVE == nButtonId) {
					Intent pIntent = new Intent(Intent.ACTION_GET_CONTENT);
					pIntent.setType("image/*");
	                startActivity(Intent.createChooser(pIntent, "Select Picture"), REQUEST_SELECT_PHOTO);
				}
			}
		});
	}
	
	/**
	 * Permission required: <uses-permission android:name="android.permission.READ_CONTACTS"/>
	 */
	protected void showContacts() {
		Intent pIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		this.startActivity(pIntent, REQUEST_SEL_CONTACT);
	}
	
	@Override
	public void onLocationUpdate(Location aLocation) {
		mLastKnown = aLocation;
	}
	
	protected Address reverseGeocode(Location aLocation) {
		return (null != mProvider ? mProvider.reverseGeocode(aLocation) : null);
	}
	
	protected void enableLocation() {
		if( null == mProvider ) {
			mProvider = new LocationProvider(getContext(), this);
		}
	}
	
	protected void startGps() {
		if (null != mProvider) {
			mProvider.start();
		}
	}
	
	protected void stopGps() {
		this.stopGps(false);
	}
	
	private void stopGps(boolean bRelease) {
		if( null != mProvider ) {
			mProvider.stop();
			
			if( bRelease ) {
				mProvider = null;
			}
		}
	}
	
	protected Location getLastKnown() {
		mLastKnown = (null != mProvider ? mProvider.getLastKnown() : null);
		
		return mLastKnown;
	}
	
	// Constants definition for wireless settings.
	protected static final int KWirelessSetting = 0x800;

	protected static final int KAppMessageIdBase = (KUiMessageIdUser + 0x100); // User defined message start offset.
	
	protected static final int KAppMessageIdUser = (KUiMessageIdUser + 0x200);
	
	// Request code.
	private static final int REQUEST_TAKE_PHOTO   = 0x300;
	private static final int REQUEST_SELECT_PHOTO = 0x301;
	private static final int REQUEST_SEL_CONTACT  = 0x302;
	
	private LocationProvider mProvider;
	private Location         mLastKnown;
}
