package com.crazybean.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

public final class UiStorage {
	/**
	 * Interface to finalize
	 * @author lorenchen
	 */
	public interface Entity {
		/**
		* method Name:doFinalize    
		* method Description:     
		* void  
		* @exception   
		* @since  1.0.0
		 */
		public void doFinalize();
	}
	
	/**
	* method Name:setEntity    
	* method Description:  
	* @param aEntity   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	public static synchronized Object setEntity(Entity aEntity) {
		if ( null == aEntity )
			return null;
		
		UiStorage pSelf = UiManager.getStorage();
		if ( null == pSelf )
			return null;
		
		// Add the object.
		pSelf.addEntity(aEntity);
		
		return aEntity;
	}
	
	/**
	* method Name:getEntity    
	* method Description:  
	* @param aClass
	* @return   
	* Entity  
	* @exception   
	* @since  1.0.0
	 */
	public static synchronized Entity getEntity(Class<?> aClass) {
		if ( null == aClass )
			return null;
		
		UiStorage pSelf = UiManager.getStorage();
		if ( (null == pSelf) || (null == pSelf.mHashMap) )
			return null;
		
		Map<Class<?>, Entity> aHashMap = pSelf.mHashMap;
		return (aHashMap.containsKey(aClass) ? aHashMap.get(aClass) : null);
	}
	
	/**
	 * getContext
	 * @return the context for UiStorage
	 */
	public static synchronized Context getContext() {
		UiStorage pSelf = UiManager.getStorage();
		if ( null == pSelf )
			return null;
		
		return pSelf.mContext;
	}
	
	/**
	* method Name:addEntity    
	* method Description:  
	* @param aEntity   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void addEntity(Entity aEntity) {
		if( null == aEntity )
			return ;
		
		if ( null == mHashMap ) {
			mHashMap = new HashMap<Class<?>, Entity>();
		}
		
		Class<?> key = aEntity.getClass();
		if( !mHashMap.containsKey(key) ) {
			mHashMap.put(key, aEntity);
		}
	}
	
	/**
	* method Name:cleanup    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void cleanup() {
		if ( null != mHashMap ) {
			Iterator<Entity> iterator = mHashMap.values().iterator();
			while( iterator.hasNext() ) {
				// Do finalize task.
				Entity entity = iterator.next();
				entity.doFinalize();
			}
			
			// Clean up.
			mHashMap.clear();
			mHashMap = null;
		}
		
		// Reset the context.
		mContext = null;
	}
	
	/**
	 * method Name:setContext    
	 * method Description:     
	 * @param aContext
	 */
	void setContext(Context aContext) {
		mContext = aContext;
	}
	
	/**
	* Create a new Instance UiStorage.  
	*
	 */
	UiStorage(Context aContext) {
		mHashMap = null;
		mContext = aContext;
	}
	
	// Member instances.
	private Map<Class<?>, Entity> mHashMap;
	private Context               mContext;  // Shared context instance.
}
