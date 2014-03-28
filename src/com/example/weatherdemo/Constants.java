package com.example.weatherdemo;

import com.crazybean.network.NetConfig;

public final class Constants 
{
	//-------------------------  Error Code ------------------------------------------------------
	public static final int    ERROR_INVALID   = -9999;
	
	//-------------------------  Web Server ---------------------------------------------------
	// Default port.
	public static final int    HOST_PORT   = 12123;
	
	// API address.
	public static final String WS_MEMLOGIN = "/memberlogin";
	public static final String WS_MEM_INFO = "/memberget";
	public static final String WS_TRANINFO = "/gettran";
	public static final String WS_TTINFO   = "/ttxx";
	public static final String WS_PACKINFO = "/getcrabboxqr";
	public static final String WS_TRANLIST = "/gettranlist";
			
	// Host address
	public static final String HOST_DEFAULT = "http://crab.shou.edu.cn";
//	public static final String HOST_DEFAULT = "http://61.152.222.188";
	public static final String HOST_ADDR    = "/wstest.asmx";
	
	public static final String HOST_GEOCODING = "http://maps.googleapis.com/maps/api/geocode/xml";
	public static final int    PORT_GEOCODING = 80;

	//-------------------------  TaskId ------------------------------------------------------
	//TaskID
	public static final int TASK_TYPE_BASE                 = (NetConfig.NETTASK_ID_USER + 0x100);
	public static final int TASK_TYPE_MEM_LOGIN            = (TASK_TYPE_BASE + 1);
	public static final int TASK_TYPE_MEM_INFO             = (TASK_TYPE_BASE + 2);
	public static final int TASK_TYPE_TRAN_INFO            = (TASK_TYPE_BASE + 3);
	public static final int TASK_TYPE_REVERSE_GECODE       = (TASK_TYPE_BASE + 4);
	public static final int TASK_TYPE_PACK_INFO            = (TASK_TYPE_BASE + 5);
	public static final int TASK_TYPE_TRAN_LIST            = (TASK_TYPE_BASE + 6);
	
	/**
	* Create a new Instance Constants.  
	* Instance of the class is forbidden.
	 */
	private Constants()
	{
	}
}
