package com.example.weatherdemo.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class UserInfo extends InfoEntity implements Serializable {

	/**
	 * Constructor of UserInfo
	 */
	public UserInfo() {
	}
	
	public static String generateId() {
		return "userinfo_" + System.currentTimeMillis();
	}
	
	public String getDisplayName() {
		String strDisplay = mFirstName;
		if( !TextUtils.isEmpty(mLastName) ) {
			if( !TextUtils.isEmpty(strDisplay) ) {
				strDisplay += (" " + mLastName);
			} else {
				strDisplay = mLastName;
			}
		}
		
		return strDisplay;
	}
	
	/**
	 * for identify the icon image.
	 * @return
	 */
	public String getId() {
		return mId;
	}
	
	public void setId(String strId) {
		mId = strId;
	}
	
	public void setFirstName(String strFirstName) {
		mFirstName = strFirstName;
	}
	
	public String getFirstName() {
		return mFirstName;
	}
	
	public void setMiddleName(String strMiddleName) {
		mMidName = strMiddleName;
	}
	
	public String getMiddleName() {
		return mMidName;
	}
	
	public void setLastName(String strLastName) {
		mLastName = strLastName;
	}
	
	public String getLastName() {
		return mLastName;
	}
	
	public void setEmail(String strEmail) {
		mEmail = strEmail;
	}
	
	public String getEmail() {
		return mEmail;
	}
	
	public void setPhone(String strPhone) {
		mPhone = strPhone;
	}
	
	public String getPhone() {
		return mPhone;
	}
	
	public void setStreet(String strStreet) {
		mStreet = strStreet;
	}
	
	public String getStreet() {
		return mStreet;
	}
	
	public void setCity(String strCity) {
		mCity = strCity;
	}
	
	public String getCity() {
		return mCity;
	}
	
	public void setState(String strState) {
		mState = strState;
	}
	
	public String getState() {
		return mState;
	}
	
	public void setZipcode(String strZipcode) {
		mZipcode = strZipcode;
	}
	
	public String getZipcode() {
		return mZipcode;
	}
	
	public void setCountry(String strCountry) {
		mCountry = strCountry;
	}
	
	public String getCountry() {
		return mCountry;
	}
	
	public void setBirthday(String strBirthday) {
		mBirthday = strBirthday;
	}
	
	public String getBirthday() {
		return mBirthday;
	}
	
	public void setPrefix(String strPrefix) {
		mPrefix = strPrefix;
	}
	
	public String getPrefix() {
		return mPrefix;
	}
	
	public void setSuffix(String strSuffix) {
		mSuffix = strSuffix;
	}
	
	public String getSuffix() {
		return mSuffix;
	}
	
	public void setGender(String strGender) {
		mGender = strGender;
	}
	
	public String getGender() {
		return mGender;
	}
	
	public void setLanguage(String strLanguage) {
		mLanguage = strLanguage;
	}
	
	public String getLanguage() {
		return mLanguage;
	}
	
	public void setTimezone(String strTimezone) {
		mTimezone = strTimezone;
	}
	
	public String getTimezone() {
		return mTimezone;
	}
	
	@Override
	JSONObject toJson() throws JSONException {
		JSONObject pObject = new JSONObject();

		pObject.put(KEY_ID, mId);
		pObject.put(KEY_PREFIX, mPrefix);
		pObject.put(KEY_FIRSTNAME, mFirstName);
		pObject.put(KEY_MIDNAME, mMidName);
		pObject.put(KEY_LASTNAME, mLastName);
		pObject.put(KEY_SUFFIX, mSuffix);
		pObject.put(KEY_EMAIL, mEmail);
		pObject.put(KEY_PHONE, mPhone);
		pObject.put(KEY_STREET, mStreet);
		pObject.put(KEY_CITY, mCity);
		pObject.put(KEY_STATE, mState);
		pObject.put(KEY_ZIPCODE, mZipcode);
		pObject.put(KEY_COUNTRY, mCountry);
		pObject.put(KEY_BIRTHDAY, mBirthday);
		pObject.put(KEY_GENDER, mGender);
		pObject.put(KEY_LANGUAGE, mLanguage);
		pObject.put(KEY_TIMEZONE, mTimezone);
		
		return pObject;
	}
	
	static UserInfo fromJson(JSONObject aObject) {
		if( null == aObject )
			return null;
		
		UserInfo pUserInfo = new UserInfo();
		pUserInfo.mId = aObject.optString(KEY_ID);
		pUserInfo.mPrefix = aObject.optString(KEY_PREFIX);
		pUserInfo.mFirstName = aObject.optString(KEY_FIRSTNAME);
		pUserInfo.mMidName = aObject.optString(KEY_MIDNAME);
		pUserInfo.mLastName = aObject.optString(KEY_LASTNAME);
		pUserInfo.mSuffix = aObject.optString(KEY_SUFFIX);
		pUserInfo.mEmail = aObject.optString(KEY_EMAIL);
		pUserInfo.mPhone = aObject.optString(KEY_PHONE);
		pUserInfo.mStreet = aObject.optString(KEY_STREET);
		pUserInfo.mCity = aObject.optString(KEY_CITY);
		pUserInfo.mState = aObject.optString(KEY_STATE);
		pUserInfo.mZipcode = aObject.optString(KEY_ZIPCODE);
		pUserInfo.mCountry = aObject.optString(KEY_COUNTRY);
		pUserInfo.mBirthday = aObject.optString(KEY_BIRTHDAY);
		pUserInfo.mGender = aObject.optString(KEY_GENDER);
		pUserInfo.mLanguage = aObject.optString(KEY_LANGUAGE);
		pUserInfo.mTimezone = aObject.optString(KEY_TIMEZONE);
		return pUserInfo;
	}
	
	private String mPrefix;
	private String mFirstName;
	private String mMidName;
	private String mLastName;
	private String mSuffix;
	private String mEmail;
	private String mPhone;
	private String mStreet;
	private String mCity;
	private String mState;
	private String mZipcode;
	private String mCountry;
	private String mBirthday;
	private String mGender;
	private String mLanguage;
	private String mTimezone;
	private String mId;
	
	// KEY definition.
	private static final String KEY_ID        = "id";
	private static final String KEY_PREFIX    = "prefix";
	private static final String KEY_FIRSTNAME = "firstname";
	private static final String KEY_LASTNAME  = "lastname";
	private static final String KEY_MIDNAME   = "midname";
	private static final String KEY_SUFFIX    = "suffix";
	private static final String KEY_EMAIL     = "email";
	private static final String KEY_PHONE     = "phone";
	private static final String KEY_STREET    = "street";
	private static final String KEY_CITY      = "city";
	private static final String KEY_STATE     = "state";
	private static final String KEY_ZIPCODE   = "zipcode";
	private static final String KEY_COUNTRY   = "country";
	private static final String KEY_BIRTHDAY  = "birthday";
	private static final String KEY_GENDER    = "gender";
	private static final String KEY_LANGUAGE  = "language";
	private static final String KEY_TIMEZONE  = "timezone";
	
	private static final long serialVersionUID = 3299774984050533478L;
}
