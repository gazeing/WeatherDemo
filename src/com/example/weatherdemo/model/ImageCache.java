package com.example.weatherdemo.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Vector;

import com.crazybean.framework.UiStorage;
import com.example.weatherdemo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

public class ImageCache implements UiStorage.Entity
{
	public interface OnImageUpdateListener 
	{
		/**
		 * onImageUpdate
		 * @param aBitmap
		 * @param strId
		 */
		abstract void onImageUpdate(String strId, Bitmap aBitmap);
		
		/**
		 * onImageError
		 * @param strId
		 */
		abstract void onImageError(String strId);
	}
	
	/**
	 * getInstance
	 */
	public synchronized static ImageCache getInstance()
	{
		ImageCache pCache = null;
		Object pObject = UiStorage.getEntity(ImageCache.class);
		if ( null == pObject )
		{
			pCache = new ImageCache(UiStorage.getContext(), "images", false);
			UiStorage.setEntity(pCache);
		}
		else
		{
			pCache = (ImageCache)pObject;
		}
		
		return pCache;
	}
	
	/**
	 * Default constructor of ImageLoader
	 */
	private ImageCache(Context aContext, String strCacheDir, boolean bRemoveOnExit) 
	{
		mContext = new WeakReference<Context>(aContext);
		mDirName = aContext.getString(R.string.app_name);
		mCachePath = this.createPath(strCacheDir);
		mRemoveOnExit = bRemoveOnExit;
		mMaxCache = DEFAULT_MAX_CACHE;
		mMemCache = new Vector<CacheEntity>(mMaxCache);
		mRequests = new Vector<RequestEntity>();
		mNameMap = new HashMap<String, String>();
		mCurrent = null;
		mTask = null;
	}
	
	@Override
	public void doFinalize() {
		this.cleanup();
	}
	
	/**
	 * setMaxCache
	 * @param nMaxCache
	 */
	public void setMaxCache(int nMaxCache)
	{
		if( 0 >= nMaxCache )
			return ;
		
		if( nMaxCache > DEFAULT_MAX_CACHE )
			nMaxCache = DEFAULT_MAX_CACHE;
		
		if( nMaxCache == mMaxCache )
			return ;
		
		this.clearCache();
		mMaxCache = nMaxCache;
		mMemCache = new Vector<CacheEntity>(mMaxCache);
	}
	
	/**
	 * saveBitmap
	 * @param strId
	 * @param aBitmap
	 */
	public void saveBitmap(String strId, Bitmap aBitmap) {
		// 1. Save to memory cache.
		this.addMemCache(strId, aBitmap);
		
		// 2. Write to local storage.
		this.save2Local(strId, aBitmap);
	}
	
	public Bitmap getBitmap(String strId, int nMaxSize)
	{
		if( TextUtils.isEmpty(strId) )
			return null;
		
		// Check whether is in request now.
		if( (null != mCurrent) && (strId.equals(mCurrent.mId)) )
			return null;
		
		// Check memory first.
		Bitmap pBitmap = this.checkMem(strId);
		if( null != pBitmap )
		{
			return pBitmap;
		}
		
		String strFileName = this.getFileName(strId);
		if( TextUtils.isEmpty(strFileName) )
			return null;
		
		String strFullPath = mRoot + strFileName;
		File pFile = new File(strFullPath);
		if( null == pFile || !pFile.exists() )
			return null;
		
		pBitmap = this.decodeImage(strFullPath);
		
		// Try to resize.
		return resize(pBitmap, nMaxSize);
	}
	
	public Bitmap resize(Bitmap aBitmap, int nMaxSize) {
		if( (null != aBitmap) && (nMaxSize > 0) ){
			final int nWidth = aBitmap.getWidth();
			final int nHeight = aBitmap.getHeight();
			final int nCurrent = Math.max(nWidth, nHeight);
			if( nCurrent > nMaxSize ){
				// Calculate the new size.
				final int nOther = Math.min(nWidth, nHeight);
				final int nMinSize = nOther * nMaxSize / nCurrent;
				
				final boolean bHorizontal = nWidth > nHeight;
				final int nNewWidth = bHorizontal ? nMaxSize : nMinSize;
				final int nNewHeight = bHorizontal ? nMinSize : nMaxSize;
				
				// scale the bitmap.
				Bitmap pTarget = Bitmap.createScaledBitmap(aBitmap, nNewWidth, nNewHeight, true);
				if( null != pTarget ) {
					aBitmap.recycle();
					aBitmap = pTarget;
				}
			}
		}
		
		return aBitmap;
	}
	
