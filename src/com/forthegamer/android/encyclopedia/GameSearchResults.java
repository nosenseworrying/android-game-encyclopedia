package com.forthegamer.android.encyclopedia;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GameSearchResults extends ListActivity {
	private String mPlatform = null;
	private String mQuery = null;
	private String mPlatformDisplay = null;
	private String mBaseURL = "http://api.gamesradar.com/search/gameName/";
	private String mApiParam = "?api_key=[YOUR_KEY_HERE]";
	private String mCurrentURL = null;
	private TextView mEmptyView;
	private TextView mHeaderView;
	private ProgressDialog mProgressDialog;
	private GamesListAdapter mGamesAdapter;
	private Boolean mIsEmpty = false;
	private static final String MY_DEBUG_TAG = "GameSearchResults";
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_list);
        
        mEmptyView = (TextView)findViewById(R.id.txtEmpty);
        mHeaderView = (TextView)findViewById(R.id.txtHeader);
        if(savedInstanceState != null){
	       	mPlatform = savedInstanceState.getString("platform");
	       	mQuery = savedInstanceState.getString("query");
	       	mPlatformDisplay = savedInstanceState.getString("platformdisplay");
        }         
        
        if(mPlatform == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
            	mPlatform = extras.getString("platform");
            	mQuery = extras.getString("query");
            	mPlatformDisplay = extras.getString("platformdisplay");
            }
        }
        
        new GetSearchListTask().execute();
        
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mPlatform != null){
			outState.putString("platform", mPlatform);
		}
		if(mQuery != null){
			outState.putString("query", mQuery);
		}
		if(mPlatformDisplay != null){
			outState.putString("platformdisplay", mPlatformDisplay);
		}
	}
	
	 private class GetSearchListTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... unused) {
	    	 String encodedQuery = URLEncoder.encode(mQuery);
	    	 mCurrentURL = mBaseURL + mPlatform + "/" + encodedQuery + mApiParam;
	    	 
			 try {
				 URL apiURL = new URL(mCurrentURL);
				 
	             /* Get a SAXParser from the SAXPArserFactory. */
	             SAXParserFactory spf = SAXParserFactory.newInstance();
	             SAXParser sp = spf.newSAXParser();

	             /* Get the XMLReader of the SAXParser we created. */
	             XMLReader xr = sp.getXMLReader();
	             /* Create a new ContentHandler and apply it to the XML-Reader*/
	             GamesSearchHandler searchHandler = new GamesSearchHandler();
	             searchHandler.setPlatform(mPlatformDisplay);
	             xr.setContentHandler(searchHandler);
	             
	             /* Parse the xml-data from our URL. */
	             xr.parse(new InputSource(apiURL.openStream()));
	             /* Parsing has finished. */

	             /* Our ExampleHandler now provides the parsed data to us. */             
	             ArrayList<HashMap<String,String>> list = searchHandler.getList();
	             int listSize = list.size();
	             if(listSize == 0){
	            	 mIsEmpty = true;
	             } else {
	            	 mIsEmpty = false;
	             }
	             mGamesAdapter = new GamesListAdapter(GameSearchResults.this, list);
	            
			 } catch(Exception e){
				 Log.e(MY_DEBUG_TAG, "GetSearchListTaskError", e);
				 return false;
			 }			    	 
	    	 
	         return true;
	     }

	     protected void onPostExecute(Boolean result) {
	    	 if(result){
	    		 mHeaderView.setText("Results for '" + mQuery + "' on " + mPlatform + ":");
	    		 if(mIsEmpty){
	    			 mEmptyView.setVisibility(View.VISIBLE);
	    		 } else {
	    			 mEmptyView.setVisibility(View.GONE);
	    		 }
	    		 GameSearchResults.this.setListAdapter(mGamesAdapter);
	    		 mProgressDialog.dismiss();	    		 
	    		 
	    	 } else {
	    		 mProgressDialog.dismiss();
	    		 //Toast.makeText(GameSearchResults.this, "Retrieval Failure", Toast.LENGTH_SHORT).show();
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(GameSearchResults.this);
	    		 builder.setCancelable(true);
	    		 //builder.setIcon(R.drawable.dialog_question);	    		 
	    		 builder.setTitle("Retrieval Failure");
	    		 builder.setMessage("Please check your network connection and try again.");
	    		 builder.setInverseBackgroundForced(true);
	    		 builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
	    		   public void onClick(DialogInterface dialog, int which) {
	    			 new GetSearchListTask().execute();
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
             mProgressDialog = new ProgressDialog(GameSearchResults.this);
             mProgressDialog.setMessage("Loading Games...");
             mProgressDialog.setIndeterminate(true);
             mProgressDialog.setCancelable(true);
             mProgressDialog.show();
	     }
	 }
	 
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        Intent i = new Intent(this, GameLoad.class);
	        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
	        //i.putExtra("headline", mList.get(position).get("headline"));
	        HashMap<String,String> selectedItem = (HashMap<String,String>)mGamesAdapter.getItem(position);
	        //i.putExtra("url", mList.get(position).get("url"));
	        String gameid = selectedItem.get("gameid");
	        String name = selectedItem.get("gamename");
	        
	        i.putExtra("gameid", selectedItem.get("gameid"));
	        i.putExtra("gamename", selectedItem.get("gamename"));
	        startActivity(i);
	    }		 
}
