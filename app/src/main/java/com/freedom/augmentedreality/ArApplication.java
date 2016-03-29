
package com.freedom.augmentedreality;

import org.artoolkit.ar.base.assets.AssetHelper;

import android.app.Application;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.freedom.augmentedreality.helper.LruBitmapCache;

public class ArApplication extends Application {

	public static final String TAG = ArApplication.class.getSimpleName();
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static ArApplication sInstance;
	 
    public static synchronized ArApplication getInstance() {
    	return sInstance;
    }

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

    @Override
    public void onCreate() {
    	super.onCreate(); 
    	sInstance = this;
    	((ArApplication) sInstance).initializeInstance();
    }

    protected void initializeInstance() {
    	PreferenceManager.setDefaultValues(this, org.artoolkit.ar.base.R.xml.preferences, false);
    	
		AssetHelper assetHelper = new AssetHelper(getAssets());
		assetHelper.cacheAssetFolder(getInstance(), "Data");
		assetHelper.cacheAssetFolder(getInstance(), "DataNFT");
    }

}
