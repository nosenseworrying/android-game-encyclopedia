package com.forthegamer.android.encyclopedia;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MidSelector extends ListActivity {
	private String[] mPlatformList={"PC", "PS2", "PS3", "PSP",  "Xbox", "Xbox 360", "Nintendo 64", 
			"GameCube", "Wii", "Nintendo DS", "Game Boy", "Game Boy Advance", "Dreamcast"};
	private String[] mUrlPlatforms={"pc", "ps2", "ps3", "psp", "xbox", "xbox360", "n64",
			"gc", "wii", "ds", "gameboy", "gba", "dc"};
	private String[] mGenreList={"Action", "Adventure", "Arcade", "Children's", "Family",
			"Fighting", "Flight", "Other Games/Compilations", "Puzzle", "Racing",
			"Role Playing", "Shooter", "Simulation", "Sports", "Strategy"};
	private String[] mNameList={"0-9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private String mSearchKey = null;
	private String[] mSearchList = null;
	private String mFirstKey = null;
	private boolean isPlatform = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.midselector_list);
        
        if(icicle != null){
        	mSearchKey = icicle.getString("key");
        }
        
        if(mSearchKey == null){
    		Bundle extras = getIntent().getExtras();
    		if(extras != null){
    			mSearchKey = extras.getString("key");
    		}	
        }
        
		if(mSearchKey.equals("platform")){
		   mSearchList = mUrlPlatforms;
		   mFirstKey = "platform";
		   isPlatform = true;
		} else if(mSearchKey.equals("genre")){
	        mSearchList = mGenreList;
	        mFirstKey = "genre";
		} else if(mSearchKey.equals("name")){
			mSearchList = mNameList;
			mFirstKey = "game_name";
		}
		
		if(isPlatform){
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mPlatformList));
		} else {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mSearchList));
		}
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mSearchKey != null){
			outState.putString("key", mSearchKey);
		}
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, GamesListActivity.class);
        String urlKey = mFirstKey + "=";
        String firstValue = mSearchList[position].toLowerCase();
        urlKey = urlKey + firstValue;
        i.putExtra("urlkey", urlKey);
        startActivity(i);
    }

}
