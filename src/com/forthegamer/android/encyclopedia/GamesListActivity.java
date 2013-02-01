package com.forthegamer.android.encyclopedia;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

public class GamesListActivity extends ListActivity {
	private String mUrlKey = null;
	private static final String MY_DEBUG_TAG = "GamesListActivity";
	
	@Override
    public void onCreate(Bundle icicle) {
         super.onCreate(icicle);
         setContentView(R.layout.games_list);
         
         if(icicle != null){
        	 mUrlKey = icicle.getString("urlkey");
         }
         
         if(mUrlKey == null){
        	 Bundle extras = getIntent().getExtras();
        	 if(extras != null){
        		 mUrlKey = extras.getString("urlkey");
        	 }
         }
         
         getGamesList(mUrlKey);
    }
	
	private void getGamesList(String urlKey){
		String baseURL = "http://api.gamesradar.com/games?page_size=20&sort=a-z&api_key=[YOUR_KEY_HERE]";
		String searchURL = baseURL + "&" + urlKey;
		 try {
			 URL apiURL = new URL(searchURL);
			 
             /* Get a SAXParser from the SAXPArserFactory. */
             SAXParserFactory spf = SAXParserFactory.newInstance();
             SAXParser sp = spf.newSAXParser();

             /* Get the XMLReader of the SAXParser we created. */
             XMLReader xr = sp.getXMLReader();
             /* Create a new ContentHandler and apply it to the XML-Reader*/
             GamesListHandler myGamesHandler = new GamesListHandler();
             xr.setContentHandler(myGamesHandler);
             
             /* Parse the xml-data from our URL. */
             xr.parse(new InputSource(apiURL.openStream()));
             /* Parsing has finished. */

             /* Our ExampleHandler now provides the parsed data to us. */
             //ParsedNewsDataSet parsedNewsDataSet =
             //                              myNewsHandler.getParsedData();
             
             ArrayList<HashMap<String,String>> list = myGamesHandler.getList();
             GamesListAdapter games = new GamesListAdapter(this, list);
             
             setListAdapter(games);
			 
		 } catch(Exception e){
			 Log.e(MY_DEBUG_TAG, "GamesQueryError", e);
		 }
	}
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mUrlKey != null){
			outState.putString("urlkey", mUrlKey);
		}
	}	
	/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, GameLoad.class);
        i.putExtra("gameid", mList.get(position).get("url"));
        startActivity(i);
    }*/
}
