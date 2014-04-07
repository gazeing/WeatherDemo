package com.crazybean.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class UiPage implements View.OnClickListener, OnCancelListener {
	/**
	* Create a new Instance UiPage.  
	 */
	protected UiPage(int nLayoutId) {
		initialize(nLayoutId, true, false);
	}
	
	/**
	 * 
	* Create a new Instance UiPage.  
	*  
	* @param nLayoutId
	* @param bAnimating
	 */
	protected UiPage(int nLayoutId, boolean bAnimating) {
		initialize(nLayoutId, bAnimating, false);
	}
	
	/**
	* Create a new Instance UiPage.  
	*  
	* @param nLayoutId
	* @param bAnimating
	* @param bFullScreen
	 */
	protected UiPage(int nLayoutId, boolean bAnimating, boolean bFullScreen) {
		initialize(nLayoutId, bAnimating, bFullScreen);
	}
	
	/**
	 * startActivity
	 * @param aTarget
	 * @param pBundle
	 * @return
	 */
	protected final boolean startActivity(Class<?> aTarget, Bundle pBundle) {
		return startActivity(aTarget, pBundle, 0);
	}
	
	/**
	 * startActivity
	 * @param aTarget
	 * @param pBundle
	 * @param nRequestCode
	 * @return
	 */
	protected final boolean startActivity(Class<?> aTarget, Bundle pBundle, int nRequestCode) {
		return null != mActivity ? mActivity.startActivity(aTarget, pBundle, nRequestCode) : false;
	}
	
	/**
	 * startActivity
	 * @param aIntent
	 * @return
	 */
	protected final boolean startActivity(Intent aIntent) {
		return startActivity(aIntent, 0);
	}
	
	/**
	 * Start a new activity
	 * @param aIntent
	 * @param nRequestCode
	 * @return
	 */
	protected final boolean startActivity(Intent aIntent, int nRequestCode) {
		return (null != mActivity ? mActivity.startActivity(aIntent, nRequestCode) : false);
	}
	
	/**
	 * onActivityResult
	 * @param nRequestCode
	 * @param nResultCode
	 * @param aData
	 */
	protected void onActivityResult(int nRequestCode, int nResultCode, Intent aData) {
		// Inherited class should override the class.
	}
	
	/**
	* method Name:setActivity    
	* method Description:  
	* @param pActivity   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void setActivity(final UiActivity pActivity) {
		mActivity = pActivity;
	}
	
	/**
	 * method Name:setForwardAnimIds
	 * @param nInAnim : Entry animation for current page.
	 * @param nOutAnim : Exit animation for previous page.
	 */
	protected void setForwardAnimIds(int nInAnim, int nOutAnim) {
		this.setForwardAnimIds(nInAnim, nOutAnim, false);
	}
	
	/**
	 * method Name:setForwardAnimIds
	 * @param nInAnim : Entry animation for current page.
	 * @param nOutAnim : Exit animation for previous page.
	 * @param bPermanent : Effect on every page or not.
	 */
	protected void setForwardAnimIds(int nInAnim, int nOutAnim, boolean bPermanent) {
		if( null != mActivity ) {
			mActivity.setForwardAnim(nInAnim, nOutAnim, bPermanent);
		}
	}
	
	/**
	 * method Name:setBackwardAnimIds
	 * @param nInAnim : Entry animation for current page.
	 * @param nOutAnim : Exit animation for previous page.
	 * @param bPermanent : Effect on every page or not.
	 */
	protected void setBackwardAnimIds(int nInAnim, int nOutAnim) {
		this.setBackwardAnimIds(nInAnim, nOutAnim, false);
	}
	
	/**
	 * method Name:setBackwardAnimIds
	 * @param nInAnim : Entry animation for current page.
	 * @param nOutAnim : Exit animation for previous page.
	 * @param bPermanent : Effect on every page or not.
	 */
	protected void setBackwardAnimIds(int nInAnim, int nOutAnim, boolean bPermanent) {
		if( null != mActivity ) {
			mActivity.setBackwardAnim(nInAnim, nOutAnim, bPermanent);
		}
	}
	
	/**
	* method Name:attachBundle    
	* method Description:  
	* @param aBundle   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void attachBundle(final Bundle aBundle) {
		mBundle = null;
		mBundle = aBundle;
	}
	
	/**
	 * Method Name: restoreState
	 */
	void restoreState() {
		// Restore the state.
		loadState(mBundle);
	}
	
	/**
	* method Name:animating    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean animating() {
		return mAnimating;
	}
	
	/**
	* method Name:isFullScreen    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean isFullScreen() {
		return mFullScreen;
	}
	
	/**
	* method Name:hasResetFlag    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean hasResetFlag() {
		return mResetFocus;
	}
	
	/**
	* method Name:setCanRotate    
	* method Description:  
	* @param bCanRotate   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void setCanRotate(boolean bCanRotate) {
		mCanRotate = bCanRotate;
	}
	
	/**
	* method Name:canRotate    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean canRotate() {
		return mCanRotate;
	}
	
	/**
	 * method Name:setCacheable
	 * @param bCacheable
	 */
	protected void setCacheable(boolean bCacheable) {
		mCacheable = bCacheable;
	}
	
	/**
	* method Name:setResetFocus    
	* method Description:  
	* @param bResetFocus   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void setResetFocus(boolean bResetFocus)
	{
		mResetFocus = bResetFocus;
	}
	
	/**
	* method Name:findViewById    
	* method Description:  
	* @param nResId
	* @return   
	* View  
	* @exception   
	* @since  1.0.0
	 */
	protected View findViewById(int nResId)
	{	
		return (null != mActivity ? mActivity.findViewById(nResId) : null);
	}
	
	
	/**
	 * setOptionsMenuRes
	 * @param nOptsMenuRes
	 * @param bUpdateNow
	 */
	protected void setOptionsMenuRes(int nOptsMenuRes, boolean bUpdateNow) {
	    mOptsMenuRes = nOptsMenuRes;
	    if( bUpdateNow ) {
	        mActivity.supportInvalidateOptionsMenu();
	    }
	}
	
	/**
	 * @return
	 */
	final int getOptionsMenuRes() {
        return mOptsMenuRes;
    }
	
	/**
	 * onPrepareOptionsMenu
	 * @param aMenu
	 * @return
	 */
	public boolean onPrepareOptionsMenu(Menu aMenu) {
	    return false;
	}
	
	/**
	* method Name: onOptionsItemSelected    
	* method Description:  
	* @param nItemId indicates the menu Id specified in onPrepareMenu
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean onOptionsItemSelected(int nItemId) {
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	* method Name:saveState    
	* method Description: Call-back when the page is replaced by another page.
	* @param aBundle   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void saveState() {
		// Save your state to current bundle.
	}
	
	/**
	* method Name:loadState 
	* method Description: Call-back when the page is activated to the top.
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void loadState(final Bundle aBundle) {
		// Add the implementation here.
	}
	
	/**
	* method Name:onSuspend    
	* method Description:     
	* void  
	* @exception indicates the page is suspend to background.
	* @since  1.0.0
	 */
	protected void onSuspend() {
		// De-activate current dialog instance when page suspend.
		if ( null != mDialog ) {
			if ( mDialog.isShowing() ) {
				mDialog.dismiss();
			}

			mDialog = null;
		}
		
		// Hide the progress bar is exists.
		if ( null != mProgress )
		{
			if ( mProgress.isShowing() )
			{
				mProgress.dismiss();
			}
			mProgress = null;
		}
	}
	
	/**
	* method Name:onResume    
	* method Description:     
	* void  
	* @exception indicates the page is restored to foreground.
	* @since  1.0.0
	 */
	protected void onResume()
	{
		// Add the implementation for inherited classes.
	}
	
	/**
	* method Name:onDestroy    
	* method Description: The callback for page destroying.
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onDestroy() {
		if( null != mOnClicks ) {
			mOnClicks.clear();
			mOnClicks = null;
		}
		
		// Hide progress dialog if any.
		hideProgress();
		
		// Destroy all fragments.
		destroyFragments();
		
		// Clean up the reference to view.
		mLayout = null;
		mDialog = null;
		mCreated = false;
		
		//Logger.log(getClass().getSimpleName() + ".onDestroy()");
	}
	
	/**
	 * finish the instance.
	 */
	protected final void finish() {
		if( null != mActivity ) {
			mActivity.finishPage(this);
		}
	}
	
	/**
	* method Name:onSaveState    
	* method Description: Call-back when application is suspend to background.
	* @param aBundle   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onSaveState(Bundle aBundle) {
		// Save the bundle to system bundle for restoring.
		if ( (null != mBundle) && (null != aBundle) ) {
			aBundle.putBundle(UiActivity.class.getName(), mBundle);
		}
	}
	
	/**
	* method Name:onResume    
	* method Description:  Call-back when the application is resumed to foreground. 
	* @param aBundle   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onRestoreState(Bundle aBundle) {
		// Add the implementation in inherited classes.
	}
	
	/**
	* method Name:onConfigurationUpdated    
	* method Description:  
	* @param aConfiguration   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onConfigurationUpdated(Configuration aConfiguration) {
		// Inherited classes should override the function.
	}
	
	/**
	 * method Name:switchFullScreen
	 * method Description: Change the full screen status.    
	 * @param bFullScreen
	 */
	protected void switchFullScreen(boolean bFullScreen) {
		if ( null != mActivity )
		{
			mActivity.switchFullScreen(bFullScreen);
		}
	}
	
	/**
	* method Name:onViewClick    
	* method Description:  
	* @param aView   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onViewClick(final View aView, int nViewId) {
		// Inherited class should override this function.
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	* method Name:postMessage    
	* method Description:  
	* @param aMessageId
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected final void postMessage(int aMessageId) {
		postMessage(aMessageId, null, 0);
	}
	
	/**
	* method Name:postMessage    
	* method Description:  
	* @param aMessageId
	* @param aObject   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected final void postMessage(int aMessageId, final Object aObject)
	{
		postMessage(aMessageId, aObject, 0);
	}
	
	/**
	* method Name:postMessage    
	* method Description:  
	* @param aMessageId
	* @param aObject
	* @param aDelayMs   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected final void postMessage(int aMessageId, final Object aObject, int aDelayMillis) {
		if ( null == mActivity )
			return ;
		
		// Handle message.
		mActivity.postMessage(aMessageId, aObject, aDelayMillis, mId);
	}
	
	/**
	* method Name:killMessage    
	* method Description:  
	* @param aMessageId   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected final void killMessage(int aMessageId) {
		if ( null == mActivity )
			return ;
		
		// kill message specified by message id.
		mActivity.killMessage(aMessageId);
	}
	
	/**
	* method Name:handleMessage    
	* method Description:  
	* @param nMessageId
	* @param aObject   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void handleMessage(int nMessageId, Object aObject) {
		// Inherited class should override this implementation.
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	* method Name:handleBack    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean handleBack() {
		if ( null == mActivity )
			return false;
		
		// Handle the event for back.
		return mActivity.handlePageEvent(UiConfig.KEventBack, null);
	}
	
	
	/**
	* method Name:handleSearch    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean handleSearch() {
		if ( null == mActivity )
			return false;
		
		// Handle the event for system search.
		return mActivity.handlePageEvent(UiConfig.KEventSearch, null);
	}
	
	/**
	* method Name:handleData    
	* method Description: Process the data from third-party application.
	* @param aIntent
	* @param aParam
	* @param aBundle   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void handleIntent(Intent aIntent) {
		// Inherited class can override the function.
	}
	
	/**
	* method Name:handleKeyEvent    
	* method Description:
	* @param aAction: ACTION_UP/ACTION_DOWN  
	* @param aKeyCode
	* @param aEvent
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean handleKeyEvent(int aAction, int aKeyCode, KeyEvent aEvent) {
		return false;
	}
	
	/**
	* method Name:handleTouchEvent    
	* method Description:  
	* @param aEvent
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean handleTouchEvent(MotionEvent aEvent) {
		return false;
	}
	
	/**
	* method Name:detectTouchEvent
	* method Description:  
	* @param aEvent
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean detectTouchEvent(MotionEvent aEvent) {
		// Inherited class may be interested in this event.
		return false;
	}
	
	/**
	* method Name:onButtonClick    
	* method Description:  
	* @param nButtonId   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onButtonClick(int nButtonId) {
		// Inherited class should override the function.
	}
	
	/**
	* method Name:onCancel    
	* method Description:  
	* @param nCancelId   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onCancel() {
		// Inherited class should override the function.
	}
	
	/**
	* method Name:getParams    
	* method Description:  
	* @param aEventId
	* @return   
	* Bundle  
	* @exception   
	* @since  1.0.0
	 */
	protected Bundle getEventParams(int aEventId) {
		// Get parameters for a event, inherited class should override the function.
		return null;
	}
	
	/**
	* method Name:exitApp    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void exitApp() {
		if ( null == mActivity )
			return ;
		
		// Exit application.
		mActivity.exitApp();
	}
	
	/**
	 * hideApp
	 * Move the application to background.
	 */
	protected boolean hideApp() {
		if ( null == mActivity )
			return false;
		
		// Move the application to background.
		return mActivity.moveTaskToBack(true);
	}
	
	/**
	* method Name:getContext    
	* method Description:  
	* @return   
	* Context  
	* @exception   
	* @since  1.0.0
	 */
	protected synchronized final Context getContext() {
		return mActivity;
	}
	
	/**
	 * getFragmentManager
	 * @return
	 */
	protected synchronized final FragmentManager getFragmentManager() {
		return mActivity.getSupportFragmentManager();
	}
	
	protected synchronized final ActionBar getActionBar() {
		return mActivity.getSupportActionBar();
	}
	
	/**
	 * runOnUiThread
	 * @param aRunnable
	 */
	protected final void runOnUiThread(Runnable aRunnable) {
		mActivity.runOnUiThread(aRunnable);
	}
	
	/**
	* method Name:getVersionName    
	* method Description:  
	* @return   
	* String  
	* @exception   
	* @since  1.0.0
	 */
	protected String getVersionName() {
		return (null != mActivity ? mActivity.getVersionName() : null);
	}
	
	/**
	* method Name:getMetrics    
	* method Description:  
	* @param pMetrics   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void getMetrics(DisplayMetrics pMetrics) {
		if ( (null == pMetrics) || (null == mActivity) ) {
			return ;
		}
		
		// Get default display.
		Display pDisplay = mActivity.getWindowManager().getDefaultDisplay();
		if ( null != pDisplay ) {
			pDisplay.getMetrics(pMetrics);
			pDisplay = null;
		}
		
	}
	
	/**
	* method Name:makeToast    
	* method Description:  
	* @param nResId   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void makeToast(int nResId) {
		if ( null == mActivity )
			return ;
		
		// Show toast text.
		Toast.makeText(mActivity, nResId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	* method Name:makeToast    
	* method Description:  
	* @param aHintStr   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void makeToast(final String strText) {
		if ( (null == mActivity) || (null == strText) )
			return ;
		
		// Show toast text.
		Toast.makeText(mActivity, strText, Toast.LENGTH_SHORT).show();
	}
	
	
	/**
	* method Name:makeDialog    
	* method Description:  
	* @param nTitleId
	* @param nBodyId
	* @param nButtonId   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected Dialog makeDialog(int nTitleId, int nBodyId, final int nButtonId, int nButtonResId) {
		return makeDialog(nTitleId, nBodyId, nButtonId, nButtonResId, 0, 0);
	}
	
	/**
	* method Name:showDialog    
	* method Description:  
	* @param nTitleId
	* @param nBodyId
	* @param nPositiveId
	* @param nNegativeId   
	* void  
	* @exception   
	* @since  1.0.0
	* @return Dialog instance
	 */
	protected Dialog makeDialog(int nTitleId, int nBodyId, final int nPositiveId, int nPositiveResId, final int nNegativeId, int nNegativeResId) {
		String strTitle = getString(nTitleId);
		String strMsg = getString(nBodyId);
		return makeDialog(strTitle, strMsg, nPositiveId, nPositiveResId, nNegativeId, nNegativeResId);
	}
	
	/**
	 * makeDialog
	 * @param strTitle
	 * @param strMsg
	 * @param nPositiveId
	 * @param strPositive
	 * @param nNegativeId
	 * @param strNegative
	 * @return Dialog instance
	 */
	protected Dialog makeDialog(String strTitle, String strMsg, final int nPositiveId, int nPositiveResId, final int nNegativeId, int nNegativeResId) {
		String strPositive = getString(nPositiveResId);
		String strNegative = getString(nNegativeResId);
		return makeDialog(strTitle, strMsg, nPositiveId, strPositive, nNegativeId, strNegative);
	}
	
	/**
	 * makeDialog
	 * @param strTitle
	 * @param strMsg
	 * @param nPositiveId
	 * @param strPositive
	 * @param nNegativeId
	 * @param strNegative
	 * @return Dialog instance
	 */
	protected Dialog makeDialog(String strTitle, String strMsg, final int nPositiveId, String strPositive, final int nNegativeId, String strNegative) {
		if ( null == mActivity )
			return null;
		
		// Create a new instance of builder.
		AlertDialog.Builder pBuilder = new AlertDialog.Builder(mActivity);
		
		// Set dialog title.
		pBuilder.setTitle(strTitle);
		
		// Set body message.
		pBuilder.setMessage(strMsg);
		
		// Set Positive button.
		if ( (nPositiveId > 0) && (!TextUtils.isEmpty(strPositive)) ) {
			pBuilder.setPositiveButton(strPositive, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface aDialog, int nWhich) {
					// TODO Auto-generated method stub
					UiPage.this.onButtonClick(nPositiveId);
				}
			});
		}
		
		// Set Negative button if required.
		if ( (nNegativeId > 0) && (!TextUtils.isEmpty(strNegative)) ) {
			pBuilder.setNegativeButton(strNegative, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface aDialog, int nWhich) {
					// TODO Auto-generated method stub
					UiPage.this.onButtonClick(nNegativeId);
				}
			});
		}
		
		// Show the dialog.
		AlertDialog pDialog = pBuilder.create();
		pDialog.show();
		
		// Clean up.
		pBuilder = null;
		
		// Save the new dialog instance.
		setDialog(pDialog);
		
		return pDialog;
	}
	
	/**
	* method Name:showProgress    
	* method Description:  
	* @param nMsgId   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void showProgress(int nMsgId) {
		showProgress(nMsgId, true);
	}
	
	/**
	 * showProgress
	 * @param strMsg
	 */
	protected void showProgress(String strMsg) {
		showProgress(strMsg, true);
	}
	
	/**
	* method Name:showProgress    
	* method Description:  
	* @param nMsgId
	* @param bCancelable   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void showProgress(int nMsgId, boolean bCancelable) {
		// Start a new progress bar instance.
		String strMsg = nMsgId > 0 ? this.getString(nMsgId) : "";
		showProgress(strMsg, bCancelable);
	}
	
	/**
	* method Name:showProgress    
	* method Description:  
	* @param strMsg
	* @param bCancelable   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void showProgress(String strMsg, boolean bCancelable) {
		if ( null != mProgress ) {
			mProgress.cancel();
			mProgress = null;
		}
		
		// Start a new progress bar instance.
		mProgress = ProgressDialog.show(getContext(), "", strMsg, true);
		mProgress.setCancelable(bCancelable);
		
		// Set on Cancel listener.
		mProgress.setOnCancelListener((OnCancelListener) this);
	}
	
	/**
	* method Name:hideProgress    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void hideProgress()
	{
		if ( null != mProgress ){
			if ( mProgress.isShowing() ) {
				mProgress.cancel();
			}
			
			// Clean up.
			mProgress = null;
		}
	}
	
	/**
	* method Name:setDialog    
	* method Description:  
	* @param pDialog   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void setDialog(Dialog pDialog) {
		mDialog = null;
		mDialog = pDialog;
	}
	
	/**
	 * 
	* method Name:showInputMethod. Show keyboard    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean showInputMethod() {
		InputMethodManager pImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		pImm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
		return true;
	}
	
	/**
	 * 
	* method Name:hideInputMethod. Hide Keyboard    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean hideInputMethod() {
		InputMethodManager pImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		View pFocus = (null != mActivity ? mActivity.getCurrentFocus() : null);
		final IBinder pBinder = (null != pFocus ? pFocus.getWindowToken() : null);
		pFocus = null;
		
		// Hide the input method.
		return pImm.hideSoftInputFromWindow(pBinder, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * onCancel
	 * From OnCancelListener
	 */
	@Override
	public void onCancel(DialogInterface pDialog) {
		if ( null != pDialog ) {
			pDialog.dismiss();
			
			// Call the cancel callback.
			onCancel();
		}
		
	}
	
	/**
	* method Name:getResources    
	* method Description:  
	* @return   
	* Resources  
	* @exception   
	* @since  1.0.0
	 */
	protected Resources getResources() {
		if ( null == mActivity )
			return null;
		
		return mActivity.getResources();
	}
	
	/**
	* method Name:getSystemService    
	* method Description:  
	* @param strName
	* @return   
	* Object  
	* @exception   
	* @since  1.0.0
	 */
	protected Object getSystemService(String strName) {
		if ( null == mActivity )
			return null;
		
		return mActivity.getSystemService(strName);
	}
	
	/**
	* method Name:getString    
	* method Description:  
	* @param nResId
	* @return   
	* String  
	* @exception   
	* @since  1.0.0
	 */
	protected final String getString(int nResId) {
		if ( null == mActivity )
			return "";
		
		return mActivity.getString(nResId);
	}
	
	/**
	 * 
	* method Name:getString    
	* method Description:  
	* @param nResId
	* @param aFormatArgs
	* @return   
	* String  
	* @exception   
	* @since  1.0.0
	 */
	protected final String getString (int nResId, Object... aFormatArgs) {
		if ( null == mActivity )
			return "";
		
		return mActivity.getString(nResId, aFormatArgs);
	}

	/**
	* method Name:postEvent    
	* method Description:  
	* @param nEventId
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean postEvent(int nEventId) {
		return postEvent(nEventId, null);
	}
	
	/**
	* method Name:postEvent    
	* method Description:  
	* @param nEventId
	* @param pBundle
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean postEvent(int aEventId, final Bundle aBundle) {
		if ( null == mActivity )
			return false;
		
		return mActivity.handlePageEvent(aEventId, aBundle);
	}
	
	/**
	* method Name:addOnClick    
	* method Description:  
	* @param aView   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean addOnClick(final View aView) {
		if ( null == aView )
			return false;
		
		// Create a new instance for events.
		if ( null == mOnClicks ) {
			mOnClicks = new Vector<View>();
		}
		
		final int nCount = mOnClicks.size();
		for ( int nIdx = 0; nIdx < nCount; nIdx++ ) {
			final View pView = mOnClicks.get(nIdx);
			if ( pView == aView ) {
				// Already exists.
				return false;
			}
		}
		
		// Save to array.
		aView.setOnClickListener(this);
		mOnClicks.add(aView);
		
		return true;
	}
	
	/*  
	 * Description:
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View aView) {
		// Check current page is animating now.
		if ( (null == mActivity) || (mActivity.isHolding()) ) {
			// If current page was animating now, we should not accept any actions.
			return ;
		}
		
		// Check the onClick event.
		final int nCount = (null != mOnClicks ? mOnClicks.size() : 0);
		for ( int nIdx = 0; nIdx < nCount; nIdx++ ) {
			final View pView = mOnClicks.get(nIdx);
			if ( (null != pView) && (pView == aView) )
			{
				onViewClick(aView, aView.getId());
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	* method Name:saveInt
	* method Description:  
	* @param aKey
	* @param aValue   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void saveInt(final String aKey, int aValue) {
		if ( null == mBundle )
			return ;
		
		mBundle.putInt(aKey, aValue);
	}
	
	/**
	* method Name:saveString    
	* method Description:  
	* @param aKey
	* @param aString   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void saveString(final String aKey, final String aString) {
		if ( null == aString )
			return ;
		
		mBundle.putString(aKey, aString);
	}
	
	/**
	* method Name:getInt    
	* method Description:  
	* @param aKey
	* @return   
	* int  
	* @exception   
	* @since  1.0.0
	 */
	protected int getInt(final String aKey) {
		return (null != mBundle ? mBundle.getInt(aKey) : 0);
	}
	
	/**
	* method Name:getString    
	* method Description:  
	* @param aKey
	* @return   
	* String  
	* @exception   
	* @since  1.0.0
	 */
	protected final String getString(final String aKey) {
		return (null != mBundle ? mBundle.getString(aKey) : null);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	* method Name:onCreate    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onCreate() {
		// inherited class need override the implementation.
	}
	
	/**
	* method Name:onActivate    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onActivate() {
		// Activate fragments.
		activateFragments();
	}
	
	
	/**
	* method Name:onDeactivate    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	protected void onDeactivate() {
		// Save status.
		saveState();
		
		// Deactivate fragments.
		deactivateFragments();
	}
	
	/**
	* method Name:doCreate    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void doCreate(List<Fragment> aFragment) {
		if ( mCreated )
		{
			// Already created.
			return ;
		}
		
		// Create the page.
		onCreate();
		mCreated = true;
	}
	
	/**
	 * attach fragment
	 * @param aFragment
	 */
	void attachFragments(List<Fragment> aFragment) {
		if( (null != aFragment) && (aFragment.size() > 0) ) {
			if( null == mFragments ) {
				mFragments = new ArrayList<Fragment>();
			}
			
			// Copy the fragment instance.
			mFragments.clear();
			mFragments.addAll(aFragment);
		}		
	}
	
	
	/**
	 * activateFragments
	 */
	private void activateFragments() {
		final int nSize = (null != mFragments ? mFragments.size() : 0);
		for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
			Fragment fragment = mFragments.get(nIdx);
			fragment.onResume();
		}
	}
	
	/**
	 * deactivateFragments
	 */
	private void deactivateFragments() {
		final int nSize = (null != mFragments ? mFragments.size() : 0);
		for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
			Fragment fragment = mFragments.get(nIdx);
			fragment.onPause();
		}
	}
	
	/**
	 * destroy fragment.
	 */
	private void destroyFragments() {
		final int nSize = (null != mFragments ? mFragments.size() : 0);
		if( nSize > 0 ) {
			FragmentManager pManager = this.getFragmentManager();
			for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
				Fragment fragment = mFragments.get(nIdx);
				pManager.beginTransaction().remove(fragment).commit();
			}
			
			mFragments.clear();
			mFragments = null;
		}
	}
	
	/**
	* method Name:preExit    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	protected boolean onExit() {
		// Return false, mean disagree to exit.
		return true;
	}
	
	/**
	* method Name:initialize    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void initialize(int nLayoutId, boolean bAnimating, boolean bFullScreen) {
		mActivity = null;
		mLayoutId = nLayoutId;
		mBundle = null;
		mOnClicks = null;
		mCreated = false;
		mProgress = null;
		mAnimating = bAnimating;
		mResetFocus = false;
		mFullScreen = bFullScreen;
		mCanRotate = true;
		mCacheable = true;
		mDialog = null;
		
		// Save current page id.
		mId = (int)(System.currentTimeMillis());
	}
	
	// private member instances.
	private UiActivity        mActivity;
	private boolean           mCreated;
	private Bundle            mBundle;     // Status for restore.
	private Vector<View>      mOnClicks;   // On click event view instances.
	private ProgressDialog    mProgress;   // The progress dialog instance.
	private boolean           mAnimating;  // Indicates whether need animation.
	private boolean           mResetFocus; // Indicates whether to clear focus when activated.
	private boolean           mFullScreen; // Indicates whether to layout in full screen mode.
	private boolean           mCanRotate;  // Indicates whether the page can rotate or not.
	private Dialog            mDialog;     // Save the current dialog instance.
	private List<Fragment>    mFragments;  // Attached fragments list
	private int               mOptsMenuRes;
	
	// Package visible member instances.
	int               mId;         // The unify id of the page.
	int               mLayoutId;
	View              mLayout;     // Layout content view.
	boolean           mCacheable;  // Indicates whether save the page to cache.
	
	// Reserved message id definition.
	protected static final int KUiMessageIdBase  = 0x100; 
	protected static final int KShowInputMethod  = (KUiMessageIdBase + 1); // Reserved message for show input method.
	
	protected static final int KUiMessageIdUser  = 0x200; // User defined message start offset.
}
