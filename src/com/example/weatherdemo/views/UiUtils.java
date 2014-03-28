/**
 * Loren Chen
 */

package com.example.weatherdemo.views;


import com.example.weatherdemo.R;
import com.example.weatherdemo.views.RadioDialog.OnRadioSelectListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class UiUtils {
	/**
	 * @param context
	 * @param nResId
	 * @param nDurationMs
	 */
	public static void makeToast(Context context, int nResId) {
		String strText = context.getString(nResId);
		UiUtils.makeToast(context, strText);
	}
	
	/**
	 * @param context
	 * @param strText
	 */
	public static void makeToast(Context context, String strText) {
		if( null == context || TextUtils.isEmpty(strText) )
			return ;
		
		LayoutInflater pInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View pLayout = pInflater.inflate(R.layout.toast_layout, null);
		if ( null != pLayout )
		{
			// Update the text.
			TextView pMessage = (TextView)pLayout.findViewById(R.id.toast_message);
			pMessage.setText(strText);
			
			// Show toast.
			UiUtils.showToast(context, pLayout);
		}
	}
	
	/**
	 * show toast and save instance to global application instance
	 * @param aContext
	 * @param aLayout
	 */
	private static void showToast(Context aContext, View aLayout) {
		/*
		// Cancel previous instance.
		if(null == pToast)
		{
			pToast = new Toast(aContext.getApplicationContext());
			pToast.setGravity(Gravity.CENTER, 0, 0);
			pToast.setDuration(Toast.LENGTH_SHORT);
		}
		*/
		Toast pToast = new Toast(aContext);
		pToast.setGravity(Gravity.CENTER, 0, 0);
		pToast.setDuration(Toast.LENGTH_SHORT);
		
		if( null != aLayout ) {
			pToast.setView(aLayout);
		}
				
		// Update the toast information.
		pToast.show();
	}
	
	public static AppDialog showDialog(Context aContext, int nCaptionResId, String strMessage, int nPositiveResId) {
		String strCaption = aContext.getString(nCaptionResId);
		return UiUtils.showDialog(aContext, strCaption, strMessage, nPositiveResId);
	}
	
	public static AppDialog showDialog(Context aContext, int nCaptionResId, int nMessageResId, int nPositiveResId) {
		return UiUtils.showDialog(aContext, nCaptionResId, nMessageResId, nPositiveResId, 0, null);
	}
	
	public static AppDialog showDialog(Context aContext, String strCaption, String strMessage, int nPositiveResId, AppDialog.OnClickListener aListener) {
		return UiUtils.showDialog(aContext, strCaption, strMessage, nPositiveResId, 0, false, aListener);
	}
	
	public static AppDialog showDialog(Context aContext, int nCaptionResId, int nMessageResId, int nPositiveResId, AppDialog.OnClickListener aListener) {
		String strCaption = aContext.getString(nCaptionResId);
		String strMessage = aContext.getString(nMessageResId);
		return UiUtils.showDialog(aContext, strCaption, strMessage, nPositiveResId, 0, false, aListener);
	}
	
	public static AppDialog showDialog(Context aContext, int nCaptionResId, int nMessageResId, int nPositiveResId, int nNegativeResId, AppDialog.OnClickListener aListener) {
		String strCaption = aContext.getString(nCaptionResId);
		String strMessage = aContext.getString(nMessageResId);
		return UiUtils.showDialog(aContext, strCaption, strMessage, nPositiveResId, nNegativeResId, false, aListener);
	}
	
	public static AppDialog showDialog(Context aContext, int nCaptionResId, int nMessageResId, int nPositiveResId, int nNegativeResId, boolean hasInput, AppDialog.OnClickListener aListener) {
		String strCaption = aContext.getString(nCaptionResId);
		String strMessage = aContext.getString(nMessageResId);
		return UiUtils.showDialog(aContext, strCaption, strMessage, nPositiveResId, nNegativeResId, hasInput, aListener);
	}
	
	public static AppDialog showDialog(Context aContext, String strCaption, String strMessage, String strPositive, String strNegative, boolean hasInput, AppDialog.OnClickListener aListener) {
		AppDialog pUiDialog = new AppDialog(aContext, hasInput, aListener);
		
		pUiDialog.setProperty(strCaption, strMessage, strPositive, strNegative);
		
		// Show the new dialog.
		pUiDialog.show();
		
		return pUiDialog;
	}
	
	public static AppDialog showDialog(Context aContext, String strCaption, String strMessage, int nPositiveResId, int nNegativeResId, AppDialog.OnClickListener aListener) {
		return UiUtils.showDialog(aContext, strCaption, strMessage, aContext.getString(nPositiveResId), (nNegativeResId > 0 ? aContext.getString(nNegativeResId) : ""), false, aListener);
	}
	
	public static AppDialog showDialog(Context aContext, String strCaption, String strMessage, int nPositiveResId, int nNegativeResId, boolean hasInput, AppDialog.OnClickListener aListener) {
		return UiUtils.showDialog(aContext, strCaption, strMessage, aContext.getString(nPositiveResId), (nNegativeResId > 0 ? aContext.getString(nNegativeResId) : ""), hasInput, aListener);
	}
	
	public static AppDialog showDialog(Context aContext, String strCaption, String strMessage, int nPositiveResId) {
		return UiUtils.showDialog(aContext, strCaption, strMessage, nPositiveResId, 0, false, null);
	}
	
	public static AppDialog showDialog(Context aContext, String strCaption, String strMessage, String strPositive) {
		return UiUtils.showDialog(aContext, strCaption, strMessage, strPositive, "", false, null);
	}
	
	/**
	 * @param context
	 * @param options
	 * @param listener
	 * @return
	 */
	public static boolean showListDialog(Context context, String[] options, OnRadioSelectListener listener) {
		return UiUtils.showListDialog(context, null, options, -1, listener, true);
	}
	
	/**
	 * @param context
	 * @param options
	 * @param checkedItem
	 * @param listener
	 * @return
	 */
	public static boolean showListDialog(Context context, String[] options, int checkedItem, OnRadioSelectListener listener) {
		return UiUtils.showListDialog(context, null, options, checkedItem, listener, true);
	}
	
	public static boolean showListDialog(Context context, String strCaption, String[] options, int checkedItem, OnRadioSelectListener listener) {
		return UiUtils.showListDialog(context, strCaption, options, checkedItem, listener, true);
	}
	
	/**
	 * @param context
	 * @param options
	 * @param listener
	 * @param cancelable
	 * @return
	 */
	public static boolean showListDialog(Context context, String[] options, OnRadioSelectListener listener, boolean cancelable) {
		return UiUtils.showListDialog(context, null, options, -1, listener, true);
	}
	
	/**
	 * @param context
	 * @param options
	 * @param checkedItem
	 * @param listener
	 * @param cancelable
	 * @return
	 */
	public static boolean showListDialog(Context context, String strCaption, String[] options, int checkedItem, OnRadioSelectListener listener, boolean cancelable) {
		if( null == context || null == options || 0 >= options.length )
			return false;
		
		RadioDialog pDialog = new RadioDialog(context, null, null);
		pDialog.setCanceledOnTouchOutside(true);
		pDialog.setCancelable(cancelable);
		pDialog.setProperty(strCaption);
		
		pDialog.setRadioSelectListener(listener);
		pDialog.setList(options, checkedItem);
		
		pDialog.show();
		
		return true;
	}
	
	public static boolean showListDialog(Context aContext, String strCaption, RadioDialog.RadioAdapter aAdapter, OnRadioSelectListener aListener) {
		if( null == aContext || null == aAdapter )
			return false;
		
		RadioDialog pDialog = new RadioDialog(aContext, null, aAdapter);
		pDialog.setCanceledOnTouchOutside(true);
		pDialog.setProperty(strCaption);
		
		pDialog.setRadioSelectListener(aListener);
		
		pDialog.show();
		
		return true;
	}
	
	/**
	 * @param aContext
	 * @param aEditText
	 */
	public static void showSoftInput(Context aContext, EditText aEditText) {
		if( null != aContext && null != aEditText ) {
			InputMethodManager pManager = (InputMethodManager)aContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
			pManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			pManager.showSoftInput(aEditText, InputMethodManager.SHOW_IMPLICIT);
		}
	}
	
	/**
	 * @param aContext
	 * @param aEditText
	 */
	public static void hideSoftInput(Context aContext, EditText aEditText) {
		if( null != aContext && null != aEditText ) {
			InputMethodManager pManager = (InputMethodManager) aContext.getSystemService(Context.INPUT_METHOD_SERVICE); 
			pManager.hideSoftInputFromWindow(aEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * @param aParent
	 * @param aTarget
	 * @param nRequestFlag
	 * @param bClearTop
	 * @return
	 */
	public static boolean startActivity(Activity aParent, Class<?> aTarget, int nRequestFlag, boolean bClearTop) {
		return UiUtils.startActivity(aParent, aTarget, null, nRequestFlag, bClearTop);
	}
	
	/**
	 * @param aParent
	 * @param aTarget
	 * @param bClearTop
	 * @return
	 */
	public static boolean startActivity(Activity aParent, Class<?> aTarget, boolean bClearTop) {
		return UiUtils.startActivity(aParent, aTarget, null, -1, bClearTop);
	}
	
	/**
	 * @param aParent
	 * @param aTarget
	 * @param aBundle
	 * @param bClearTop
	 * @return
	 */
	public static boolean startActivity(Activity aParent, Class<?> aTarget, Bundle aBundle, boolean bClearTop) {
		return UiUtils.startActivity(aParent, aTarget, aBundle, -1, bClearTop);
	}

	/**
	 * @param aParent
	 * @param aTarget
	 * @param aBundle
	 * @param nRequestFlag
	 * @return
	 */
	public static boolean startActivity(Activity aParent, Class<?> aTarget, Bundle aBundle, int nRequestFlag) {
		return UiUtils.startActivity(aParent, aTarget, aBundle, nRequestFlag, false);
	}
	
	/**
	 * @param aParent
	 * @param aTarget
	 * @param aBundle
	 * @param nRequestFlag
	 * @param bClearTop
	 * @return
	 */
	public static boolean startActivity(Activity aParent, Class<?> aTarget, Bundle aBundle, int nRequestFlag, boolean bClearTop) {
		if( null == aParent || null == aTarget )
			return false;
		
		Intent pIntent = new Intent(aParent, aTarget);
		if( null != aBundle ) {
			pIntent.putExtras(aBundle);
		}
		
		if( bClearTop ) {
			pIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		
		// Start the new instance of activity.
		if( nRequestFlag > 0 ) {
			aParent.startActivityForResult(pIntent, nRequestFlag);
		} else {
			aParent.startActivity(pIntent);
		}
		
		return true;
	}
	
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static int getInteger(Context context, TypedArray array, int index) {
		return UiUtils.getInteger(context, array, index, -1);
	}
	
	/**
	 * 
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static int getInteger(Context context, TypedArray array, int index, int defaultVal) {
		if( null == context || null == array || 0 > index )
			return defaultVal;
		
		if( !array.hasValue(index) )
			return defaultVal;
		
		return array.getInteger(index, defaultVal);
	}
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static int getColor(Context context, TypedArray array, int index) {
		if( null == context || null == array || 0 > index )
			return 0;
		
		if( !array.hasValue(index) )
			return 0;
		
		return array.getColor(index, 0);
	}
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static float getDimension(Context context, TypedArray array, int index) {
		if( null == context || null == array || 0 > index )
			return 0;
		
		if( !array.hasValue(index) )
			return 0;
		
		return array.getDimension(index, 0);
	}
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static String getString(Context context, TypedArray array, int index) {
		final int nResId = UiUtils.getResId(context, array, index);
		return (nResId > 0 ? context.getString(nResId) : array.getString(index));
	}
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static boolean getBoolean(Context context, TypedArray array, int index) {
		if( null == context || null == array || 0 > index )
			return true;
		
		if( !array.hasValue(index) )
			return true;
		
		return array.getBoolean(index, true);
	}
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @param defaultValut
	 * @return
	 */
	public static boolean getBoolean(Context context, TypedArray array, int index, boolean defaultValue) {
		if( null == context || null == array || 0 > index )
			return defaultValue;
		
		if( !array.hasValue(index) )
			return defaultValue;
		
		return array.getBoolean(index, defaultValue);
	}
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static int getResId(Context context, TypedArray array, int index) {
		return UiUtils.getResId(context, array, index, 0);
	}
	
	/**
	 * @param context
	 * @param array
	 * @param index
	 * @return
	 */
	public static int getResId(Context context, TypedArray array, int index, int defaultVal) {
		if( null == context || null == array || 0 > index )
			return defaultVal;
		
		if( !array.hasValue(index) )
			return defaultVal;
		
		return array.getResourceId(index, defaultVal);
	}
	
	/**
	 * @param aSpinner
	 * @param strValue
	 */
	public static void setSpinnerValue(Spinner aSpinner, String strValue) {
		if( (null != aSpinner) && (!TextUtils.isEmpty(strValue)) ) {
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> pAdapter = (ArrayAdapter<String>)aSpinner.getAdapter(); //cast to an ArrayAdapter
			aSpinner.setSelection(pAdapter.getPosition(strValue));
		}
	}
	
	public static void lockFractionDecimal(EditText aEditText) {
		UiUtils.lockFractionDecimal(aEditText, 2);
	}
	
	public static void lockFractionDecimal(EditText aEditText, int nLimit) {
		FractionLocker pLocaker = new FractionLocker(aEditText);
		pLocaker.limitFractionDigitsinDecimal(nLimit);
	}

	private UiUtils() {
	}
}
