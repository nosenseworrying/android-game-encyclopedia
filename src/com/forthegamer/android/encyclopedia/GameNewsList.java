package com.forthegamer.android.encyclopedia;

import java.net.URL;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class GameNewsList extends ListActivity {
	private String mApiUrl = "http://api.gamesradar.com/game/news/";
	private String mApiKey = "?api_key=[YOUR_KEY_HERE]";
	private String mGameID = null;
	private boolean mIsEmpty = false;
	private ProgressDialog mProgressDialog;
	private GameNewsAdapter mGameNewsAdapter;
	private static final String MY_DEBUG_TAG = "GameNewsList";
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_list);
        
        if(savedInstanceState != null){
	       	mGameID = savedInstanceState.getString("id");
        }         
        
        if(mGameID == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
            	mGameID = extras.getString("id");
            }
        }
        
        new GetSearchListTask().execute();
        
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mGameID != null){
			outState.putString("id", mGameID);
		}
	}
	
	 private class GetSearchListTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... unused) {
	    	 mApiUrl = mApiUrl + mGameID + mApiKey;
	    	 
			 try {
				 URL apiURL = new URL(mApiUrl);
				 
	             /* Get a SAXParser from the SAXPArserFactory. */
	             SAXParserFactory spf = SAXParserFactory.newInstance();
	             SAXParser sp = spf.newSAXParser();

	             /* Get the XMLReader of the SAXParser we created. */
	             XMLReader xr = sp.getXMLReader();
	             /* Create a new ContentHandler and apply it to the XML-Reader*/
	             GameNewsListHandler searchHandler = new GameNewsListHandler();
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
	             mGameNewsAdapter = new GameNewsAdapter(GameNewsList.this, list);
	            
			 } catch(Exception e){
				 Log.e(MY_DEBUG_TAG, "GetSearchListTaskError", e);
				 return false;
			 }			    	 
	    	 
	         return true;
	     }

	     protected void onPostExecute(Boolean result) {
	    	 if(result){
	    		 GameNewsList.this.setListAdapter(mGameNewsAdapter);
	    		 mProgressDialog.dismiss();	    		 
	    		 
	    	 } else {
	    		 mProgressDialog.dismiss();
	    		 //Toast.makeText(GameSearchResults.this, "Retrieval Failure", Toast.LENGTH_SHORT).show();
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(GameNewsList.this);
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
             mProgressDialog = new ProgressDialog(GameNewsList.this);
             mProgressDialog.setMessage("Loading News...");
             mProgressDialog.setIndeterminate(true);
             mProgressDialog.setCancelable(true);
             mProgressDialog.show();
	     }
	 }
	 
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Intent i = new Intent(this, GameLoad.class);
        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        //i.putExtra("headline", mList.get(position).get("headline"));
        HashMap<String,String> selectedItem = (HashMap<String,String>)mGameNewsAdapter.getItem(position);
        //i.putExtra("url", mList.get(position).get("url"));
        String newsUrl = selectedItem.get("url");
        
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl)));
        //Intent i = new Intent(this, FeatureLoad.class);
        //i.putExtra("url", newsUrl);
        //startActivity(i);
    }		 	
}
