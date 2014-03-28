package com.example.weatherdemo;

import com.crazybean.framework.UiConfig;
import com.crazybean.framework.UiPage;

public final class AppConfig  
{ 
	// State table.
	public static final int KStateTable[][] =
	{
		{UiConfig.KStateChaos,    UiConfig.KEventInit,         AppConfig.KSplashPage},
		{UiConfig.KStateAny,      UiConfig.KEventBack,         UiConfig.KStatePrev},
		
		{UiConfig.KStateAny,      AppConfig.KShowMain,         AppConfig.KMainPage},
		{AppConfig.KMainPage,     UiConfig.KEventBack,         UiConfig.KStateChaos},
//		
		{UiConfig.KStateAny,      AppConfig.KInput,            AppConfig.KInputPage},
		{UiConfig.KStateAny,      AppConfig.KHistory,          AppConfig.KHistoryPage},
		{UiConfig.KStateAny,      AppConfig.KGenerate,         AppConfig.KGeneratePage},
		{UiConfig.KStateAny,      AppConfig.KDividend,         AppConfig.KDividendPage},
//		
//		{AppConfig.KTranListPage, AppConfig.KTranInfo,         AppConfig.KTranInfoPage},
//		{AppConfig.KTranInfoPage, AppConfig.KViewUser,         AppConfig.KUserInfoPage},
	};
	
	// States
	private static final int KSplashPage       = (UiConfig.KStateUser + 1);
	private static final int KMainPage         = (UiConfig.KStateUser + 2);
	private static final int KInputPage        = (UiConfig.KStateUser + 3);
	private static final int KHistoryPage      = (UiConfig.KStateUser + 4);
	private static final int KGeneratePage     = (UiConfig.KStateUser + 5);
	private static final int KDividendPage     = (UiConfig.KStateUser + 6);
//	private static final int KUserInfoPage     = (UiConfig.KStateUser + 7);
//	
//	// Event IDs
	public static final int KShowMain          = (UiConfig.KEventUser + 1);
	public static final int KInput        	   = (UiConfig.KEventUser + 2);
	public static final int KHistory           = (UiConfig.KEventUser + 3);
	public static final int KGenerate          = (UiConfig.KEventUser + 4);
	public static final int KDividend          = (UiConfig.KEventUser + 5);
//	public static final int KLogout            = (UiConfig.KEventUser + 6);
	
	/**
	 * getPage
	 */
	public static UiPage getPage(int nState)
	{
		UiPage pPage = null;
		switch ( nState )
		{
		case AppConfig.KSplashPage:
			pPage = new SplashPage();
			break;
			
//		case AppConfig.KMainPage:
//			pPage = new MainPage();
//			break;
//			
////		case AppConfig.KMyInfoPage:
//		case AppConfig.KInputPage:
//			pPage = new InputPage();
//			break;
//			
//		case AppConfig.KHistoryPage:
//			pPage = new HistoryPage();
//			break;
//			
//		case AppConfig.KGeneratePage:
//			pPage = new GeneratePage();
//			break;
//			
//		case AppConfig.KDividendPage:
//			pPage = new DividendsPage();
//			break;
			
		default:
			break;
		}
		
		return pPage;
	}
}
