package com.crazybean.framework;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.SparseArray;

import com.crazybean.framework.UiActivity.PageCache;

public final class StateMachine {
	/**
	* Create a new Instance StateMachine.  
	 */
	StateMachine() {
		mArray = null;
		mStates = null;
		mSequence = null;
	}
	
	/**
	 * Set sparse array instance for state reset.
	 * @param aMap
	 */
	void setArray(SparseArray<PageCache> aArray) {
		mArray = aArray;
	}
	
	/**
	* method Name:getPrevious
	* method Description:  
	* @return   
	* int  
	* @exception   
	* @since  1.0.0
	 */
	int getPrevious() {
		final int nCount = (null != mSequence ? mSequence.size() : 0);
		if ( 1 >= nCount )
			return UiConfig.KStateNone;
		
		// Get previous state which is not transparent.
		int nPos = nCount - 2;
		StateInfo pInfo = mSequence.get(nPos);
		while ( (StateMachine.isTranscient(pInfo.mState)) && (nPos >= 0) ) {
			// Check next position.
			nPos--;
			// Update the state.
			pInfo = mSequence.get(nPos);
		}

		return pInfo.mState;
	}
	
	/**
	* method Name:moveBackward    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	Bundle moveBackward(Bundle aBundle) {
		final int nCount = (null != mSequence ? mSequence.size() : 0);
		if ( 1 >= nCount )
			return null;
		
		// Get previous state which is not transparent.
		StateInfo pRemove = mSequence.remove(mSequence.size() - 1);
		pRemove.reset();
		pRemove = null;
		StateInfo pCurrent = mSequence.get(mSequence.size() - 1);
		while ( StateMachine.isTranscient(pCurrent.mState) ) {
			pRemove = mSequence.remove(mSequence.size() - 1);
			pRemove.reset();
			pRemove = null;
			
			// Update the state.
			pCurrent = mSequence.get(mSequence.size() - 1);
		}
		
		// Update the bundle if exists.
		if ( (null != pCurrent) && (null != aBundle) ) {
			pCurrent.mBundle = aBundle;
		}

		return (null != pCurrent ? pCurrent.mBundle : null);
	}
	
	/**
	* method Name:getCurrent    
	* method Description:  
	* @return   
	* int  
	* @exception   
	* @since  1.0.0
	 */
	int getCurrent() {
		final int nCount = (null != mSequence ? mSequence.size() : 0);
		StateInfo pCurrent = (nCount > 0 ? mSequence.get(nCount - 1) : null);
		return (null != pCurrent ? pCurrent.mState : UiConfig.KStateNone);
	}
	
	/**
	 * getBundle
	 * @return
	 */
	Bundle getBundle() {
		final int nCount = (null != mSequence ? mSequence.size() : 0);
		StateInfo pCurrent = (nCount > 0 ? mSequence.get(nCount - 1) : null);
		return (null != pCurrent ? pCurrent.mBundle : null);
	}
	
