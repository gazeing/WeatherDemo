package com.crazybean.framework;

public final class UiConfig {
	// Comment ui state
	public static final int KTranscient    = 0x40000000;
	public static final int KStateNone     = 0x1000;
	public static final int KStateChaos    = (KStateNone + 1);  // Reserved state to indicate application is starting.
	public static final int KStateAny      = (KStateNone + 2);  // Reserved state to indicate any state.
	public static final int KStatePrev     = (KStateNone + 3);  // Reserved state to indicates previous state.
	public static final int KStateUser     = (KStateNone + 0x100);
	
	// Comment event id.
	public static final int KEventNone     = 0x2000;
	public static final int KEventInit     = (KEventNone + 1);  // Reserved event, initializing the application.
	public static final int KEventBack     = (KEventNone + 2);  // Reserved event, for back event.
	public static final int KEventSearch   = (KEventNone + 3);  // Reserved event, for system search key.
	public static final int KEventExit     = (KEventNone + 4);  // Reserved event, try to exit the application.
	public static final int KEventUser     = (KEventNone + 0x100); // Base event for user definition.
	
	/**
	* Create a new Instance UiEvent.
	* Instanced of UiConfig is forbidden.  
	 */
	private UiConfig() {}
}
