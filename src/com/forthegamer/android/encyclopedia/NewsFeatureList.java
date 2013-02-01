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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class NewsFeatureList extends ListActivity {
	private String mApiUrl = "http://api.gamesradar.com/features?page_size=" + ROWS_PER_PAGE + "&api_key=[YOUR_KEY_HERE]";
	private int mPageNum = 1;
	private int mTotalPages = 0;
	private int mTotalItems = 0;
	private boolean mLoading = false;
	private boolean mAppendItems = false;
	private boolean mIsEmpty = false;
	private FeaturesListAdapter mFeaturesAdapter;
	private static final int ROWS_PER_PAGE = 20;
	private static final String MY_DEBUG_TAG = "NewsFeatureList";
	private ProgressDialog mProgressDialog;
	private TextView mEmptyView;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.features_list);
        mEmptyView = (TextView)findViewById(R.id.txtEmpty);
        
        if(icicle != null){
        	mPageNum = icicle.getInt("pagenum");
        }
        
        if(!mLoading){
        	new GetFeaturesListTask().execute();
        }
        
        ListView ourList = this.getListView();
        ourList.setOnScrollListener(new OnScrollListener(){
       	 private int priorFirst = -1;
       	 public void onScroll(final AbsListView view, final int first, final int visible, final int total) {
	        	 // detect if last item is visible
	        	 if (visible < total && (first + visible == total)) {
		        	 // see if we have more results
		        	 if (first != priorFirst) {
			        	 priorFirst = first;
			        	 if(mPageNum < mTotalPages){
			        		 //Toast.makeText(GameBrowser.this, "End of the list!", Toast.LENGTH_SHORT).show();
			        		 mPageNum = mPageNum + 1;
			        		 mAppendItems = true;
			        		 if(!mLoading){
			        			 new GetFeaturesListTask().execute();
			        		 }			        		
			        	 }
		        	 }
	        	 }
       	 }
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}       	 
        });        
	}
	
	private class GetFeaturesListTask extends AsyncTask<Void, Void, Boolean> {
		protected Boolean doInBackground(Void... unused) {
			mLoading = true;
			if(!mAppendItems){
				mPageNum = 1;
			}
			
			String currentUrl = mApiUrl + "&page_num=" + mPageNum;
			
			 try {
				 URL apiURL = new URL(currentUrl);
				 
	             /* Get a SAXParser from the SAXPArserFactory. */
	             SAXParserFactory spf = SAXParserFactory.newInstance();
	             SAXParser sp = spf.newSAXParser();

	             /* Get the XMLReader of the SAXParser we created. */
	             XMLReader xr = sp.getXMLReader();
	             /* Create a new ContentHandler and apply it to the XML-Reader*/
	             FeaturesListHandler myFeaturesHandler = new FeaturesListHandler();
	             xr.setContentHandler(myFeaturesHandler);
	             
	             /* Parse the xml-data from our URL. */
	             xr.parse(new InputSource(apiURL.openStream()));
	             /* Parsing has finished. */
	             	             
	             /* Our ExampleHandler now provides the parsed data to us. */             
	             ArrayList<HashMap<String,String>> list = myFeaturesHandler.getList();
	             int listSize = list.size();
	             if(listSize == 0){
	            	 mIsEmpty = true;
	             } else {
	            	 mIsEmpty = false;
	             }
	             
	             if(mAppendItems){
	            	 int numNewItems = list.size();
	            	 for(int i = 0; i < numNewItems; i++){
	            		 mFeaturesAdapter.addItem(list.get(i));
	            	 }
	             } else {
	            	 mFeaturesAdapter = new FeaturesListAdapter(NewsFeatureList.this, list);
	             }
	            
	             
	             // Set the total number of rows and pages
	             mTotalItems = myFeaturesHandler.getTotalNumRows();
	             mTotalPages = (int) Math.ceil((double) mTotalItems / (double) ROWS_PER_PAGE);
				 
			 } catch(Exception e){
				 Log.e(MY_DEBUG_TAG, "FeaturesQueryError", e);
				 return false;
			 }			 
			
			
			return true;
		}
		
	     protected void onPreExecute (){
             mProgressDialog = new ProgressDialog(NewsFeatureList.this);
             mProgressDialog.setMessage("Loading News...");
             mProgressDialog.setIndeterminate(true);
             mProgressDialog.setCancelable(true);
             mProgressDialog.show();
	     }
	     
	     protected void onPostExecute(Boolean result) {
	    	 if(result){
	    		 if(mAppendItems){
	    			 mAppendItems = false;
	    			 mFeaturesAdapter.notifyDataSetChanged();
	    		 } else {
		    		 if(mIsEmpty){
		    			 mEmptyView.setVisibility(View.VISIBLE);
		    		 } else {
		    			 mEmptyView.setVisibility(View.GONE);
		    		 }
		    		 NewsFeatureList.this.setListAdapter(mFeaturesAdapter);
	    		 }
	    		 mProgressDialog.dismiss();
	    		 mLoading = false;    		 
	    		 
	    	 } else {
	    		 mProgressDialog.dismiss();
	    		 //Toast.makeText(NewsFeatureList.this, "Retrieval Failure", Toast.LENGTH_SHORT).show();
	    		 
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeatureList.this);
	    		 builder.setCancelable(true);
	    		 //builder.setIcon(R.drawable.dialog_question);
	    		 builder.setTitle("Retrieval Failure");
	    		 builder.setMessage("Please check your network connection and try again.");
	    		 builder.setInverseBackgroundForced(true);
	    		 builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
	    		   public void onClick(DialogInterface dialog, int which) {
	    			 if(mPageNum > 1){
	    				 mAppendItems = true;
	    			 }
	    			 new GetFeaturesListTask().execute();
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
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("pagenum", mPageNum);
	}	
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, FeatureLoad.class);
        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        //i.putExtra("headline", mList.get(position).get("headline"));
        HashMap<String,String> selectedItem = (HashMap<String,String>)mFeaturesAdapter.getItem(position);
        //i.putExtra("url", mList.get(position).get("url"));
        i.putExtra("url", selectedItem.get("url"));
        startActivity(i);
    }
}
