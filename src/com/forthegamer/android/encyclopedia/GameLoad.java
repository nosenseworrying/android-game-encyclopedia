package com.forthegamer.android.encyclopedia;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameLoad extends Activity {
	private String mGameName = null;
	private String mGameID = null;
	private String mGameRelease = null;
	private String mGamePlatform = null;
	private String mGameDescription = null;
	private String mGameGenre = null;
	private String mGameRating = null;
	private String mGameScoreID = null;
	private String mGameScoreName = null;
	private String mThumbURL = null;
	private String mGameOfficialSite = null;
	private boolean mShowOfficialSite = false;
	private String mGameNews = null;
	private boolean mShowGameNews = false;
	private String mGameReview = null;
	private boolean mShowGameReview = false;
	private String mGameScreenshots = null;
	private boolean mShowGameScreens = false;
	private String mGameCheats = null;
	private boolean mShowGameCheats = false;
	private ImageView mThumbView = null;
	private TextView mHeaderView = null;
	private TextView mDescription = null;
	private TextView mPlatform = null;
	private TextView mGenre = null;
	private TextView mRelease = null;
	private TextView mRating = null;
	private TextView mScore = null;
	//private ImageView mScoreImage = null;
	private TextView mViewMenu = null;
	private String mBaseURL = "http://api.gamesradar.com/game/";
	private String mApiURL = "?api_key=[YOUR_KEY_HERE]";
	private ProgressDialog mProgressDialog;
	private Bitmap mThumbnail;
	private static final String MY_DEBUG_TAG = "GameLoad";
	private static final String EMPTY_LOGO_URL = "http://static.gamesradar.com/images/empty_01_logo.jpg";
	private static final String GAMES_RADAR_PREFIX = "http://www.gamesradar.com";
	private String mReviewUrl = "http://api.gamesradar.com/game/reviews/";
	private String mGameReviewUrl = null;
	
	//private static final int REVIEW_REQUEST = 20;
	
	private static final int OFFICIAL_SITE_ID = Menu.FIRST + 1;
	private static final int FIND_NEWS_ID = Menu.FIRST + 2;
	private static final int READ_REVIEW_ID = Menu.FIRST + 3;
	private static final int SCREENSHOTS_ID = Menu.FIRST + 4;
	private static final int CHEATS_EXTRAS_ID = Menu.FIRST + 5;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_load);
        
        if(savedInstanceState != null){
        	mGameName = savedInstanceState.getString("gamename");
        	mGameID = savedInstanceState.getString("gameid");
        }
        
        if(mGameName == null || mGameID == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
            	mGameName = extras.getString("gamename");
            	mGameID = extras.getString("gameid");
            }
        }
        
        // Get Views
        mThumbView = (ImageView)findViewById(R.id.imgThumb);
        mHeaderView = (TextView)findViewById(R.id.txtHeader);
        mDescription = (TextView)findViewById(R.id.txtDescription);
        mPlatform = (TextView)findViewById(R.id.txtPlatform);
        mGenre = (TextView)findViewById(R.id.txtGenre);
        mRelease = (TextView)findViewById(R.id.txtRelease);
        mRating = (TextView)findViewById(R.id.txtRating);
        mScore = (TextView)findViewById(R.id.txtScore);
        //mScoreImage = (ImageView)findViewById(R.id.imgScore);
        mViewMenu = (TextView)findViewById(R.id.txtViewMenu);
        
        // Set Header
        mHeaderView.setText(mGameName);
        
        // Get Values
        new GetGameTask().execute();
	}
	
	 private class GetGameTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... unused) {
    		 String url = mBaseURL + mGameID + mApiURL;
    		 
			 try {
				 URL apiURL = new URL(url);
				 
	             /* Get a SAXParser from the SAXPArserFactory. */
	             SAXParserFactory spf = SAXParserFactory.newInstance();
	             SAXParser sp = spf.newSAXParser();

	             /* Get the XMLReader of the SAXParser we created. */
	             XMLReader xr = sp.getXMLReader();
	             /* Create a new ContentHandler and apply it to the XML-Reader*/
	             GameHandler myGameHandler = new GameHandler();
	             xr.setContentHandler(myGameHandler);
	             
	             /* Parse the xml-data from our URL. */
	             xr.parse(new InputSource(apiURL.openStream()));
	             /* Parsing has finished. */

	             /* Our ExampleHandler now provides the parsed data to us. */             
	             HashMap<String,String> item = myGameHandler.getItem();
	             mGameDescription = item.get("description");
	             mGameGenre = item.get("genre");
	             mGameRating = item.get("rating");
	             mGameScoreID = item.get("scoreid");
	             mGameScoreName = item.get("scorename");
	             mThumbURL = item.get("thumburl");
	             mGamePlatform = item.get("platform");
	             mGameRelease = item.get("release");
	             mGameOfficialSite = item.get("officialsite");
	             mGameNews = item.get("news");
	             mGameReview = item.get("review");
	             mGameScreenshots = item.get("screenshots");
	             mGameCheats = item.get("cheats");
	             
			 } catch(Exception e){
				 Log.e(MY_DEBUG_TAG, "GetGameTaskError", e);
				 return false;
			 }		    	 
	    	 
	    	 return true;
	     }
	     
	     protected void onPostExecute(Boolean result) {
	    	 if(result){
		    	 if(mGameDescription == null || mGameDescription == ""){
		    		 mGameDescription = "No Description Available";
		    	 }
		    	 mDescription.setText(mGameDescription);
		    	 
		    	 if(mGamePlatform == null || mGamePlatform == ""){
		    		 mGamePlatform = "Unknown";
		    	 }
		    	 mPlatform.setText(mGamePlatform);
		    	 
		    	 if(mGameGenre == null || mGameGenre == ""){
		    		 mGameGenre = "Unknown";
		    	 }
		    	 mGenre.setText(mGameGenre);
		    	 
		    	 if(mGameRelease == null || mGameRelease == ""){
		    		 mGameRelease = "Unknown";
		    	 }
		    	 mRelease.setText(mGameRelease);
		    	 
		    	 if(mGameRating == null || mGameRating == ""){
		    		 mGameRating = "Unknown";
		    	 }
		    	 mRating.setText(mGameRating);
		    	 
		    	 String scoreText = "";
		    	 if(mGameScoreID == null || mGameScoreID == ""){
		    		 scoreText = "Unknown";
		    		 mScore.setTextColor(Color.WHITE);
		    	 } else {
		    		 scoreText = mGameScoreID + " - " + mGameScoreName;
		    		 int scoreNumber = Integer.parseInt(mGameScoreID);
		    		 if(scoreNumber < 4){
		    			 mScore.setTextColor(Color.RED);
		    		 } else if(scoreNumber < 7){
		    			 mScore.setTextColor(Color.rgb(255, 102, 51));
		    		 } else {
		    			 mScore.setTextColor(Color.GREEN);
		    		 }
		    		 /*if(scoreNumber == 10){
		    			 mScoreImage.setBackgroundResource(R.drawable.ten_incredible);
		    			 mScoreImage.setVisibility(View.VISIBLE);
		    		 }*/
		    	 }
		    	 mScore.setText(scoreText);
	    		 
		    	 boolean showMenu = false;
		    	 if(mGameOfficialSite != null && mGameOfficialSite != ""){
		    		 showMenu = true;
		    		 mShowOfficialSite = true;
		    	 }
		    	 
		    	 if(mGameNews != null && mGameNews != ""){
		    		 showMenu = true;
		    		 mShowGameNews = true;
		    	 }
		    	 
		    	 if(mGameReview != null && mGameReview != ""){
		    		 showMenu = true;
		    		 mShowGameReview = true;
		    	 }
		    	 
		    	 if(mGameScreenshots != null && mGameScreenshots != ""){
		    		 showMenu = true;
		    		 mShowGameScreens = true;
		    	 }
		    	 
		    	 if(mGameCheats != null && mGameCheats != ""){
		    		 showMenu = true;
		    		 mShowGameCheats = true;
		    	 }		    	 
		    	 
		    	 if(showMenu){
		    		 mViewMenu.setVisibility(View.VISIBLE);
		    	 }
		    	 
	    		 mProgressDialog.dismiss();
	    		 
	    		 // Get the thumbnail
	    		 if(mThumbURL != null && mThumbURL != "" && mThumbURL != EMPTY_LOGO_URL){
	    			 new GetImageTask().execute();
	    		 }
		    		 
	    	 } else {
	    		 mProgressDialog.dismiss();
	    		 //Toast.makeText(GameLoad.this, "Retrieval Failure", Toast.LENGTH_SHORT).show();
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(GameLoad.this);
	    		 builder.setCancelable(true);
	    		 //builder.setIcon(R.drawable.dialog_question);	    		 
	    		 builder.setTitle("Retrieval Failure");
	    		 builder.setMessage("Please check your network connection and try again.");
	    		 builder.setInverseBackgroundForced(true);
	    		 builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
	    		   public void onClick(DialogInterface dialog, int which) {
	    			 new GetGameTask().execute();
	    		     dialog.dismiss();
	    		   }
	    		 });
	    		 builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		   public void onClick(DialogInterface dialog, int which) {
	    		     dialog.dismiss();
	    		   }
	    		 });
	    		 AlertDialog alert = builder.create();
	    		 alert.show();		    		 
	    	 }
	     }
	     
	     protected void onPreExecute (){
             mProgressDialog = new ProgressDialog(GameLoad.this);
             mProgressDialog.setMessage("Loading Game...");
             mProgressDialog.setIndeterminate(true);
             mProgressDialog.setCancelable(true);
             mProgressDialog.show();
	     }	     
	 }
	
	 
	 private class GetImageTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... unused) {
	          try {
                  /* Open a new URL and get the InputStream to load data from it. */
                  URL aURL = new URL(mThumbURL);
                  URLConnection conn = aURL.openConnection();
                  conn.connect();
                  InputStream is = conn.getInputStream();
                  /* Buffered is always good for a performance plus. */
                  BufferedInputStream bis = new BufferedInputStream(is);
                  /* Decode url-data to a bitmap. */
                  mThumbnail = BitmapFactory.decodeStream(bis);
                  bis.close();
                  is.close();
             } catch (IOException e) {
                  Log.e(MY_DEBUG_TAG, "GetImageTask", e);
                  return false;
             } 
	    	 
	    	 return true;
	     }
	     
	     protected void onPostExecute(Boolean result) {
	    	 if(result){
                 /* Apply the Bitmap to the ImageView that will be returned. */
                 mThumbView.setImageBitmap(mThumbnail);
	    	 } else {
	    		 Toast.makeText(GameLoad.this, "Failed to get image", Toast.LENGTH_SHORT).show();
	    	 }
	     }
	 }
	 
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("gamename", mGameName);
		outState.putString("gameid", mGameID);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	public void populateMenu(Menu menu){
		if(mShowOfficialSite){
			menu.add(Menu.NONE, OFFICIAL_SITE_ID, Menu.NONE, "Official Game Site");
		}
		if(mShowGameNews){
			menu.add(Menu.NONE, FIND_NEWS_ID, Menu.NONE, "Find Related News");
		}
		if(mShowGameReview){
			menu.add(Menu.NONE, READ_REVIEW_ID, Menu.NONE, "Read Review");
		}
		if(mShowGameScreens){
			menu.add(Menu.NONE, SCREENSHOTS_ID, Menu.NONE, "Screenshots");
		}	
		if(mShowGameCheats){
			menu.add(Menu.NONE, CHEATS_EXTRAS_ID, Menu.NONE, "Cheats, Guides, Extras");
		}		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return(applyMenuChoice(item) ||
		super.onOptionsItemSelected(item));
	}
	
	private boolean applyMenuChoice(MenuItem item){
		switch (item.getItemId()) {
			case OFFICIAL_SITE_ID:
				//Toast.makeText(GameLoad.this, "Load Official Site!", Toast.LENGTH_SHORT).show();
				if(!mGameOfficialSite.startsWith("http")){
					mGameOfficialSite = "http://" + mGameOfficialSite;
				}
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGameOfficialSite))); 
				return true;
			case FIND_NEWS_ID:
				Intent i = new Intent(this, GameNewsList.class);
				i.putExtra("id", mGameID);
				startActivity(i);
				return true;
			case READ_REVIEW_ID:
				//Intent reviewIntent = new Intent(this, GameReviewActivity.class);
				//reviewIntent.putExtra("id", mGameID);
				//this.startActivityForResult(reviewIntent, REVIEW_REQUEST);
				new GetReviewTask().execute();
				return true;
			case SCREENSHOTS_ID:
				String screensURL = GAMES_RADAR_PREFIX + mGameScreenshots;
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(screensURL))); 
				return true;
			case CHEATS_EXTRAS_ID:
				String cheatsURL = GAMES_RADAR_PREFIX + mGameCheats;
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(cheatsURL)));
				return true;
		}
		return false;
	}