	/**
	 * getBitmap
	 * @param strId
	 */
	public Bitmap getBitmap(String strId)
	{
		return getBitmap(strId, null);
	}
	
	/**
	 * getBitmap
	 * @param strId
	 * @param aListener
	 */
	public Bitmap getBitmap(String strId, OnImageUpdateListener aListener)
	{
		if( TextUtils.isEmpty(strId) )
		{
			return null;
		}
		
		// Check whether is in request now.
		if( (null != mCurrent) && (strId.equals(mCurrent.mId)) )
			return null;
		
		// Check memory first.
		Bitmap pBitmap = this.checkMem(strId);
		if( null != pBitmap )
		{
			if( null != aListener )
			{
				aListener.onImageUpdate(strId, pBitmap);
			}
			
			return pBitmap;
		}
		
		// Check cache in local file system.
		if( TextUtils.isEmpty(mCachePath) )
		{
			return checkMode(strId, aListener);
		}
		
		final boolean bRequesting = this.checkFile(strId, aListener);
		if( !bRequesting )
		{
			return checkMode(strId, aListener);
		}
		
		return null;
	}
	
	/**
	 * Decode image with options.
	 * @param strFullPath
	 * @return
	 */
	private Bitmap decodeImage(String strFullPath) {
		Bitmap pOutput = null;
		if(null == mOptions)
		{
			mOptions = new BitmapFactory.Options();
			mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			mOptions.inPurgeable = true;  
			mOptions.inInputShareable = true;
		}
		// Try to decode bitmap from local file.
		try {
			pOutput = BitmapFactory.decodeFile(strFullPath, mOptions);
		}catch (OutOfMemoryError aException) {
			aException.printStackTrace();
			System.gc();
			pOutput = null;
		}
		
		return pOutput;
	}
	
	/**
	 * checkMode
	 * @param strId
	 * @param aListener
	 * @return
	 */
	private Bitmap checkMode(String strId, OnImageUpdateListener aListener)
	{	
		// Default, we should request for network requesting.
		if( null != aListener )
		{
			// Send request for image from network.
			this.appendRequest(strId, aListener, false);
		}
		
		return null;
	}
	
	/**
	 * cleanup
	 * Clean up the memory and content to release resources.
	 */
	public void cleanup()
	{
		if( null != mTask )
		{
			mTask.cancel(true);
			mTask = null;
		}
		
		if( null != mRequests )
		{
			mRequests.clear();
			mRequests = null;
		}
		
		mCurrent = null;
		
		// Clear the memory cache.
		this.clearCache();
		
		// Clean up the array.
		if( null != mNameMap )
		{
			mNameMap.clear();
			mNameMap = null;
		}
		
		// Remove folder.
		if( !TextUtils.isEmpty(mCachePath) && mRemoveOnExit )
		{
			this.removeFolder(mCachePath);
		}
		
		if( null != mEmpty )
		{
			Bitmap pEmpty = mEmpty.get();
			if( null != pEmpty && !pEmpty.isRecycled() )
			{
				pEmpty.recycle();
			}
			pEmpty = null;
			mEmpty.clear();
			mEmpty = null;
		}
		
		if( null != mDefault )
		{
			Bitmap pDefault = mDefault.get();
			if( null != pDefault && !pDefault.isRecycled() )
			{
				pDefault.recycle();
			}
			pDefault = null;
			mDefault.clear();
			mDefault = null;
		}
		
		if( null != mContext )
		{
			mContext.clear();
			mContext = null;
		}
	}
	
	/**
	 * clearCache
	 */
	private void clearCache()
	{
		if( null != mMemCache )
		{
			int nSize = mMemCache.size();
			for( int nIdx = 0; nIdx < nSize; nIdx++ )
			{
				CacheEntity pEntity = mMemCache.elementAt(nIdx);
				pEntity.cleanup();
			}
			
			mMemCache.clear();
			mMemCache = null;
		}
	}

