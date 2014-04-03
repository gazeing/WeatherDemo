package com.example.weatherdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlideViewGroup extends ViewGroup {
	// Status definition.
	public static final int STATUS_SHOW_MAIN  = 0;
	public static final int STATUS_SHOW_LEFT  = (STATUS_SHOW_MAIN + 1);
	public static final int STATUS_SHOW_RIGHT = (STATUS_SHOW_MAIN + 2);
		
	/**
	 * Constructor while create a new instance directly in code
	 * @param context
	 */
	public SlideViewGroup(Context context) {
		super(context);
		doInit(context);
	}
	
	/**
	 * Constructor while created from XML file
	 * @param context
	 * @param attrs
	 */
	public SlideViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		doInit(context);
	}
	
	public View setLeftView(int nLayoutId) {
		View pView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(nLayoutId, null);
		setLeftView(pView);
		return pView;
	}
	
	public void setLeftView(View aView) {
		if( null == aView ) 
			return ;
		
		mLeftView = aView;
		addView(aView, 0);
		
		mCanLeftScroll = true;
	}
	
	public View setRightView(int nLayoutId) {
		View pView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(nLayoutId, null);
		setRightView(pView);
		return pView;
	}
	
	public void setRightView(View aView) {
		if( null == aView )
			return ;
		
		mRightView = aView;
		addView(aView, 0);
		
		mCanRightScroll = true;
	}
	
	public View setMainView(int nLayoutId) {
		View pView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(nLayoutId, null);
		setMainView(pView);
		return pView;
	}
	
	public void setMainView(View aView) {
		if( null == aView )
			return ;
		
		mMainView = aView;
		addView(aView);
	}
	
	public boolean slideView(boolean bShowLeft) {
		if( (STATUS_SHOW_LEFT == mStatus) || (STATUS_SHOW_RIGHT == mStatus) ) {
			// Restore to main page.
			return showMainView();
		} else {
			return bShowLeft ? showLeftView() : showRightView();
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if( null == mMainView )
			return false;
		
		final int action = ev.getAction() & MotionEvent.ACTION_MASK;
		final int x = (int) ev.getX();
		
		switch(action) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionX = x;
				
	        	if(mStatus == STATUS_SHOW_MAIN) {
					if(mTouchMode== TOUCH_MODE_RESET && (x < mTouchWidht || x > (getWidth()-mTouchWidht))){
						mCanScroll = true;
					}
				}
				else if(mStatus == STATUS_SHOW_LEFT){
					int leftWidth = 0;
					
					if(mLeftView != null) {
						leftWidth = mMainView.getLeft();
					}
					if(x > leftWidth) {
						mCanScroll = true;
					}
				}
				else if(mStatus == STATUS_SHOW_RIGHT) {
					int rightWidth = 0;
					if(mRightView != null) {
						rightWidth = mMainView.getRight();
					}
					
					if(x < rightWidth){
						mCanScroll = true;
					}
				}
				
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	if(!mCanScroll && !mCanLeftScroll && !mCanRightScroll) return false;
	        	final int deltaX = (int) (x - mLastMotionX);
	        	if(mTouchMode != TOUCH_MODE_SCROLLING) {
	        		if(Math.abs(deltaX) > mTouchSlop) {
	        			if(mCanScroll || (deltaX > 0 && mCanLeftScroll) || (deltaX < 0 && mCanRightScroll)) {
		        			mTouchMode = TOUCH_MODE_SCROLLING;
		        			mLastMotionX = x;
		        			return true;
	        			}
	        		}
	        	}
	        	break;
	        case MotionEvent.ACTION_CANCEL:
	        case MotionEvent.ACTION_UP:
	        	mCanScroll = false;
	        	break;
		}
		return false;
	}
	
	public int getStatus() {
		return mStatus;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if( null == mMainView )
			return false;
		
		final int action = ev.getAction() & MotionEvent.ACTION_MASK;

		final int x = (int) ev.getX();
		
		addMovement(ev);
        
        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	        	mLastMotionX = x;
				
				if(mStatus == STATUS_SHOW_MAIN) {
					if(mTouchMode == TOUCH_MODE_RESET) {
						if(mLastMotionX > mTouchWidht && mLastMotionX < (getWidth()-mTouchWidht)) return false;
					}
				}
				else if(mStatus == STATUS_SHOW_LEFT){
					int leftWidth = 0;
					
					if(mLeftView != null) {
						leftWidth = mMainView.getLeft();
					}
					if(mLastMotionX < leftWidth) return false;
				}
				else if(mStatus == STATUS_SHOW_RIGHT) {
					int rightWidth = 0;
					if(mRightView != null) {
						rightWidth = mMainView.getRight();
					}
					
					if(mLastMotionX > rightWidth) return false;
				}
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	final int deltaX = (int) (x - mLastMotionX);
	        	if(mTouchMode == TOUCH_MODE_SCROLLING) {
	        		trackMotionScroll(deltaX);
	        		mLastMotionX = x;
	        	}
	        	break;
	        case MotionEvent.ACTION_CANCEL:
	        case MotionEvent.ACTION_UP:
	        	if(mTouchMode == TOUCH_MODE_SCROLLING) {	
	        		final VelocityTracker velocityTracker = mVelocityTracker;
	                final int pointerId = ev.getPointerId(0);
	                velocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
	                final float velocityX = velocityTracker.getXVelocity(pointerId);
	        		
	                if(Math.abs(velocityX) > mMinVelocity) {
	                	handleFling(velocityX);
	                }
	                else 
	                {
	                	scrollIntoSlots();
	                }
	        		
	        	}
	        	mCanScroll = false;
	        	mTouchMode = TOUCH_MODE_RESET;
	        	releaseVelocityTracker();
	        	break;
        }
		return true;
	}
	
	private void handleFling(float velocity) {
		int delta = 0;
		if(mOffset > 0) {
			if(velocity > 0) {
				delta = mMaxOffset - mOffset;
				setStatus(STATUS_SHOW_LEFT);
			}
			else {
				delta = -mOffset;
				setStatus(STATUS_SHOW_MAIN);
			}
		}
		else {
			if(velocity < 0) {
				delta = mMinOffset - mOffset;
				setStatus(STATUS_SHOW_RIGHT);
			}
			else {
				delta = -mOffset;
				setStatus(STATUS_SHOW_MAIN);
			}
		}
		mScroller.startScroll(delta);
	}
	
	private void scrollIntoSlots() {
		if(mOffset > 0) {
			if(mOffset > mMaxOffset/2) {
				showLeftView();
			} else {
				showMainView();
			}
		}
		else if(mOffset < 0){
			if(mOffset < mMinOffset/2) {
				showRightView();
			} else {
				showMainView();
			}
		}
	}
	
	private void addMovement(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		
		mVelocityTracker.addMovement(ev);
	}
	
	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	/**
	 * Callback when layout required.
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutChildren(l, t, r, b);
	}
	
	private void layoutChildren(int l, int t, int r, int b) {
		int height = b-t;
		int width = r-l;
		
		if(null != mMainView) {
			mMainView.layout(0, 0, width, height);
		}
		
		if(mLeftView != null) {
			width = mLeftView.getMeasuredWidth();
			mLeftView.layout(0, 0, width, height);
			mMaxOffset = width; 
		}
		
		if(mRightView != null) {
			width = mRightView.getMeasuredWidth();
			mRightView.layout(getWidth()-width, 0, getWidth(), height);
			mMinOffset = -width;
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawShade(canvas);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		doMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	/**
	 * Do measurement for the views.
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	private void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Left view.
		SlideViewGroup.measureSlideOut(mLeftView, mViewMargin, widthMeasureSpec, heightMeasureSpec);
		
		// Right view.
		SlideViewGroup.measureSlideOut(mRightView, mViewMargin, widthMeasureSpec, heightMeasureSpec);
		
		// Main view.
		if( null != mMainView ) {
			mMainView.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	private static void measureSlideOut(View aView, int aMargin, int widthMeasureSpec, int heightMeasureSpec) {
		if( null == aView )
			return ;
		
		LayoutParams lp = aView.getLayoutParams();
		if( null == lp ) {
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		}
		
		int subWidthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, aMargin, lp.width);
		aView.measure(subWidthMeasureSpec, heightMeasureSpec);
	}
	
	private boolean showLeftView() {
		if( null == mLeftView )
			return false;
		
		setStatus(STATUS_SHOW_LEFT);
		mScroller.startScroll(mMaxOffset - mOffset);
		return true;
	}
	
	private boolean showRightView() {
		if( null == mRightView )
			return false;
		
		setStatus(STATUS_SHOW_RIGHT);
		mScroller.startScroll(mMinOffset - mOffset);
		return true;
	}
	
	private boolean showMainView() {
		if( null == mMainView )
			return false;
		
		setStatus(STATUS_SHOW_MAIN);
		mScroller.startScroll(-mOffset);
		return true;
	}
	
	private void trackMotionScroll(int detla) {
		if(detla == 0)
			return;
		
		int newOffset = mOffset + detla;
		if((mOffset >= 0 && newOffset < 0) || (mOffset <= 0 && newOffset > 0)) {
			onDirectionChanged(newOffset > 0);
    	}
		
		if(newOffset > mMaxOffset) {
			newOffset = mMaxOffset;
		}
		
		if(newOffset < mMinOffset) {
			newOffset = mMinOffset;
		}
		int offset = newOffset - mMainView.getLeft();
		mOffset = newOffset;
		if(offset != 0) {
			mMainView.offsetLeftAndRight(offset);
		}
		invalidate();
	}
	
	private void drawShade(Canvas canvas) {
		if(mOffset > 0) {
			int right = mMainView.getLeft();
			mLeftShadow.setBounds(right - mShadeWidth, 0, right, getHeight());
			mLeftShadow.draw(canvas);
			//Log.i(TAG, "bounds = " +mLeftShadeDrawable.getBounds().toString());
		}
		else if(mOffset < 0) {
			int left = mMainView.getRight();
			mRightShadow.setBounds(left, 0, left + mShadeWidth, getHeight());
			mRightShadow.draw(canvas);
			//Log.i(TAG, "bounds = " +mRightShadeDrawable.getBounds().toString());
		}
	}
	
	private void setStatus(int state) {
		//if(STATUS_SHOW_LEFT == state) return;
		mStatus = state;
	}
	
	private void onDirectionChanged(boolean toLeft) {
		if(toLeft) {
			if(mRightView != null) {
				mRightView.setVisibility(View.GONE);
			}
			
			if(mLeftView != null) {
				mLeftView.setVisibility(View.VISIBLE);
			}
		}
		else {
			if(mRightView != null) {
				mRightView.setVisibility(View.VISIBLE);
			}
			
			if(mLeftView != null) {
				mLeftView.setVisibility(View.GONE);
			}
		}
	}
	
	private void doInit(Context aContext) {
		float density = aContext.getResources().getDisplayMetrics().density;
		mViewMargin = (int) (DEFAULT_VIEW_MARGIN * density + 0.5f);
		mShadeWidth = (int) (DEFAULT_SHADE_WIDTH * density + 0.5f);
		mTouchWidht = (int) (DEFAULT_TOUCH_WIDTH * density + 0.5f);
		final ViewConfiguration configuration = ViewConfiguration.get(aContext);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
		
		mStatus = STATUS_SHOW_MAIN;
		mTouchMode = TOUCH_MODE_RESET;
		
		// Initialize the scroller.
		mScroller = new SlideScroller();
		
		// Initialize shadow.
		initShadow();
	}
	
	private void initShadow() {
		final int[] colors = {Color.TRANSPARENT, 0X44000000, 0XAA000000};
		mLeftShadow = new GradientDrawable(Orientation.LEFT_RIGHT, colors);
		mRightShadow = new GradientDrawable(Orientation.RIGHT_LEFT, colors);
	}

	// Member instance.
	private int mViewMargin;
	private int mShadeWidth;
	private int mTouchWidht;
	private int mTouchSlop;
	private int mMinVelocity;
    private int mMaxVelocity;
    private int mStatus;
    private int mMaxOffset;
    private int mMinOffset;
    private int mOffset;
    private SlideScroller mScroller;
    private boolean mCanScroll;
    private boolean mCanLeftScroll;
    private boolean mCanRightScroll;
    private VelocityTracker mVelocityTracker;
	private int mLastMotionX = 0;
	private int mTouchMode;
	
	private View  mLeftView;  // Left slide out menu
	private View  mRightView; // Right one.
	private View  mMainView;  // Main page.
	private Drawable mLeftShadow;
	private Drawable mRightShadow;

	// Touch mode.
	private static final int TOUCH_MODE_RESET = 0;
	private static final int TOUCH_MODE_SCROLLING = 1;
	
	private static final int DEFAULT_VIEW_MARGIN = 50;
	private static final int DEFAULT_SHADE_WIDTH = 10;
	private static final int DEFAULT_TOUCH_WIDTH = 10;
	
	
	/**
	 * Scroller 
	 * @author lorenchen
	 */
	private class SlideScroller implements Runnable {
		public SlideScroller() {
			mScroller = new Scroller(getContext());
		}
		
		/*
		public void stop() {
			mScroller.forceFinished(false);
			removeCallbacks(this);
		}*/
		
		public void startScroll(int delta) {
			if( (0 == delta) || (null == mScroller) )
				return ;
			
			// Stop previous callback.
			removeCallbacks(this);
	        mScroller.forceFinished(true);
	        mCurrX = 0;
			
	        // Reset the value and start.
			mScroller.startScroll(0, 0, delta, 0, ANIMATION_DURATION);
			post(this);
		}

		@Override
		public void run() {
			boolean more = mScroller.computeScrollOffset();
			final int x = mScroller.getCurrX();
			trackMotionScroll(x - mCurrX);
			
			if(more) {
				mCurrX = x;
				post(this);
			}
		}
		
		private Scroller mScroller;
		private int      mCurrX;
		
		private static final int ANIMATION_DURATION = 500;
	}
}