/*	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REVIEW_REQUEST){
			if(resultCode == RESULT_OK){
				// Launch the review
				String reviewURL = "";
				if(data != null){
					Bundle extras = data.getExtras();
					if(extras != null){
						reviewURL = extras.getString("url");
					}
				}
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reviewURL)));
			} else {
				// Error!
				Toast.makeText(GameLoad.this, "Failed to retrieve Review", Toast.LENGTH_SHORT).show();
			}
		}
	}
*/	
	 private class GetReviewTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... unused) {
	    	 mReviewUrl = mReviewUrl + mGameID + mApiURL;
	    	 
			 try {
				 URL apiURL = new URL(mReviewUrl);
				 
	             /* Get a SAXParser from the SAXPArserFactory. */
	             SAXParserFactory spf = SAXParserFactory.newInstance();
	             SAXParser sp = spf.newSAXParser();

	             /* Get the XMLReader of the SAXParser we created. */
	             XMLReader xr = sp.getXMLReader();
	             /* Create a new ContentHandler and apply it to the XML-Reader*/
	             GameReviewHandler reviewHandler = new GameReviewHandler();
	             xr.setContentHandler(reviewHandler);
	             
	             /* Parse the xml-data from our URL. */
	             xr.parse(new InputSource(apiURL.openStream()));
	             /* Parsing has finished. */
	             
	             mGameReviewUrl = reviewHandler.getURL();
	            
			 } catch(Exception e){
				 Log.e(MY_DEBUG_TAG, "GetSearchListTaskError", e);
				 return false;
			 }			    	 
	    	 
	         return true;
	     }

	     protected void onPostExecute(Boolean result) {
	    	 if(result){
	    		 //GameNewsList.this.setListAdapter(mGameNewsAdapter);
	    		 mProgressDialog.dismiss();	
	    		 
	    		 // Launch intent to browser
	    		 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGameReviewUrl)));
	    		 //Intent i = new Intent();
	    		 //i.putExtra("url", mUrl);
	    		 //setResult(RESULT_OK, i);
	    		 //finish();
	    		 
	    	 } else {
	    		 mProgressDialog.dismiss();
	    		 //Toast.makeText(GameSearchResults.this, "Retrieval Failure", Toast.LENGTH_SHORT).show();
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(GameLoad.this);
	    		 builder.setCancelable(true);
	    		 //builder.setIcon(R.drawable.dialog_question);	    		 
	    		 builder.setTitle("Retrieval Failure");
	    		 builder.setMessage("Please check your network connection and try again.");
	    		 builder.setInverseBackgroundForced(true);
	    		 builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
	    		   public void onClick(DialogInterface dialog, int which) {
	    			 new GetReviewTask().execute();
	    		     dialog.dismiss();
	    		   }
	    		 });
	    		 builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		   public void onClick(DialogInterface dialog, int which) {
	    		     dialog.dismiss();
	    		   }
	    		 });
	    		 AlertDialog alert = builder.create();
	    		 alert.show();		    		 
	    	 }
	     }
	     
	     protected void onPreExecute (){
             mProgressDialog = new ProgressDialog(GameLoad.this);
             mProgressDialog.setMessage("Loading Review...");
             mProgressDialog.setIndeterminate(true);
             mProgressDialog.setCancelable(true);
             mProgressDialog.show();
	     }
	 }		
}