	/**
	 * onNewResult
	 * @param aBitmap
	 */
	private synchronized void onNewResult(Bitmap aBitmap, boolean bFromNetwork)
	{
		if( null != mCurrent )
		{
			String strId = mCurrent.mId;
			OnImageUpdateListener pListener = mCurrent.mListener;
			mCurrent = null;
			
			// Process the data.
			if( (null != aBitmap) && (!aBitmap.isRecycled()) )
			{
				// Save the bitmap to request.
				this.addMemCache(strId, aBitmap);
				
				if( bFromNetwork )
				{
					// Save image to local storage.
					this.save2Local(strId, aBitmap);
				}
					
				// Notify the listener.
				if(null!=pListener)
					pListener.onImageUpdate(strId, aBitmap);
			}
			else if (null != pListener)
			{
				pListener.onImageError(strId);
			}
		}
		
		// Process for next request.
		this.processRequest();
	}
	
	/**
	 * appendRequest
	 * @param strId
	 * @param aListener
	 */
	private boolean appendRequest(String strId, OnImageUpdateListener aListener, boolean bLocal)
	{
		if( (TextUtils.isEmpty(strId)) || (null == aListener) || (null == mRequests) )
			return false;
		
		// Check whether previous instance already exits in request queue.
		final int nSize = (null != mRequests ? mRequests.size() : 0);
		for( int nIdx = 0; nIdx < nSize; nIdx++ )
		{
			RequestEntity pRequest = mRequests.elementAt(nIdx);
			if( (null != pRequest) && (pRequest.mId.equals(strId)) )
			{
				// Already exits.
				return false;
			}
		}
		
		RequestEntity pRequest = new RequestEntity(strId, aListener, bLocal);
		mRequests.add(pRequest);
		
		// Process request.
		this.processRequest();
		return true;
	}
	
	/**
	 * processRequest
	 * @return
	 */
	private synchronized boolean processRequest()
	{
		// Pick up the top instance.
		if( (null == mRequests) || (0 >= mRequests.size()) || (null != mCurrent) )
			return false;
		
		// Pick up the first item.
		mCurrent = mRequests.remove(0);
		mTask = null;
		if( mCurrent.mLocal )
		{
			mTask = new AsyncTask<String, Void, Bitmap>()
			{
				@Override
				protected Bitmap doInBackground(String... aParams) 
				{
					String strId = aParams[0];
					String strFileName = getFileName(strId);
					String strFullPath = mRoot + strFileName;
					Bitmap pBitmap = decodeImage(strFullPath);
					
					return pBitmap;
				}
				
				@Override
				protected void onPostExecute(Bitmap aBitmap)
				{
					onNewResult(aBitmap, false);
				}
			};
			//android.util.Log.e("load img","+++ :" + mCurrent.mId);
			mTask.execute(mCurrent.mId);
		}
		else
		{
			// Send request to get image from website
		}
		
		return true;
	}
	
	/**
	 * checkMem
	 * @param strId
	 * @return
	 */
	private Bitmap checkMem(String strId)
	{
		final int nSize = (null != mMemCache ? mMemCache.size() : 0);
		for( int nIdx = 0; nIdx < nSize; nIdx++ )
		{
			CacheEntity pEntity = mMemCache.elementAt(nIdx);
			if( pEntity.mId.equals(strId) )
			{
				Bitmap pBitmap = pEntity.getBitmap();
				if( null != pBitmap )
				{
					// Exchange the position to the last one.
					mMemCache.remove(nIdx);
					mMemCache.add(pEntity);
				}
				
				return pBitmap;
			}
		}
		
		return null;
	}
	
	/**
	 * checkFile
	 * @param strId
	 * @return
	 */
	private boolean checkFile(String strId, OnImageUpdateListener aListener)
	{
		String strFileName = this.getFileName(strId);
		if( TextUtils.isEmpty(strFileName) )
			return false;
		
		// Load file content.
		File pFile = this.getFile(strFileName);

		if (null == pFile)
			return false;
		
		// Append request for get local file.
		return this.appendRequest(strId, aListener, true);
	}
	
	/**
	 * removeFolder
	 */
	private void removeFolder(String strFolder)
	{
		delAllFile(strFolder);
		removeFile(strFolder);
	}
	