	/**
	* method Name:moveForward
	* method Description:  
	* @param nState   
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void moveForward(final int nCurrent, Bundle aBundle) {
		// Next state cannot be any!!!
		if ( nCurrent == UiConfig.KStateAny )
			return ;
		
		// Get the total count.
		final int nCount = (null != mSequence ? mSequence.size() : 0);
		int nPos = nCount - 1;
		
		// Check whether current state is a transparent one.
		StateInfo pCurrent = (nPos >= 0 ? mSequence.get(nPos) : null);
		if ( (null != pCurrent) && (StateMachine.isTranscient(pCurrent.mState)) ) {
			// Replace the latest one.
			pCurrent.reset();
			pCurrent.mState = nCurrent;
			pCurrent.mBundle = aBundle;
			
			return ;
		}	
		
		// Check the previous instance.
		for ( nPos = 0; nPos < nCount; nPos++ ) {
			StateInfo pEntity = mSequence.get(nPos);
			if ( (null != pEntity) && (nCurrent == pEntity.mState) ) {
				// Update the bundle to last one.
				pEntity.mBundle = aBundle;
				break;
			}
		}
		
		// Not found in previous sequence.
		if ( nPos >= nCount ) {
			// Check capacity.
			this.checkCapacity();
			
			// Update the current state.
			StateInfo pInfo = new StateInfo(nCurrent);
			pInfo.mBundle = aBundle;
			mSequence.add(pInfo);
		} else {
			// Trace back, new state already exists in sequence array.
			for ( int nIdx = nCount - 1; nIdx > nPos; nIdx-- ) {
				StateInfo pRemove = mSequence.remove(nIdx);
				pRemove.reset();
				pRemove = null;
			}
		}
	}
	
	/**
	* method Name:reset    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	void reset() {
		// Reset the state to KStateChaos.
		final int nCount = (null != mSequence ? mSequence.size() : 0);
		for ( int nIdx = nCount - 1; nIdx >= 1; nIdx-- ) {
			StateInfo pEntity = mSequence.remove(nIdx);
			pEntity.reset();
			pEntity = null;
		}
	}
	
	/**
	* method Name:loadStates    
	* method Description:  
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	StateError loadStates(final int aStates[][], boolean aDebuggable) {
		StateError pError = new StateError(StateError.KErrNone, 0, 0);
		final int nSize = aStates.length;
		if ( 0 >= nSize ) {
			pError.mErrCode = StateError.KErrTableEmpty;
			return pError;
		}
		
		// Create the instance for state table.
		if ( null == mStates ) {
			mStates = new ArrayList<StateEntry>();
		} else {
			// Clear the previous content.
			mStates.clear();
		}
		
		// Create the table.
		for ( int nIdx = 0; nIdx < nSize; nIdx++ ) {
			final int aEntry[] = aStates[nIdx];
			
			// Get next state.
			final int nNext = aEntry[2];
			if ( (aDebuggable) && (nNext == UiConfig.KStateAny) ) {
				pError.mErrCode = StateError.KErrNextIsAny;
				pError.mRow1 = nIdx;
				return pError;
			}
			
			// Get the item.
			final StateEntry pEntry = new StateEntry(aEntry[0], aEntry[1], nNext, true);
			
			// Save to array.
			final int nOffset = this.addEntry(pEntry, aDebuggable);
			if ( nOffset <= nIdx ) {
				// Set the error type.
				final StateEntry pPrev = mStates.get(nOffset);
				pError.mErrCode = (pPrev.mNext == pEntry.mNext ? StateError.KErrDuplicated : StateError.KErrAmbiguous);
				
				// Save the index.
				pError.mRow1 = nOffset;
				pError.mRow2 = nIdx;
				
				return pError;
			}
		}
		
		// Set initialized state.
		this.moveForward(UiConfig.KStateChaos, null);
		
		return pError;
	}
	
	/**
	* method Name:getNext    
	* method Description:  
	* @param nEventId
	* @return   
	* int  
	* @exception   
	* @since  1.0.0
	 */
	int getNext(int nEventId) {
		if ( UiConfig.KEventNone == nEventId )
			return UiConfig.KStateNone;
			
		final int nCount = (null != mStates ? mStates.size() : 0);
		final int nCurrent = this.getCurrent();
		StateEntry pOptional = null;
		for ( int nIdx = 0; nIdx < nCount; nIdx++ ) {
			final StateEntry pEntry = mStates.get(nIdx);
			if ( pEntry.mEventId == nEventId ) {
				if ( pEntry.mCurrent == nCurrent ) {
					// OK, find the correct one.
					return pEntry.mNext;
				} else if ( UiConfig.KStateAny == pEntry.mCurrent ) {
					// Pick current entry as an option.
					pOptional = pEntry;
				}
			}
		}
		
		// Not found.
		return (null != pOptional ? pOptional.mNext : UiConfig.KStateNone);
	}
	
	/**
	* method Name:addEntry    
	* method Description:  
	* @param aEntry
	* @return   
	* int  
	* @exception   
	* @since  1.0.0
	 */
	private int addEntry(final StateEntry aEntry, boolean aDebuggable) {
		final int nCount = mStates.size();
		if ( aDebuggable ) {
			for ( int nIdx = 0; nIdx < nCount; nIdx++ ) {
				final StateEntry pItem = mStates.get(nIdx);
				if ( (pItem.mCurrent == aEntry.mCurrent) && (pItem.mEventId == aEntry.mEventId) ) {
					return nIdx;
				}
			}
		}
		
		// Save to array.
		mStates.add(aEntry);
		return (nCount + 1);
	}
	
