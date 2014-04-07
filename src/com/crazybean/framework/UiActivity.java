package com.crazybean.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.crazybean.utils.Logger;

public final class UiActivity extends ActionBarActivity {
	/**
	 * onCreate
	 * Callback when the UiActivity instance is activated.
	 */
    @Override
    public void onCreate(Bundle aSavedState)  {
    	// To support ActionBar, we need remove the line.
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
		
        // Create the instance.
        super.onCreate(aSavedState);

        // Default setting: hide actionbar
        getSupportActionBar().hide();
        
        // Initialize the handler instance.
        if( null == mHandler ) {
        	// Create the instance of handler.
            mHandler = new Handler(){
	        	@Override
	        	public void handleMessage(Message aMessage) {
	    			if ( null == aMessage )
	    				return ;
	    			
	    			// Notify message to its owner.
	    			UiActivity.this.handleMessage(aMessage.what, aMessage.obj, aMessage.arg2);
	    		}
            };
        }
                
        // Check the UiManager instance.
        UiManager pManager = UiManager.getInstance();
        if ( null == pManager ) {
        	pManager = UiManager.create(this);
        }
        
        // Save the activity.
        pManager.setActivity(this);
        
        // Initialize the instance.
        initialize();
        
        // Attach the bundle from previous instance.
        if ( null != mCurrent ) {
        	Bundle pPrev = (null != aSavedState ? aSavedState.getBundle(UiActivity.class.getName()) : null);
        	if ( null != pPrev ) {
        		mCurrent.attachBundle(pPrev);
        		pPrev = null;
        	}
        }
        
        // Activate the first page.
        activatePage();
    }
    
    /**
     * onResume
     * Callback when the activity is reactivated.
     */
    @Override
    public void onResume() {
    	// Call the base implementation.
    	super.onResume();
    	
    	// Try to handle the new intent.
    	Intent pIntent = getIntent();
    	handleIntent(pIntent);
    	
    	// Check whether it is restore from background.
    	if ( mStopped ) {
    		if ( null != mCurrent ) {
    			mCurrent.onResume();
    		}
    		
    		// Restore the flag.
    		mStopped = false;
    	}
    	
    	// Clear the intent.
    	setIntent(null);
    	pIntent = null;
    }
    
    /**
     * onStop
     * Callback when UiActivity instance is stopped.
     */
    @Override
    public void onStop() {
    	// Call the base implementation.
    	super.onStop();
    	
    	// Set the flag.
    	mStopped = true;
    	
    	if ( null != mCurrent ) {
    		mCurrent.onSuspend();
    	}
    }
    
    /**
     * onDestroy
     * Callback when UiActivity instance is destroyed.
     */
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	// Clear local caches.
    	doFinalize(false);
    }
    
    @Override
	public void onAttachFragment(Fragment aFragment) {
    	super.onAttachFragment(aFragment);
    	Logger.log("FragmentId : " + aFragment.getId() + ", Class: " + aFragment.getClass().toString());
    	if( null == mFragments ) {
    		mFragments = new ArrayList<Fragment>();
    	}
    	
    	// Save to cache.
    	mFragments.add(aFragment);
    }
    
    
    /**
    * method Name:onNewIntent    
    * method Description:     
    * void  
    * @exception   
    * @since  1.0.0
     */
    @Override
    public void onNewIntent(Intent aIntent) {
    	// Save the new intent, it would handle in onResume.
    	setIntent(aIntent);
    }
    
    /**  
	* method Name:onCreateOptionsMenu    
	* method Description:  
	* @param aMenu
	* @return
	* @exception   
	* @since  1.0.0  
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu aMenu) {
//		if( null != mCurrent ) {
//			final int nResId = R.menu.main_page_menu;
//			if( nResId > 0 ) {
//				// Create the options menu.
//				MenuInflater inflater = getMenuInflater();
//				inflater.inflate(nResId, aMenu);
////				return true;
//
//			}
//		}
		
//		final int nResId = R.menu.main_page_menu;
//		if( nResId > 0 ) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(nResId, aMenu);
//		}
		
		
		if( null != mCurrent ) {
			final int nResId = mCurrent.getOptionsMenuRes();
			if( nResId > 0 ) {
				// Create the options menu.
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(nResId, aMenu);
			}
		}
		
//		this.openOptionsMenu();
		
		return super.onCreateOptionsMenu(aMenu);
    }
	
	/**
	 * onPrepareOptionsMenu
	 */