	/**
	 * delAllFile
	 * @param path
	 */
	private void delAllFile(String path) 
	{
		File file = getFile(path);
		if (file == null)
			return;
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = getFile(path + tempList[i]);
			} else {
				temp = getFile(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				removeFolder(path + "/" + tempList[i]);//再删除空文件夹
			}
		}
	}
	
	private boolean removeFile(String fileName) 
	{
		File file = getFile(fileName);

		if (null != file) {
			try {
				return file.delete();
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}

		return true;
	}
	
	/**
	 * getFile
	 * @param strFileName
	 * @return
	 */
	private File getFile(String strFileName)
	{
		strFileName = !strFileName.equals("") && strFileName.startsWith(File.separator) ? strFileName.substring(1) : strFileName;
		String strFullPath = mRoot + strFileName;
		File pFile = new File(strFullPath);
		
		if( pFile.exists() )
			return pFile;
		
		pFile = null;
		return null;
	}
	
	/**
	 * addMemCache
	 * @param strId
	 * @param aBitmap
	 */
	private void addMemCache(String strId, Bitmap aBitmap)
	{
		if( null == aBitmap )
			return ;
		
		final int nSize = mMemCache.size();
		
		// Firstly, let's check whether item exists.
		for( int nIdx = 0; nIdx < nSize; nIdx++ ) {
			CacheEntity pEntity = mMemCache.elementAt(nIdx);
			if( pEntity.mId.equals(strId) ) {
				// Just update the item.
				if( pEntity.mBitmap != aBitmap ) {
					// Discard the previous one.
					pEntity.mBitmap.recycle();
					pEntity.mBitmap = aBitmap;
				}
				
				return ;
			}
		}
		
		if( nSize < mMaxCache )
		{
			CacheEntity pEntity = new CacheEntity(strId, aBitmap);
			mMemCache.add(pEntity);
		}
		else
		{
			int nPos = 0;
			for( int nIdx = 0; nIdx < nSize; nIdx++ )
			{
				CacheEntity pEntity = mMemCache.elementAt(nIdx);
				if( CacheEntity.CACHE_STATUS_OKAY != pEntity.mStatus )
				{
					nPos = nIdx;
					break;
				}
			}
			
			CacheEntity pUpdate = mMemCache.elementAt(nPos);
			pUpdate.cleanup();
			pUpdate.save(strId, aBitmap, CacheEntity.CACHE_STATUS_OKAY);
			
			// Exchange the position to last one.
			mMemCache.remove(nPos);
			mMemCache.add(pUpdate);
		}
	}
	
	/**
	 * save2Local
	 * @param strId
	 * @param aBitmap
	 */
	private boolean save2Local(String strId, Bitmap aBitmap)
	{
		if( (TextUtils.isEmpty(mCachePath)) || (TextUtils.isEmpty(strId)) || (null == aBitmap) )
			return false;
		
		// Get target file name.
		String strFileName = this.getFileName(strId);
		File pTarget = createFile(strFileName);
		if( null == pTarget )
			return false;

		boolean bSuccess = true;
		FileOutputStream output = null;
		try 
		{
			output = new FileOutputStream(pTarget);
			aBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			output.flush();
		}
		catch (Exception aException) 
		{
			aException.printStackTrace();
			bSuccess = false;
		}
		finally
		{
			if( null != output )
			{
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = null;
			}
		}
		
		return bSuccess;
	}
	
	/**
	 * createFile
	 * @param filename
	 * @return
	 */
	private File createFile(String strFileName)
	{
		String strFullPath = mRoot + strFileName;
		File pFile = new File(strFullPath);
		try {
			if (pFile.exists()) {
				pFile.delete();
			}
			
			if( !pFile.createNewFile() )
			{
				pFile = null;
			}
		}
		catch (Exception aException) 
		{
			aException.printStackTrace();
			pFile = null;
		}
		
		return pFile;
	}
	
	/**
	 * getFileName
	 * @param strId
	 * @return
	 */
	private String getFileName(String strId)
	{
		if( TextUtils.isEmpty(strId) )
			return null;
		
		String strResult = mNameMap.get(strId);
		if( !TextUtils.isEmpty(strResult) )
			return strResult;
		
		// Encode a new file name.
		String strFileName = "pyz" + AppUtils.getMD5(strId);
		strResult = mCachePath + "/" + strFileName + ImageCache.getExtension(strId) + ".cache";
		mNameMap.put(strId, strResult);
		
		return strResult;
	}
	
	/**
	 * 
	 */
	private String createPath(String strPath)
	{
		if( TextUtils.isEmpty(strPath) )
			return null;
		
		// 1. Firstly, check whether directory already exits or not.
		if (strPath.startsWith(File.separator)) {
			strPath = strPath.substring(1);
		}

		if (strPath.endsWith(File.separator)) {
			strPath = strPath.substring(0, strPath.length() - 1);
		}

		String[] dirs = strPath.split("\\" + File.separator);
		String pre = "";
		for (String dir : dirs) 
		{
			pre += ((pre.equals("") ? "" : File.separator) + dir);
			this.createDir(pre);
		}

		return strPath;
	}
	
	/**
	 * @param strPath
	 */
	private void createDir(String strPath)
	{
		if ( TextUtils.isEmpty(strPath) )
			return ;
		
		// 1. Get the root path.
		if( TextUtils.isEmpty(mRoot) )
		{
			Context pContext = mContext.get();
			if( AppUtils.hasExternalStorage() )
				mRoot = Environment.getExternalStorageDirectory() + "/" + mDirName + "/";
			else
				mRoot = pContext.getCacheDir() + "/" + mDirName + "/";
		}
		
		if( TextUtils.isEmpty(mRoot) )
			return ;
		
		// Check whether root path exists.
		File pRoot = new File(mRoot);
		if( !pRoot.exists() ) {
			pRoot.mkdir();
		}
		pRoot = null;
		
		// 2. Check whether current path exits.
		String strFullPath = mRoot + strPath;
		File pFile = new File(strFullPath);
		if( !pFile.exists() )
		{
			pFile.mkdir();
		}
		
		// Clean up.
		pFile = null;
	}
	
	private static String getExtension(String fileName) {
		if (null == fileName || fileName.length() == 0)
			return "";

		int index = fileName.lastIndexOf(".");

		if (index != -1 && index < fileName.length() - 1) {
			return fileName.substring(index);
		}

		return "";
	}
	
	private WeakReference<Context>  mContext;  // Current active context, instead of Activity instance.
	private String                  mRoot;     // Root path depends on SD card exits.
	private String                  mCachePath; // Required cache path.
	private boolean                 mRemoveOnExit; // Indicates whether remove cache folder when destroyed.
	private Vector<CacheEntity>     mMemCache;    // Memory cache for bitmap instances.
	private Vector<RequestEntity>   mRequests;    // Array for pending requests.
	private HashMap<String, String> mNameMap;     // Id -> Encoded file name.
	private WeakReference<Bitmap>   mEmpty = null;
	private WeakReference<Bitmap>   mDefault = null;
	private int                     mMaxCache;
	private RequestEntity           mCurrent;     // Current request entity instance.
	private AsyncTask<String,Void,Bitmap>   mTask; // Current asynchronous task.
	private String                  mDirName;
	private BitmapFactory.Options   mOptions;
	
	// Max memory cache.
	private static final int        DEFAULT_MAX_CACHE = 64;
	
	// Class for ImageCache.
	private static final class CacheEntity
	{
		// Cache entity status definition.
		private static final int CACHE_STATUS_NONE    = 0x0000;
		private static final int CACHE_STATUS_OKAY    = (CACHE_STATUS_NONE + 1);
		private static final int CACHE_STATUS_DISCARD = (CACHE_STATUS_NONE + 2);
		
		public CacheEntity(String strId, Bitmap aBitmap)
		{
			mId = strId;
			mStatus = (null != aBitmap ? CACHE_STATUS_OKAY : CACHE_STATUS_NONE);
			mBitmap = aBitmap;
		}
		
		/**
		 * getBitmap
		 * @return
		 */
		public Bitmap getBitmap()
		{
			if( (null != mBitmap) && (!mBitmap.isRecycled()) && (CACHE_STATUS_OKAY == mStatus) )
				return mBitmap;
			
			return null;
		}
		
		/**
		 * save
		 * @param strId
		 * @param aBitmap
		 * @param nStatus
		 */
		public void save(String strId, Bitmap aBitmap, int nStatus)
		{
			mBitmap = aBitmap;
			mId = strId;
			mStatus = nStatus;
		}
		
		/**
		 * cleanup
		 */
		public void cleanup()
		{
			if( null != mBitmap )
			{
				if( !mBitmap.isRecycled() )
				{
					mBitmap.recycle();
				}
				
				// Clean up.
				mBitmap = null;
			}
			
			// Reset the status.
			mStatus = CACHE_STATUS_DISCARD;
		}
		
		public String  mId;
		public int     mStatus;
		private Bitmap mBitmap;
	}
	
	/**
	 * class for RequestEntity
	 * @author lorenchen
	 */
	private static final class RequestEntity
	{
		/**
		 * Default constructor of RequestEntity
		 * @param strId
		 * @param aListener
		 */
		public RequestEntity(String strId, OnImageUpdateListener aListener, boolean bLocal) {
			mId = strId;
			mListener = aListener;
			mLocal = bLocal;
		}
		
		public boolean               mLocal;
		public String                mId;
		public OnImageUpdateListener mListener;
	}
}
