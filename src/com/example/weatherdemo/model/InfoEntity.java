package com.example.weatherdemo.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class InfoEntity {
	/**
	 * @return
	 * @throws JSONException
	 */
	abstract JSONObject toJson() throws JSONException;
	
	/**
	 * putArray
	 * @param aParent
	 * @param aTarget
	 * @param strKey
	 * @throws JSONException
	 */
	static void putArray(JSONObject aParent, String strKey, List<?> aTarget) throws JSONException {
		final int nSize = (null != aTarget ? aTarget.size() : 0);
		if( nSize > 0 ) {
			JSONArray aArray = new JSONArray();
			for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
				InfoEntity entity = (InfoEntity)aTarget.get(nIdx);
				JSONObject pChild = entity.toJson();
				if( null != pChild ) {
					aArray.put(pChild);
				}
			}
			
			// Save the items.
			aParent.put(strKey, aArray);
		}
	}
}
