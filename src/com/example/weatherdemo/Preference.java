package com.example.weatherdemo;

import android.content.Context;
import android.content.SharedPreferences;

import com.crazybean.framework.UiStorage;
import com.crazybean.framework.UiStorage.Entity;
import com.crazybean.utils.ValueMap;

public final class Preference implements Entity 
{
	/**
	* method Name:getInstance    
	* method Description:  
	* @return   
	* Preference  
	* @exception   
	* @since  1.0.0
	 */
	public static Preference getInstance()
	{
		return Preference.getInstance(null);
	}
	
	/**
	* method Name:getInstance    
	* method Description:  
	* @param aContext
	* @return   
	* Preference  
	* @exception   
	* @since  1.0.0
	 */
	public static Preference getInstance(Context aContext)
	{
		Object pObject = null;
		Preference pPreference = (null == (pObject = UiStorage.getEntity(Preference.class)) ? (Preference)UiStorage.setEntity(new Preference()) : (Preference)pObject); 
		
		// Save the preference.
		pPreference.setContext(aContext);
		
		return pPreference;
	}
	
	/**
	 * setAccount
	 * @param strAccount
	 */
	public void setAccount(String strAccount)
	{
		setValue(PREF_ACCOUNT, strAccount);
	}
	
	/**
	 * getAccount
	 * @return
	 */
	public String getAccount()
	{
		return getValue(PREF_ACCOUNT);
	}

	/**
	 * setPassword
	 * @param strPassword
	 */
	public void setPassword(String strPassword)
	{
		setValue(PREF_PASSWORD, strPassword);
	}
	
	/**
	 * setMemberId
	 * @param nId
	 */
	public void setMemberId(String strMemId)
	{
		setValue(PREF_MEM_ID, strMemId);
	}
	
	/**
	 * getMemberId
	 * @return
	 */
	public String getMemberId()
	{
		return getValue(PREF_MEM_ID);
	}
	
	/**
	 * getPassword
	 * @return
	 */
	public String getPassword()
	{
		return getValue(PREF_PASSWORD);
	}
	
	/*  
	 * Description:
	 * @see com.soso.framework.UiStorage.Entity#doFinalize()
	 */
	@Override
	public void doFinalize()
	{
		if ( null != mPreferences )
		{
			// Save the items to local preference.
			final int nSize = (null != mValMap ? mValMap.size() : 0);
			if ( nSize > 0 )
			{
				SharedPreferences.Editor pEditor = mPreferences.edit();
				
				for ( int nIdx = 0; nIdx < nSize; nIdx++ )
				{
					ValueMap.Element pElement = mValMap.elementAt(nIdx);
					
					// Save to local storage.
					pEditor.putString(pElement.getKey(), pElement.getValue());
				}
				
				// Commit it.
				pEditor.commit();
			}
		}
	}
	
	/**
	* method Name:setValue    
	* method Description:  
	* @param aKey
	* @param bValue   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void setValue(String aKey, String strValue)
	{
		if ( null == mValMap )
			return ;
		
		mValMap.setValue(aKey, strValue);
	}
	
	/**
	* method Name:getValue    
	* method Description:  
	* @param aKey
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	private String getValue(String aKey)
	{
		ValueMap.Element pElement = (null != mValMap ? mValMap.getElement(aKey) : null);
		if ( null == pElement )
			return "";
		
		return pElement.getValue();
	}

	/**
	* Create a new Instance Preference.  
	*
	 */
	private Preference()
	{
		initialize();
	}
	
	/**
	* method Name:initialize    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void initialize()
	{
		mPreferences = null;
		mValMap = null;
	}
	
	/**
	* method Name:setContext    
	* method Description:  
	* @param aContext   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void setContext(Context aContext)
	{
		if ( null != aContext )
		{
			mContext = aContext;
			
			// Load the preference.
			mPreferences = null;
			mPreferences = mContext.getSharedPreferences(PREF_NAME, 0);
			loadPref();
		}
	}
	
	/**
	* method Name:loadPref    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void loadPref()
	{
		if ( (null == mPreferences) || (null != mValMap) )
			return ;
		
		mValMap = new ValueMap();
		final int nCount = PROPERTIES.length;
		for ( int nIdx = 0; nIdx < nCount; nIdx++ )
		{
			// Get the value from local storage.
			String strVal = mPreferences.getString(PROPERTIES[nIdx], "");
			
			// Save to memory.
			mValMap.addValue(PROPERTIES[nIdx], strVal);
		}
	}
	
	// Member instance.
	private Context           mContext;
	private SharedPreferences mPreferences;
	private ValueMap          mValMap;
	
	// Preference key.
	private static final String PREF_NAME         = "CRAB_APP_PREF";
	private static final String PREF_ACCOUNT      = "CRAB_ACCOUNT";
	private static final String PREF_PASSWORD     = "CRAB_PASSWORD";
	private static final String PREF_MEM_ID       = "CRAB_MEM_ID";
	private static final String[] PROPERTIES = {
		                                        PREF_ACCOUNT,
		                                        PREF_PASSWORD,
		                                        PREF_MEM_ID,
		                                        };
}
