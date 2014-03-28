package com.example.weatherdemo;

import com.crazybean.framework.UiStorage;

public class DataManager implements UiStorage.Entity
{
	/**
	* method Name:getInstance    
	* method Description:  
	* @return   
	* DataManager  
	* @exception   
	* @since  1.0.0
	 */
	public static DataManager getInstance()
	{
		DataManager pManager = null;
		Object pObject = UiStorage.getEntity(DataManager.class);
		if ( null == pObject )
		{
			pManager = new DataManager();
			UiStorage.setEntity(pManager);
		}
		else
		{
			pManager = (DataManager)pObject;
		}
		
		return pManager;
	}

	/**
	 * constructor of DataManager
	 */
	private DataManager() 
	{
		mInitialized = false;
	}
	
	@Override
	public void doFinalize()
	{
	}
	
	/**
	* method Name:setInitialized    
	* method Description:  
	* @param bInitialized   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	public void setInitialized(boolean bInitialized)
	{
		mInitialized = bInitialized;
	}
	
	/**
	* method Name:initialized    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	public boolean initialized()
	{
		return mInitialized;
	}

	// Member instances.
	private boolean               	      mInitialized;       // Indicates whether is first launch or not.
}
