package com.example.weatherdemo.views;


import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class FractionLocker {
    public FractionLocker(EditText editText) {
        mEditText = editText;
        editText.setOnKeyListener(mOnKeyListener);
    }

    private OnKeyListener mOnKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                startStopEditing(false);
            }

            return false;
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!mEditText.getText().toString().equalsIgnoreCase("") && mRunning == false) {

            	mRunning = true;
            	String editTextString = mEditText.getText().toString().trim();
            	int decimalIndexOf = editTextString.indexOf(".");
            	int length = editTextString.length();
            	int cursor = mEditText.getSelectionStart();
            	if(decimalIndexOf == -1) {
	            	if(length <= mFractionLimit) {
	                	editTextString = "." + editTextString;
	                } else if(length > mFractionLimit) {
	                	editTextString = editTextString.substring(0, editTextString.length() - 2) + "." + editTextString.substring(editTextString.length() - 2);
	                }
	            	cursor += 1;
            	} else {
            		editTextString = editTextString.replace(".","");
            		if(length <= mFractionLimit) {
	                	editTextString = "." + editTextString;
	                } else if(length > mFractionLimit) {
	                	editTextString = editTextString.substring(0, editTextString.length() - 2) + "." + editTextString.substring(editTextString.length() - 2);
	                }
            	}
            	mEditText.setText(editTextString);
            	mEditText.setSelection(cursor);
            	mRunning = false;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void limitFractionDigitsinDecimal(int nFractionLimit) {
    	mFractionLimit = nFractionLimit;
        mEditText.addTextChangedListener(mTextWatcher);
    }

    public void unlockEditText() {
        startStopEditing(false);
    }

    public void startStopEditing(boolean isLock) {

        if (isLock) {

        	mEditText.setFilters(new InputFilter[] { new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
                }
            } });

        } else {

        	mEditText.setFilters(new InputFilter[] { new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    return null;
                }
            } });
        }
    }
    
    private EditText mEditText;
    private int      mFractionLimit;
    static boolean   mRunning;
}