	/**
	* method Name:isTranscient    
	* method Description:  
	* @param nState
	* @return   
	* boolean  
	* @exception   
	* @since  1.0.0
	 */
	private static boolean isTranscient(int nState) {
		return (UiConfig.KTranscient == (UiConfig.KTranscient & nState));
	}
	
	/**
	* method Name:checkCapacity    
	* method Description:     
	* void  
	* @exception   
	* @since  1.0.0
	 */
	private void checkCapacity() {
		if ( null == mSequence ) {
			mSequence = new ArrayList<StateInfo>(KStepSize);
		}
	}
	
	private List<StateInfo>      mSequence; // Operation sequence with data.
	private List<StateEntry>     mStates;   // States table.
	private SparseArray<PageCache> mArray;
	
	private static final int    KStepSize = 32; // Increase size.
	
	///////////////////////////////////////////////////////////////////////////////////
	/**
	 * StateInfo
	 */
	final class StateInfo {
		/**
		 * Default constructor of StateInfo
		 * @param nState
		 */
		public StateInfo(int nState) {
			mState = nState;
		}
		
		/**
		 * reset
		 */
		public void reset() {
			// Set the page cache to discard flag.
			PageCache pCache = (null != mArray ? mArray.get(mState) : null);
			if( (null != pCache) && (null != pCache.mPage) && (!pCache.mPage.mCacheable) ) {
				pCache.mDiscard = true;
			}
			
			mBundle = null;
		}
		
		public int    mState;
		public Bundle mBundle;
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	/**
	 * StateError
	 */
	final class StateError {
		// Error code definition.
		public static final int KErrNone       = 0; // No error.
		public static final int KErrTableEmpty = (KErrNone + 1); // Table empty.
		public static final int KErrAmbiguous  = (KErrNone + 2); // Item ambiguous.
		public static final int KErrDuplicated = (KErrNone + 3); // Item duplicated.
		public static final int KErrNextIsAny  = (KErrNone + 4); // Next state is any.
		
		/**
		 * 
		* Create a new Instance StateError.  
		 */
		public StateError(int aErrCode, int aIndex1, int aIndex2) {
			mErrCode = aErrCode;
			mRow1 = aIndex1;
			mRow2 = aIndex2;
		}
		
		public String toString() {
			StringBuilder pBuilder = new StringBuilder();
			pBuilder.append("StateMachine, ErrCode: ");
			pBuilder.append(mErrCode);
			switch (mErrCode) {
			case KErrNone:
				pBuilder.append(", Perfect!");
				break;
				
			case KErrTableEmpty:
				pBuilder.append(", Table empty!");
				break;
				
			case KErrAmbiguous:
				pBuilder.append(", Entities are Ambiguous!");
				pBuilder.append(" == Row 1: ");
				pBuilder.append(mRow1);
				pBuilder.append(", Row 2: ");
				pBuilder.append(mRow2);
				break;
				
			case KErrDuplicated:
				pBuilder.append(", Entities are Duplicated!");
				pBuilder.append(" ==== Row 1: ");
				pBuilder.append(mRow1);
				pBuilder.append(", Row 2: ");
				pBuilder.append(mRow2);
				break;
				
			case KErrNextIsAny:
				pBuilder.append(", Next state cannot be StateAny");
				pBuilder.append(" == Row: ");
				pBuilder.append(mRow1);
				break;

			default:
				break;
			}
			
			return pBuilder.toString();
		}
		
		// Member instance.
		public int mErrCode;
		public int mRow1;
		public int mRow2;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////
	/**
	* Class Name:StateEntry 
	* Class Description: 
	* Author: lorenchen 
	* Modify: lorenchen 
	* Modify Date: Mar 18, 2011 1:41:08 PM 
	* Modify Remarks: 
	* @version 1.0.0
	*
	 */
	private final class StateEntry {
		/**
		 * 
		* Create a new Instance StateEntry.  
		*  
		* @param nCurrent
		* @param nEventId
		* @param nNext
		 */
		StateEntry(int nCurrent, int nEventId, int nNext, boolean bBackable) {
			mCurrent = nCurrent;
			mEventId = nEventId;
			mNext = nNext;
		}
		
		int mCurrent;
		int mEventId;
		int mNext;
	}
}