//	@Override
//	public boolean onPrepareOptionsMenu(Menu aMenu) {
//	    if( null != mCurrent ) {
//	        mCurrent.onPrepareOptionsMenu(aMenu);
//	        return true;
//	    }
//	    
//	    return false;
//	}
	
	/**  
	* method Name: onOptionsItemSelected    
	* method Description:  
	* @param items
	* @return
	* @exception   
	* @since  1.0.0  
	*/
	@Override
	public boolean onOptionsItemSelected(MenuItem aItem) {
		return (null != mCurrent ? mCurrent.onOptionsItemSelected(aItem.getItemId()) : false);
	}
	
	/**  
	* method Name:onKeyDown    
	* method Description:  
	* @param keyCode
	* @param event
	* @return
	* @exception   
	* @since  1.0.0  
	*/
	@Override
	public boolean onKeyDown(int aKeyCode, KeyEvent aEvent) {
		if( mHolding || mHandler.hasMessages(KEventAnim) )
			return true;
		
		// Pass the event to current page.
		final boolean bHandled = onKeyEvent(KeyEvent.ACTION_DOWN, aKeyCode, aEvent);
		
		return bHandled ? bHandled : super.onKeyDown(aKeyCode, aEvent);
	}
	
	/**  
	* method Name:onKeyUp    
	* method Description:  
	* @param keyCode
	* @param event
	* @return
	* @exception   
	* @since  1.0.0  
	*/
	@Override
	public boolean onKeyUp(int aKeyCode, KeyEvent aEvent) {
		if( mHolding || mHandler.hasMessages(KEventAnim) )
			return true;
		
		// Pass the event to current page.
		final boolean bHandled = onKeyEvent(KeyEvent.ACTION_UP, aKeyCode, aEvent);
		
		return bHandled ? bHandled : super.onKeyUp(aKeyCode, aEvent);
	}
	
	/**  
	* method Name:onKeyEvent    
	* method Description:
	* @param aAction  
	* @param aKeyCode
	* @param aEvent
	* @return
	* @exception   
	* @since  1.0.0  
	*/
	private boolean onKeyEvent(int aAction, int aKeyCode, KeyEvent aEvent) {
		// Pass the event to current page.
		boolean bHandled = false;
		if ( null != mCurrent ) {
			switch ( aKeyCode )
			{
			case KeyEvent.KEYCODE_BACK:
				if ( KeyEvent.ACTION_DOWN == aAction )
				{
					// Handle back key event.
					bHandled = mCurrent.handleBack();
				}
				break;
				
			case KeyEvent.KEYCODE_SEARCH:
				if ( KeyEvent.ACTION_DOWN == aAction )
				{
					// Post a event for search.
					bHandled = mCurrent.handleSearch();
				}
				break;
				
			default:
				bHandled = mCurrent.handleKeyEvent(aAction, aKeyCode, aEvent);
				break;
			}
		}
		
		return bHandled;
	}
	
	/**
	* method Name:onTouchEvent    
	* method Description:  
	* @param ev
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	@Override
	public boolean onTouchEvent(MotionEvent aEvent) {
		if( mHolding || mHandler.hasMessages(KEventAnim) )
			return true;
		
		return null != mCurrent ? mCurrent.handleTouchEvent(aEvent) : super.onTouchEvent(aEvent);
	}
    
	/**
	* method Name:dispatchTouchEvent    
	* method Description:  
	* @param ev
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent aEvent) {
		boolean bHandled = null != mCurrent ? mCurrent.detectTouchEvent(aEvent) : false;
		
		if ( !bHandled ) {
			// Do something special here.
			bHandled = super.dispatchTouchEvent(aEvent);
		}
		
		return bHandled;
	}
	
	/**
	 * onSaveInstanceState
	 * Save the state when activity suspend.
	 */
	@Override
	public void onSaveInstanceState(Bundle aSavedState) {
		if ( null != mCurrent ) {
			mCurrent.onSaveState(aSavedState);
		}
		
		super.onSaveInstanceState(aSavedState);
	}
	
	
	/**
	 * onRestoreInstanceState
	 * Restore the state when activity resume.
	 */
	@Override
	public void onRestoreInstanceState(Bundle aSavedState) {
		super.onRestoreInstanceState(aSavedState);
	  
		if ( null != mCurrent ) {
			mCurrent.onRestoreState(aSavedState);
		}
	}
	
	/**
	 * onConfigurationChanged
	 * Call-back when screen configuration updated.
	 */
	@Override
	public void onConfigurationChanged(Configuration aNewConfig) {
		// Call the base implementation first.
        super.onConfigurationChanged(aNewConfig);
        
        // TODO: Notify the configuration event to current/every page.
        if ( null != mCurrent ) {
        	mCurrent.onConfigurationUpdated(aNewConfig);
        }
	}
	
	/**
	* method Name:isHolding    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	boolean isHolding() {
		return mHolding;
	}
	
	/**
	* method Name:handleIntent    
	* method Description:  
	* @param aIntent   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void handleIntent(Intent aIntent) {
		if ( (null == aIntent) || (null == mCurrent) )
			return ;
		
		// Check whether is from history: Launching by long pressing the HOME key.
		if ( (aIntent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) > 0 )
			return ;
	
		// Handle the data.
		mCurrent.handleIntent(aIntent);
	}
	
	/**
	 * startActivity
	 * @param aTarget
	 * @param pBundle
	 * @param nRequestCode
	 * @return
	 */
	boolean startActivity(Class<?> aTarget, Bundle pBundle, int nRequestCode) {
		if( null == aTarget )
			return false;
		
		Intent pIntent = new Intent(this, aTarget);
		if( null != pBundle ) {
			pIntent.putExtras(pBundle);
		}
		
		return startActivity(pIntent, nRequestCode);
	}
	
	/**
	 * startActivity
	 * @param aIntent
	 * @param nRequestCode
	 * @return
	 */
	boolean startActivity(Intent aIntent, int nRequestCode)
	{
		if( 0 >= nRequestCode ) {
			startActivity(aIntent);
		} else {
			startActivityForResult(aIntent, nRequestCode);
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int nRequestCode, int nResultCode, Intent aData) {
		if( null != mCurrent ) {
			mCurrent.onActivityResult(nRequestCode, nResultCode, aData);
		}
		
		super.onActivityResult(nRequestCode, nResultCode, aData);
	}
	
    /////////////////////////////////////////////////////////////////////////////////
    /**
    * method Name:activatePage    
    * method Description:     
    * void  
    * @exception   
    * @since  1.0.0
     */
    private void activatePage() {
    	// Set the animation status.
    	mFlipper.removeAllViews();
    	int nPos = 0;
    	boolean bCheckAnim = false;
    	View pPrev = (null != mPrevious ? mPrevious.mLayout : null);
    	if ( null != pPrev ) {
    		// De-activate the previous page.
    		mPrevious.onDeactivate();
    		mFlipper.addView(pPrev, nPos++);
    		bCheckAnim = (getOutAnim(mIsForward) > 0);
    	}
    	
    	if ( null != mCurrent ) {
    		// Get the clear flag.
    		final boolean bResetFocus = mCurrent.hasResetFlag();
    		
    		// Update current page id.
    		View pView = mCurrent.mLayout;
    		if ( null != pView ) {
    			// Add the view to flipper.
    			mFlipper.addView(pView, nPos);
    			
        		// Check full screen.
        		checkConfig(mCurrent.isFullScreen(), mCurrent.canRotate());
        		
            	if ( bResetFocus ) {
            		// Clear the focus. If the function was not invoked, sometimes the EditText can not 
                	// accept any input after page resumed. We still do now know the exact reason.
            		// However, while using web view, we should not call the clear focus. Or the touching
            		// focus would loss after activating again.
                	pView.clearFocus();
            	}
    		}
    		
    		// Try to do initializing task.
    		mCurrent.doCreate(mFragments);
    		
    		// Restore status.
    		mCurrent.restoreState();
    		
    		// Load the animation.
    		Animation pInAnim = null;
    		Animation pOutAnim = null;
    		if ( bCheckAnim ) {
        		final int nInAnim = getInAnim(mIsForward);
        		final int nOutAnim = getOutAnim(mIsForward);
        		
        		if ( (nInAnim > 0) && (nOutAnim > 0) ) {
        			pInAnim = AnimationUtils.loadAnimation(this, nInAnim);
            		pOutAnim = AnimationUtils.loadAnimation(this, nOutAnim);
        		}
    		}
    		
    		// Check the duration.
    		final int nDuration = (int) Math.max((null != pInAnim ? pInAnim.getDuration() : 0), (null != pOutAnim ? pOutAnim.getDuration() : 0));
    		
    		// Start the animation event.
        	postMessage(KEventAnim, null, nDuration + KPageDelayMs, 0);
        	
    		// Update the animation type.
    		mFlipper.setInAnimation(pInAnim);
    		mFlipper.setOutAnimation(pOutAnim);
    		mFlipper.setDisplayedChild(nPos);
    		
    		// Clean up the animation.
    		pInAnim = null;
    		pOutAnim = null;
    	}
    }
    
    /**
    * method Name:checkConfig  
    * method Description:     
    * void  
    * @exception   
    * @since  1.0.0
     */
    private void checkConfig(boolean aFullScreen, boolean aCanRotate) {
    	// Check the configuration for full screen.
    	switchFullScreen(aFullScreen);
    	
    	// Check the rotate configuration for the screen.
    	final int nOrentation = aCanRotate ? ActivityInfo.SCREEN_ORIENTATION_SENSOR : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    	setRequestedOrientation(nOrentation);
    }
    
    /**
	* method Name:processEvent    
	* method Description:  
	* @param nEventId
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
    private boolean processEvent(int aEventId, final Bundle aBundle) {
		if ( null == mStates )
			return false;
		
		// Check whether is exit event.
		if ( UiConfig.KEventExit == aEventId ) {
			// Try to exit.
			tryExit();
			
			mHolding = false;
			return true;
		}
		
		// Get next state.
		int nNewState = mStates.getNext(aEventId);
		mIsForward = false;
		switch ( nNewState ) {
		case UiConfig.KStatePrev:
			nNewState = mStates.getPrevious();
			break;
			
		case UiConfig.KStateNone:
			// Not found, check whether it is a back event.
			if ( UiConfig.KEventBack != aEventId ) {
				mHolding = false;
				return false;
			}
			
			nNewState = mStates.getPrevious();
			break;
			
		default:
			mIsForward = true;
			break;
		}
		
		// Check status.
		if ( UiConfig.KStateChaos == nNewState ) {
			// Try to exit.
			tryExit();
			mHolding = false;
			return true;
		}
		
		// Update current state.
		Bundle pPrev = null;
		if ( mIsForward ) {
			mStates.moveForward(nNewState, aBundle);
		} else {
			pPrev = mStates.moveBackward(aBundle);
		}
		
		// Save the previous UI page.
		mPrevious = null;
		mPrevious = mCurrent;
		
		// Get view id.
		mCurrent = null;
		mCurrent = getPage(nNewState);
		if ( null != mCurrent ) {
			// Attach the bundle for data.
			mCurrent.attachBundle(mIsForward ? aBundle : pPrev);
			if ( mCurrent != mPrevious ) {	
				// Activate a new page.
				activatePage();
			} else {
				// Reset the holding status.
				mHolding = false;
				
				// Activate the page.
//				invalidateOptionsMenu();
				supportInvalidateOptionsMenu();
				mCurrent.onActivate();
			}

		}
		return true;
	}
    
    /**
     * switchFullScreen
     * @param bFullscreen
     */
    void switchFullScreen(boolean bFullScreen) {
    	Window pWindow = getWindow();
    	if ( null != pWindow ) {
    		if ( bFullScreen ) {
    			pWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    		} else {
    			pWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    		}
    	}
    }
    
    /**
	 * method Name:setForwardAnim
	 * @param nInAnim : Entry animation for current page.
	 * @param nOutAnim : Exit animation for previous page.
	 * @param bPermanent : Effect on every page or not.
	 */
	void setForwardAnim(int nInAnim, int nOutAnim, boolean bPermanent) {
		AnimConfig pConfig = bPermanent ? mPermanent : mTemporary;
		pConfig.mForward.mInAnim = nInAnim;
		pConfig.mForward.mOutAnim = nOutAnim;
	}
	
	/**
	 * method Name:setBackwardAnim
	 * @param nInAnim : Entry animation for previous page.
	 * @param nOutAnim : Exit animation for current page.
	 * @param bPermanent : Effect on every page or not.
	 */
	void setBackwardAnim(int nInAnim, int nOutAnim, boolean bPermanent) {
		AnimConfig pConfig = bPermanent ? mPermanent : mTemporary;
		pConfig.mBackward.mInAnim = nInAnim;
		pConfig.mBackward.mOutAnim = nOutAnim;
	}
	
	/**
	* method Name:ginitPage    
	* method Description:  
	* @return   
	* UiPage  
	* @exception   
	* @since  1.0.0
	 */
    private void initPage() {
    	// Initialize the previous page.
    	mPrevious = null;
    	
    	// Save current state.
    	int nNext = mStates.getCurrent();
    	Bundle pBundle = mStates.getBundle();
    	
		// It means application just starts up, need get next state.
		if ( UiConfig.KStateChaos == nNext ) {
			// 1. Move forward current state machine.
			nNext = mStates.getNext(UiConfig.KEventInit);
			if ( UiConfig.KStateNone == nNext )
				return ;
			
			// Update current page.
			mStates.moveForward(nNext, null);
		}
		
		// Load current page.
		mCurrent = getPage(nNext);
		if ( null != mCurrent ) {
			mCurrent.attachBundle(pBundle);
		}
	}
	
	/**
	* method Name:doExit    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
    private void doExit() {	
    	// Clear all the page cache and global instances.
    	doFinalize(true);
    	
    	// finish the activity.
    	finish();
	}
    
    /**
	 * getInAmin
	 * @param bForward
	 * @return
	 */
	private int getInAnim(boolean bForward) {
		int nInAmin = mTemporary.getInAnim(bForward);
		if( 0 == nInAmin )
			nInAmin = mPermanent.getInAnim(bForward);
		
		return nInAmin;
	}
	
	/**
	 * getOutAmin
	 * @param bForward
	 * @return
	 */
	private int getOutAnim(boolean bForward) {
		int nOutAmin = mTemporary.getOutAnim(bForward);
		if( 0 == nOutAmin )
			nOutAmin = mPermanent.getOutAnim(bForward);
		
		return nOutAmin;
	}
    
    /**
     * doFinalize
    * method Name:clearCache    
    * method Description:     
    * void  
    * @exception   
    * @since  1.0.0
     */
    private void doFinalize(boolean bExit) {
    	if( null != mFragments ) {
    		mFragments.clear();
    		mFragments = null;
    	}
    	
    	// Clear page cache
    	if ( null != mCaches ) {
    		final int nSize = mCaches.size();
    		for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
    			PageCache pCache = mCaches.valueAt(nIdx);
    			if( null != pCache.mPage ) {
    				pCache.mPage.onDestroy();
    				
    				if( pCache.mPage == mCurrent ) {
    					// Current already destroy in the loop.
    					mCurrent = null;
    				}
    			}
    		}
    		
    		// Clear the items.
    		mCaches.clear();
    		mCaches = null;
    	}
    	
    	if ( null != mCurrent ) {
    		mCurrent.onDestroy();
    		// Always set current page to null when exiting.
        	mCurrent = null;
    	}
    	
    	// Clean up the UI Manager instance.
    	UiManager pManager = UiManager.getInstance();
    	if ( null != pManager ) {
    		pManager.reset(bExit);
    	}
    }
	
	/**
	* method Name:tryExit    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void tryExit() {
		// Get current UI page.
		final boolean bExit = null != mCurrent ? mCurrent.onExit() : true;
		if ( bExit ) {
			doExit();
		}
	}
	
	/**
	* method Name:getPage    
	* method Description:  
	* @param nState
	* @return   
	* UiPage  
	* @exception   
	* @since  1.0.0
	 */
	private UiPage getPage(int nState) {
		// 1. Firstly, we need search in cache.
		PageCache pCache = (null != mCaches ? mCaches.get(nState) : null);
		if( (null != pCache) && (null != pCache.mPage) ) {
			// Already found in cache.
			pCache.mDiscard = false;
			return pCache.mPage;
		}
		
		// 2. Create a new instance and save to cache.
		UiPage pPage = null;
		try {
			pPage = (null != mMethod ? (UiPage)mMethod.invoke(null, nState) : null);
		} catch (Exception aException) {
			aException.printStackTrace();
			Logger.log(aException.toString(), Logger.KError);
		}
		
		if( null == pPage ) {
			throw new RuntimeException("getPage(), Corresponding UiPage Instance Not Found, State: " + nState + ", check (UiConfig.KStateUser + " + (nState - UiConfig.KStateUser) + ")");
		} else if ( null != mInflater ) {
			// Save activity.
			pPage.setActivity(this);
    		
			// Attach a view instance.
			View pView = mInflater.inflate(pPage.mLayoutId, null);
			pPage.mLayout = pView;
			
			// Save cache to array.
			PageCache entity = new PageCache(pPage);
			mCaches.put(nState, entity);
		}
		
		return pPage;
	}
	
	/**
	* method Name:getVersionName    
	* method Description:  
	* @return   
	* String  
	* @exception   
	* @since  1.0.0
	 */
	public String getVersionName() {
		return UiManager.getVersionName();
	}
    
    /**
    * method Name:exitApp    
    * method Description:     
    * void  
    * @exception   
    * @since  1.0.0
     */
    void exitApp() {
    	doExit();
    }
    
    /**
     * Force to finish the page.
     * @param aPage
     */
    synchronized void finishPage(UiPage aPage) {
    	final int nSize = (null != mCaches ? mCaches.size() : 0);
    	for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
    		PageCache pCache = mCaches.valueAt(nIdx);
    		if( pCache.mPage == aPage ) {
    			pCache.mFinish = true;
    			break;
    		}
    	}
    }
    
    /**
    * method Name:initialize    
    * method Description:     
    * void  
    * @exception   
    * @since  1.0.0
     */
    private void initialize() {
        // Create an instance for Inflater
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        // Initialize the flipper for pages.
        mFlipper = new UiFlipper(this);
        setContentView(mFlipper);
        
		// Load states.
        mStates = UiManager.getInstance().getStates();
		
		// Create instance for page cache.
        mCaches = new SparseArray<PageCache>();
        mStates.setArray(mCaches);
		
		// Create the default animation support.
		AnimVal pForward = new AnimVal(android.R.anim.fade_in, android.R.anim.fade_out);
		AnimVal pBackward = new AnimVal(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		mPermanent = new AnimConfig(pForward, pBackward);
		mTemporary = new AnimConfig();
		
		// Set the default animating flag to false.
		mHolding = false;
		mWaiting = null;
		mStopped = false;
		mMethod = null;
		mIsForward = true;
		
		try {
			ApplicationInfo pInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			Bundle pBundle = pInfo.metaData;
			if ( null != pBundle )
			{
				String strClass = pBundle.getString("state_table_class_name");
				String strMethod = pBundle.getString("state_table_page_function");
				
				// Get the class and method.
				Class<?> pClass = Class.forName(strClass);
				Class<?> pArgus[] = new Class[1];
				pArgus[0] = int.class;
				mMethod = pClass.getMethod(strMethod, pArgus);
			}
		} catch( Exception aException ) {
			aException.printStackTrace();
			Logger.log(aException.toString(), Logger.KError);
		}
		
		// Load the first page.
		initPage();
    }
    
    /**
	* method Name:showStatesError
	* method Description:  
	* @param nIndex1
	* @param nIndex2   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void showStatesError(final StateMachine.StateError aError) {
		if ( (null == aError) || (StateMachine.StateError.KErrNone == aError.mErrCode) ) {
			// No error reported.
			return ;
		}
		
		// Show the error message.
		AlertDialog.Builder pBuilder = new AlertDialog.Builder(this);
		
		switch ( aError.mErrCode ) {
		case StateMachine.StateError.KErrTableEmpty:
			pBuilder.setTitle("KErrTableEmpty");
			break;
			
		case StateMachine.StateError.KErrAmbiguous:
			pBuilder.setTitle("KErrAmbiguous");
			break;
			
		case StateMachine.StateError.KErrDuplicated:
			pBuilder.setMessage("KErrDuplicated");
			break;
			
		case StateMachine.StateError.KErrNextIsAny:
			pBuilder.setMessage("KErrNextIsAny");
			break;
			
		default:
			break;
		}
		
		pBuilder.setMessage(aError.toString());
		
		// Show the dialog.
		pBuilder.show();
	}
    
    
    //////////////////////////////////////////////////////////////////////////////////////////
    /**
    * method Name:handlePageEvent    
    * method Description:  
    * @param nEventId
    * @return   
    * boolean  
    * @exception   
    * @since  1.0.0
     */
    public boolean handlePageEvent(int aEventId, final Bundle aBundle) {
    	if ( (mHolding) || (null == mHandler) || (mHandler.hasMessages(KEventPage)) )
    		return false;
    	
    	// Set the animation flag.
    	mHolding = true;
    	
    	// Send a message for handling page event.
    	Message pMessage = mHandler.obtainMessage(KEventPage, aBundle);
		pMessage.arg2 = aEventId; // Event id set to argument 2.
		
		// Post the message.
		mHandler.sendMessageDelayed(pMessage, KPageDelayMs);
		
		return true;
    }
    
   /**
   * method Name:postMessage    
   * method Description:  
   * @param aMessageId
   * @param aObject
   * @param aDelayMillis
   * @param aOwnerId
   * @return   
   * boolean  
   * @exception   
   * @since  1.0.0
    */
    public boolean postMessage(int aMessageId, final Object aObject, int aDelayMillis, int aOwnerId) {
    	if ( null == mHandler )
			return false;
		
		// Create a new message.
		Message pMessage = mHandler.obtainMessage(aMessageId, aObject);
		pMessage.arg2 = aOwnerId; // Save the owner id.
		
		boolean bRet = false;
		
		// Check is animating now.
		if ( (mHolding) && (KEventAnim != aMessageId) ) {
			if ( null == mWaiting ) {
				mWaiting = new ArrayList<Message>();
			}
			
			// Save the message in the waiting queue.
			pMessage.arg1 = aDelayMillis; // Save the delay information.
			bRet = mWaiting.add(pMessage);
			pMessage = null;
			
			return bRet;
		}
		
		// Send the new message.
		bRet = postMessage(pMessage, aDelayMillis);
		
		// Clean up.
		pMessage = null;
		
		return bRet;
    }
    
    /**
    * method Name:postMessage    
    * method Description:  
    * @param pMessage
    * @param aDelayMillis   
    * void  
    * @exception   
    * @since  1.0.0
     */
    private boolean postMessage(Message pMessage, int aDelayMillis) {
    	if ( (null == mHandler) || (null == pMessage) )
    		return false;
    	
    	// Send the new message.
		return (0 >= aDelayMillis ? mHandler.sendMessage(pMessage) : mHandler.sendMessageDelayed(pMessage, aDelayMillis));
    }
    
    /**
    * method Name:killMessage    
    * method Description:  
    * @param aMessageId   
    * void  
    * @exception   
    * @since  1.0.0
     */
    public void killMessage(int aMessageId) {
    	// Remove message from queue.
		if ( null == mHandler )
			return ;
		
		// Firstly, whether the message is in the waiting queue.
		final int nSize = (null != mWaiting ? mWaiting.size() : 0);
		for ( int nIdx = nSize - 1; nIdx >= 0; nIdx-- ) {
			Message pMessage = mWaiting.get(nIdx);
			if ( pMessage.what == aMessageId ) {
				mWaiting.remove(nIdx);
			}
			
			// Release the instance.
			pMessage = null;
		}
		
		mHandler.removeMessages(aMessageId);
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
	private void handleMessage(int nMessageId, Object aObject, int nOwnerId) {
		// Get current active page.
		switch ( nMessageId ) {
		case UiConfig.KEventInit:
			// Start the application.
			StateMachine.StateError pError = (StateMachine.StateError)aObject;
			if ( null != pError ) {
				// Show error message for state machine.
				showStatesError(pError);
			}
			break;
			
		case KEventPage:
			// Process the event from pages with bundle.
			Bundle pBundle = (null != aObject ? (Bundle)aObject : null);
	    	processEvent(nOwnerId, pBundle);
			break;
			
		case KEventAnim:
			{
				// Animation done.
				mHolding = false;
				
				// Send the message in the waiting queue.
				if ( null != mWaiting )
				{
					final int nSize = mWaiting.size();
					for ( int nIdx = 0; nIdx < nSize; nIdx++ )
					{
						Message pMessage = mWaiting.get(nIdx);
						if ( null != pMessage )
						{
							// Reset the delay information.
							final int nDelayMillis = pMessage.arg1;
							pMessage.arg1 = 0;
							
							// Send the message.
							postMessage(pMessage, nDelayMillis);
						}
					}
					
					// Clear the waiting message queue.
					mWaiting.clear();
				}
				
				// Clear the temporary animation.
				if( null != mTemporary ) {
					mTemporary.reset();
				}
				
				// Attach the fragment.
				mCurrent.attachFragments(mFragments);
				if( null != mFragments ) {
					mFragments.clear();
				}
				
				// Activate the new page.
	    		mCurrent.onActivate();
	    		
	    		
	    		// Check whether need release the pages in cache.
	    		final int nSize = (null != mCaches ? mCaches.size() : 0);
	    		for( int nIdx = nSize -1; nIdx >=0; nIdx-- ) {
	    			PageCache pCache = mCaches.valueAt(nIdx);
	    			if( ((pCache.mDiscard) && (!pCache.mPage.mCacheable)) || (pCache.mFinish) ) {
						mCaches.remove(mCaches.keyAt(nIdx));
						pCache.mPage.onDestroy();
						
						// Check whether previous page is already destroy.
						if( pCache.mPage == mPrevious ) {
							mPrevious = null;
						}
					} else {
						pCache.mDiscard = false;
					}
	    		}
			}
			break;
			
		default:
			if ( (null == mCurrent) || (nOwnerId != mCurrent.mId) ) {
				return ;
			}
			
			// Pass event to current page.
			mCurrent.handleMessage(nMessageId, aObject);
			break;
		}
	}
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Class Name:PageCache 
	* Class Description: 
	* Modify Date: Apr 8, 2011 11:35:41 AM 
	* Modify Remarks: 
	* @version 1.0.0
	*
	 */
	public final class PageCache {
		/**
		* Create a new Instance PageCache.  
		* @param aPage
		* @param nState
		 */
		public PageCache(UiPage aPage) {
			mPage = aPage;
			mDiscard = false;
			mFinish = false;
		}
		
		UiPage  mPage;    // Page cache.
		boolean mDiscard; // Indicates whether state already discarded
		boolean mFinish;  // Indicates whether flag as force to finish
	}
	
	/**
	 * SafeFlipper
	 * @author lorenchen
	 */
	private final class UiFlipper extends ViewFlipper {
		/**
		 * Constructor of UiFlipper
		 * @param aContext
		 */
		public UiFlipper(Context aContext) {
			super(aContext);
		}
		
		/**
		 * onDetachedFromWindow
		 * There is this error that plagued me for a few months. If you use a ViewFlipper, 
		 * this error will pop up on Android 2.1 devices every once in a while:
		 * java.lang.IllegalArgumentException: Receiver not registered: android.widget.ViewFlipper.
		 */
		@Override
		protected void onDetachedFromWindow() {
		    try {
		        super.onDetachedFromWindow();
		    } catch (IllegalArgumentException aException) {
		    	stopFlipping();
		    	Logger.log(aException.toString());
		    }
		}
	}
	
	/**
	 * @author lorenchen
	 */
	private final class AnimVal
	{
		AnimVal(int nInAnim, int nOutAnim) {
			mInAnim = nInAnim;
			mOutAnim = nOutAnim;
		}
		
		// Configuration.
		int mInAnim;
		int mOutAnim;
	}
	
	/**
	 * @author lorenchen
	 */
	private final class AnimConfig
	{
		AnimConfig(AnimVal aForward, AnimVal aBackward) {
			mForward = aForward;
			mBackward = aBackward;
		}
		
		AnimConfig() {
			mForward = new AnimVal(0, 0);
			mBackward = new AnimVal(0, 0);
		}
		
		int getInAnim(boolean isForward) {
			AnimVal pAnimVal = (isForward ? mForward : mBackward);
			return pAnimVal.mInAnim;
		}
		
		int getOutAnim(boolean isForward) {
			AnimVal pAnimVal = (isForward ? mForward : mBackward);
			return pAnimVal.mOutAnim;
		}
		
		void reset() {
			mForward.mInAnim = mForward.mOutAnim = 0;
			mBackward.mInAnim = mBackward.mOutAnim = 0;
		}
		
		// Forward and backward,
		AnimVal mForward;
		AnimVal mBackward;
	}
	
	// Member instances
	private UiPage              mPrevious;   // Previous page.
	private UiPage              mCurrent;    // Current page.
	private LayoutInflater      mInflater;   // Inflater.
	private StateMachine        mStates;     // State machine instance.
	private UiFlipper           mFlipper;    // View flipper for animation.
	private Handler             mHandler;    // Handler instance to handle message/event.
	private boolean             mHolding;    // Indicates whether is holding for view switching.
	private boolean             mStopped;    // Indicates whether the activity is stopped.
	private Method              mMethod;
	private boolean             mIsForward;  // Indicates whether page is forward or not.
	private AnimConfig          mPermanent;  // Permanent animation configuration
	private AnimConfig          mTemporary;  // Temporary animation configuration.
	private List<Message>       mWaiting;    // Message queue for waiting.
	private List<Fragment>      mFragments;  // Fragments cache.
	private SparseArray<PageCache> mCaches;  // Page caches.pView
	
	// Reserved message definition.
	private static final int KPageDelayMs = 50;
	private static final int KEventInner  = (UiConfig.KEventNone + 0x10);
	private static final int KEventAnim   = (KEventInner + 1);  // Reserved event, for indicating animation.
	private static final int KEventPage   = (KEventInner + 2);  // Reserved event, handling page event.
}