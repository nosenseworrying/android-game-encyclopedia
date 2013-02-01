package com.forthegamer.android.encyclopedia;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FeatureLoad extends Activity {
	private WebView mWebView = null;
	private ProgressBar mProgress = null;
	private String mURL;
	
	public static final int WEB_MENU_GROUP = Menu.FIRST + 1;
	public static final int WEB_REFRESH_ID = Menu.FIRST + 2;
	public static final int WEB_STOP_ID = Menu.FIRST + 3;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feature_load);
        mWebView = (WebView)findViewById(R.id.webkit);
        mProgress = (ProgressBar)findViewById(R.id.progress);
        mProgress.setProgress(0);
        
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setInitialScale(100);

        //final Activity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient() {
          public void onProgressChanged(WebView view, int progress) {
        	mProgress.setProgress(progress);
          }
        });
        
        mWebView.setWebViewClient(new WebViewClient() {
		  public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			  Toast.makeText(FeatureLoad.this, "Error: " + description, Toast.LENGTH_SHORT).show();
		  }
		  public void onPageStarted(WebView  view, String  url, Bitmap  favicon){
			  mProgress.setVisibility(View.VISIBLE);
		  }
		  public void onPageFinished(WebView  view, String  url){
			  mProgress.setVisibility(View.GONE);
		  }
        });
        
       
        if(savedInstanceState != null){
        	mURL = savedInstanceState.getString("url");
        }
        
        if(mURL == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
            	mURL = extras.getString("url");
            }
        }
        
        loadFeature();
    }
	
	private void loadFeature(){
		if(mURL != null){
			// To avoid pan and zoom Incredible problem
			try{
				Method m = mWebView.getClass().getMethod("setIsCacheDrawBitmap", boolean.class);
				if(m != null){
					m.invoke(mWebView, false);
					mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
				}
			} catch (NoSuchMethodException e){				
			} catch (IllegalAccessException e){				
			} catch (InvocationTargetException e){				
			}
			// End of Incredible fix
			mWebView.loadUrl(mURL);
		}
	}
	
	private void stopLoading(){
		if(mWebView != null){
			mWebView.stopLoading();
		}
	}
	
	private void refreshPage(){
		if(mWebView != null){
			mWebView.reload();
		}
	}
/*
	@Override
	protected void onResume() {
		super.onResume();
		loadFeature();
	}
*/
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("url", mURL);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return(super.onCreateOptionsMenu(menu));
	}	
	
	private void populateMenu(Menu menu) {
		menu.add(WEB_MENU_GROUP, WEB_REFRESH_ID, 0, "Refresh");
		menu.add(WEB_MENU_GROUP, WEB_STOP_ID, 0, "Stop");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return(applyMenuChoice(item) ||
		super.onOptionsItemSelected(item));
	}
	
	private boolean applyMenuChoice(MenuItem item){
		switch (item.getItemId()) {
			case WEB_REFRESH_ID:
				refreshPage();
				return true;
			case WEB_STOP_ID:
				stopLoading();
				return true;				
		}
		
		return false;
	}
}
