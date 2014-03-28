package com.buddy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class SlidingMenuView extends ViewGroup {
	
	static final String TAG = "SlidingMenuView";
	
	static final int DEFAULT_VIEW_MARGIN = 50;
	
	static final int DEFAULT_SHADE_WIDTH = 10;
	
	static final int DEFAULT_TOUCH_WIDTH = 10;
	
	
	View mLeftView;
	View mRightView;
	
	View mContentView;
	
	Drawable mLeftShadeDrawable;
	
	Drawable mRightShadeDrawable;
	
	float mDensity;
	
	int mViewMargin;
	
	int mShadeWidth;
	
	int mTouchWidht;
	
	private int mTouchSlop;
	private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
	
	private VelocityTracker mVelocityTracker;
	
	private int mLastMotionX = 0;
	private int mLastMotionY = 0;
	
	private static final int TOUCH_MODE_RESET = 0;
	
	private static final int TOUCH_MODE_SCROLLING = 1;
	
	private int mTouchMode = TOUCH_MODE_RESET;
	
	
	private static final int SHOW_STATE_CONTENT = 0;
	private static final int SHOW_STATE_LEFT_OPEN = 1;
	
	private static final int SHOW_STATE_RIGHT_OPEN = 2;
	
	private int mShowState =  SHOW_STATE_CONTENT;
	
	boolean bCanScroll = false;
	
	int mMaxOffset;
	
	int mMinOffset;
	
	int mOffset;
	
	FastScroller mFastScroller = new FastScroller();
	
	boolean bToLeft = false;
	
	boolean bCanLeftScroll = false;
	
	boolean bCanRightScroll = false;
	
	
	public void setCanSliding(boolean left, boolean right) {
		bCanLeftScroll = left;
		bCanRightScroll = right;
	}
	
	
	private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    
	
	public SlidingMenuView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public SlidingMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		init(context);
	}
	
	
	public boolean isLeftOpen() {
		return mShowState == SHOW_STATE_LEFT_OPEN;
	}
	
	public boolean isRightOpen() {
		return mShowState == SHOW_STATE_RIGHT_OPEN;
	}
	
	public boolean isShowContent() {
		return mShowState == SHOW_STATE_CONTENT;
	}
	
	
	public void toggleLeftView() {
		if(mLeftView == null) return;
		
		if(isShowContent()) {
			showLeftView();
		}
		else if(isRightOpen()) {
			showContent();
		}
		else if(isLeftOpen()) {
			showContent();
		}
	}
	
	
	public void toggleRightView() {
		if(mRightView == null) return;
		if(isShowContent()) {
			showRightView();
		}
		else if(isRightOpen()) {
			showContent();
		}
		else if(isLeftOpen()) {
			showContent();
		}
	}
	
	private void init(Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		mViewMargin = (int) (DEFAULT_VIEW_MARGIN * density + 0.5f);
		mShadeWidth = (int) (DEFAULT_SHADE_WIDTH * density + 0.5f);
		mTouchWidht = (int) (DEFAULT_TOUCH_WIDTH * density + 0.5f);
		//Log.d(TAG, "int  mViewMargin = " + mViewMargin + ", mShadeWidth = " + mShadeWidth + ", mTouchWidht = " + mTouchWidht);
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
		initShadeDrawable();
	}
	
	private void initShadeDrawable() {
		int[] colors = {Color.TRANSPARENT, 0X44000000, 0XAA000000};
		mLeftShadeDrawable = new GradientDrawable(Orientation.LEFT_RIGHT, colors);
		mRightShadeDrawable = new GradientDrawable(Orientation.RIGHT_LEFT, colors);
	}
	
	
	public void setLeftView(View left) {
		mLeftView = left;
		addView(left, 0);
	}
	
	public void setRightView(View right) {
		mRightView = right;
		addView(right, 0);
	}
	
	public void setContentView(View content) {
		mContentView = content;
		addView(content);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		measureViews(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	private void measureViews(int widthMeasureSpec, int heightMeasureSpec) {
		
		if(mLeftView != null) {
			LayoutParams lp = mLeftView.getLayoutParams();
	        if (lp == null) {
	            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
	        }
			int leftWidthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, mViewMargin, lp.width);
			mLeftView.measure(leftWidthMeasureSpec, heightMeasureSpec);
		}
		
		if(mRightView != null) {
			LayoutParams lp = mRightView.getLayoutParams();
	        if (lp == null) {
	            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
	        }
			int rightWidthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, mViewMargin, lp.width);
			mRightView.measure(rightWidthMeasureSpec, heightMeasureSpec);
		}
		
		if(mContentView != null) {
			mContentView.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//Log.i(TAG, "-----------onLayout------------changed = " + changed);
		layoutChildren(l, t, r, b);
	}
	
	private void layoutChildren(int l, int t, int r, int b) {
		
		int height = b-t;
		int width = r-l;
		
		if(mContentView != null) {
			mContentView.layout(0, 0, width, height);
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
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
		if(mContentView == null) return false;
		
		final int action = ev.getAction() & MotionEvent.ACTION_MASK;
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();
		
		//Log.d(TAG, "---------onInterceptTouchEvent--------- action = " + action);
		switch(action) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionX = x;
	        	mLastMotionY = y;
	        	
	        	
	        	if(mShowState == SHOW_STATE_CONTENT) {
					if(mTouchMode== TOUCH_MODE_RESET && (x < mTouchWidht || x > (getWidth()-mTouchWidht))){
						bCanScroll = true;
					}
				}
				else if(mShowState == SHOW_STATE_LEFT_OPEN){
					int leftWidth = 0;
					
					if(mLeftView != null) {
						leftWidth = mContentView.getLeft();
					}
					if(x > leftWidth) {
						bCanScroll = true;
					}
				}
				else if(mShowState == SHOW_STATE_RIGHT_OPEN) {
					int rightWidth = 0;
					if(mRightView != null) {
						rightWidth = mContentView.getRight();
					}
					
					if(x < rightWidth){
						bCanScroll = true;
					}
				}
				
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	if(!bCanScroll && !bCanLeftScroll && !bCanRightScroll) return false;
	        	final int deltaX = (int) (x - mLastMotionX);
                //final int deltaY = (int) (y - mLastMotionY);
	        	if(mTouchMode != TOUCH_MODE_SCROLLING) {
	        		if(Math.abs(deltaX) > mTouchSlop) {
	        			
	        			if(bCanScroll || (deltaX > 0 && bCanLeftScroll) || (deltaX < 0 && bCanRightScroll)) {
		        			mTouchMode = TOUCH_MODE_SCROLLING;
		        			mLastMotionX = x;
		        			mLastMotionY = y;
		        			
		        			return true;
	        			}
	        		}
	        	}
	        	break;
	        case MotionEvent.ACTION_CANCEL:
	        case MotionEvent.ACTION_UP:
	        	bCanScroll = false;
	        	break;
		}
		return false;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if(mContentView == null) return false;
		
		final int action = ev.getAction() & MotionEvent.ACTION_MASK;

		final int x = (int) ev.getX();
		final int y = (int) ev.getY();
		//Log.d(TAG, "---------onTouchEvent--------- action = " + action);
		
		acquireVelocityTrackerAndAddMovement(ev);
        
        switch (action & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:
	        	mLastMotionX = x;
	        	mLastMotionY = y;
				
				if(mShowState == SHOW_STATE_CONTENT) {
					if(mTouchMode == TOUCH_MODE_RESET) {
						if(mLastMotionX > mTouchWidht && mLastMotionX < (getWidth()-mTouchWidht)) return false;
					}
				}
				else if(mShowState == SHOW_STATE_LEFT_OPEN){
					int leftWidth = 0;
					
					if(mLeftView != null) {
						leftWidth = mContentView.getLeft();
					}
					if(mLastMotionX < leftWidth) return false;
				}
				else if(mShowState == SHOW_STATE_RIGHT_OPEN) {
					int rightWidth = 0;
					if(mRightView != null) {
						rightWidth = mContentView.getRight();
					}
					
					if(mLastMotionX > rightWidth) return false;
				}
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	final int deltaX = (int) (x - mLastMotionX);
	        	if(mTouchMode == TOUCH_MODE_SCROLLING) {
	        		trackMotionScroll(deltaX);
	        		mLastMotionX = x;
        			mLastMotionY = y;
	        	}
	        	break;
	        case MotionEvent.ACTION_CANCEL:
	        case MotionEvent.ACTION_UP:
	        	if(mTouchMode == TOUCH_MODE_SCROLLING) {	
	        		final VelocityTracker velocityTracker = mVelocityTracker;
	                final int pointerId = ev.getPointerId(0);
	                velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
	                final float velocityX = velocityTracker.getXVelocity(pointerId);
	        		
	                if(Math.abs(velocityX) > mMinimumFlingVelocity) {
	                	handleFling(velocityX);
	                }
	                else 
	                {
	                	scrollIntoSlots();
	                }
	        		
	        	}
	        	bCanScroll = false;
	        	mTouchMode = TOUCH_MODE_RESET;
	        	releaseVelocityTracker();
	        	break;
        }
		return true;
	}
	
	
	private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
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
	
	private void setShowState(int state) {
		if(mShowState == state) return;
		mShowState = state;
	}

	
	private void handleFling(float velocity) {
		int detla = 0;
		if(mOffset > 0) {
			if(velocity > 0) {
				detla = mMaxOffset - mOffset;
				setShowState(SHOW_STATE_LEFT_OPEN);
			}
			else {
				detla = -mOffset;
				setShowState(SHOW_STATE_CONTENT);
			}
		}
		else {
			if(velocity < 0) {
				detla = mMinOffset - mOffset;
				setShowState(SHOW_STATE_RIGHT_OPEN);
			}
			else {
				detla = -mOffset;
				setShowState(SHOW_STATE_CONTENT);
			}
		}
		mFastScroller.startScroll(detla);
	}
	
	private void scrollIntoSlots() {
		if(mOffset > 0) {
			if(mOffset > mMaxOffset/2) {
				showLeftView();
			}
			else {
				showContent();
			}
		}
		else if(mOffset < 0){
			if(mOffset < mMinOffset/2) {
				showRightView();
			}
			else {
				showContent();
			}
		}
	}
	
	
	private void showLeftView() {
		int detla = mMaxOffset - mOffset;
		setShowState(SHOW_STATE_LEFT_OPEN);
		mFastScroller.startScroll(detla);
	}
	
	private void showRightView() {
		int detla = mMinOffset - mOffset;
		setShowState(SHOW_STATE_RIGHT_OPEN);
		mFastScroller.startScroll(detla);
	}
	
	private void showContent() {
		int detla = -mOffset;
		setShowState(SHOW_STATE_CONTENT);
		mFastScroller.startScroll(detla);
	}
	
	
	private void trackMotionScroll(int detla) {
		if(detla == 0) return;
		int newOffset = mOffset + detla;
		
		if((mOffset >= 0 && newOffset < 0) || (mOffset <= 0 && newOffset > 0)) {
    		onOritationChanged(newOffset > 0);
    	}
		
		if(newOffset > mMaxOffset) {
			newOffset = mMaxOffset;
		}
		
		if(newOffset < mMinOffset) {
			newOffset = mMinOffset;
		}
		//Log.d(TAG, "---------trackMotionScroll--------- mOffset = " + newOffset);
		int offset = newOffset - mContentView.getLeft();
		//int offset = newOffset + mContentView.getScrollX();
		//Log.d(TAG, "---------trackMotionScroll--------- offset = " + offset);
		mOffset = newOffset;
		if(offset != 0) {
			mContentView.offsetLeftAndRight(offset);
			//mContentView.scrollBy(-offset, 0);
		}
		invalidate();
	}
	
	private void onOritationChanged(boolean toLeft) {
		if(toLeft) {
			if(mLeftView != null) {
				mLeftView.setVisibility(View.GONE);
			}
			
			if(mRightView != null) {
				mLeftView.setVisibility(View.VISIBLE);
			}
		}
		else {
			if(mLeftView != null) {
				mLeftView.setVisibility(View.VISIBLE);
			}
			
			if(mRightView != null) {
				mLeftView.setVisibility(View.GONE);
			}
		}
	}
	
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "---------dispatchDraw--------- mOffset = " + mOffset);
		
		//long start = System.currentTimeMillis();
		super.dispatchDraw(canvas);
		drawShade(canvas);
		//long end = System.currentTimeMillis();
		//Log.e(TAG, "---------dispatchDraw--------- time = " + (end-start));
	}
	
	
	private void drawShade(Canvas canvas) {

		if(mOffset > 0) {
			int right = mContentView.getLeft();
			mLeftShadeDrawable.setBounds(right-mShadeWidth, 0, right, getHeight());
			mLeftShadeDrawable.draw(canvas);
			//Log.i(TAG, "bounds = " +mLeftShadeDrawable.getBounds().toString());
		}
		else if(mOffset < 0) {
			int left = mContentView.getRight();
			mRightShadeDrawable.setBounds(left, 0, left + mShadeWidth, getHeight());
			mRightShadeDrawable.draw(canvas);
			//Log.i(TAG, "bounds = " +mRightShadeDrawable.getBounds().toString());
		}
	}
	
	class FastScroller implements Runnable {
		
		static final int ANIMATION_DURATION = 450;
		
		Scroller mScroller;
		
		int mLastValue;
		
		public FastScroller() {
			mScroller = new Scroller(getContext());
		}
		
		private void startCommon() {
	        // Remove any pending flings
	        removeCallbacks(this);
	        mScroller.forceFinished(true);
	    }
		
		public void stop() {
			mScroller.forceFinished(false);
			removeCallbacks(this);
		}
		
		public void startScroll(int detla) {
			if(detla == 0) return;
			startCommon();
			mLastValue = 0;
			mScroller.startScroll(0, 0, detla, 0, ANIMATION_DURATION);
			post(this);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			boolean more = mScroller.computeScrollOffset();
			
			int x = mScroller.getCurrX();
			
			trackMotionScroll(x-mLastValue);
			
			if(more) {
				mLastValue = x;
				post(this);
			}
		}
		
	}
}
