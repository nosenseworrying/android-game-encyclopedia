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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class GameBrowser extends ListActivity {
	// Go get the entire games list from GamesRadar and display it
	// Add menu options for "Platform Filter", "Genre Filter", "Name Filter"
	// For each of those menus, add a selector for "None" plus all possible values
		// None should be selected initially
	private String mBaseURL = "http://api.gamesradar.com/games?page_size=" + ROWS_PER_PAGE + "&api_key=[YOUR_KEY_HERE]";
	private String mCurrentURL = null;
	private String mPlatformFilter = null;
	private String mGenreFilter = null;
	private String mNameFilter = null;
	private String mSortFilter = "&sort=a-z";
	private int mPageNum = 1;
	private int mTotalItems = 0;
	private int mTotalPages = 0;
	private boolean mLoading = false;
	private boolean mAppendItems = false;
	private boolean mIsEmpty = false;
	private GamesListAdapter mGamesAdapter;
	private ProgressDialog mProgressDialog;
	private SubMenu mPlatformSubmenu;
	private SubMenu mGenreSubmenu;
	private SubMenu mNameSubmenu;
	private SubMenu mSortSubmenu;
	private TextView mEmptyView;
	private TextView mHeaderView;
	private String mHeaderPlatform = "All";
	private String mHeaderGenre = "All";
	private String mHeaderName = "All";
	private static final int ROWS_PER_PAGE = 20;
	private static final String MY_DEBUG_TAG = "GameBrowser";
	public static final int PLATFORM_MENU_GROUP = Menu.FIRST + 1;
	public static final int PLATFORM_ALL_ID = Menu.FIRST + 2;
	public static final int PLATFORM_PC_ID = Menu.FIRST + 3;
	public static final int PLATFORM_PS2_ID = Menu.FIRST + 4;
	public static final int PLATFORM_PS3_ID = Menu.FIRST + 5;
	public static final int PLATFORM_PSP_ID = Menu.FIRST + 6;
	public static final int PLATFORM_XBOX_ID = Menu.FIRST + 7;
	public static final int PLATFORM_XBOX360_ID = Menu.FIRST + 8;
	public static final int PLATFORM_N64_ID = Menu.FIRST + 9;
	public static final int PLATFORM_GAMECUBE_ID = Menu.FIRST + 10;
	public static final int PLATFORM_WII_ID = Menu.FIRST + 11;
	public static final int PLATFORM_DS_ID = Menu.FIRST + 12;
	public static final int PLATFORM_GAMEBOY_ID = Menu.FIRST + 13;
	public static final int PLATFORM_GBA_ID = Menu.FIRST + 14;
	public static final int PLATFORM_DREAMCAST_ID = Menu.FIRST + 15;
	public static final int PLATFORM_NES_ID = Menu.FIRST + 67;
	public static final int PLATFORM_SNES_ID = Menu.FIRST + 68;
	public static final int PLATFORM_GENESIS_ID = Menu.FIRST + 69;
	
	
	public static final int GENRE_MENU_GROUP = Menu.FIRST+16;
	public static final int GENRE_ALL_ID = Menu.FIRST+32;
	public static final int GENRE_ACTION_ID = Menu.FIRST+17;
	public static final int GENRE_ADVENTURE_ID = Menu.FIRST+18;
	public static final int GENRE_ARCADE_ID = Menu.FIRST+19;
	public static final int GENRE_CHILDRENS_ID = Menu.FIRST+20;
	public static final int GENRE_FAMILY_ID = Menu.FIRST+21;
	public static final int GENRE_FIGHTING_ID = Menu.FIRST+22;
	public static final int GENRE_FLIGHT_ID = Menu.FIRST+23;
	public static final int GENRE_OTHER_ID = Menu.FIRST+24;
	public static final int GENRE_PUZZLE_ID = Menu.FIRST+25;
	public static final int GENRE_RACING_ID = Menu.FIRST+26;
	public static final int GENRE_ROLEPLAYING_ID = Menu.FIRST+27;
	public static final int GENRE_SHOOTER_ID = Menu.FIRST+28;
	public static final int GENRE_SIMULATION_ID = Menu.FIRST+29;
	public static final int GENRE_SPORTS_ID = Menu.FIRST+30;
	public static final int GENRE_STRATEGY_ID = Menu.FIRST+31;
	
	public static final int NAME_MENU_GROUP = Menu.FIRST+33;
	public static final int NAME_ALL_ID = Menu.FIRST+34;
	public static final int NAME_09_ID = Menu.FIRST+35;
	public static final int NAME_A_ID = Menu.FIRST+36;
	public static final int NAME_B_ID = Menu.FIRST+37;
	public static final int NAME_C_ID = Menu.FIRST+38;
	public static final int NAME_D_ID = Menu.FIRST+39;
	public static final int NAME_E_ID = Menu.FIRST+40;
	public static final int NAME_F_ID = Menu.FIRST+41;
	public static final int NAME_G_ID = Menu.FIRST+42;
	public static final int NAME_H_ID = Menu.FIRST+43;
	public static final int NAME_I_ID = Menu.FIRST+44;
	public static final int NAME_J_ID = Menu.FIRST+45;
	public static final int NAME_K_ID = Menu.FIRST+46;
	public static final int NAME_L_ID = Menu.FIRST+47;
	public static final int NAME_M_ID = Menu.FIRST+48;
	public static final int NAME_N_ID = Menu.FIRST+49;
	public static final int NAME_O_ID = Menu.FIRST+50;
	public static final int NAME_P_ID = Menu.FIRST+51;
	public static final int NAME_Q_ID = Menu.FIRST+52;
	public static final int NAME_R_ID = Menu.FIRST+53;
	public static final int NAME_S_ID = Menu.FIRST+54;
	public static final int NAME_T_ID = Menu.FIRST+55;
	public static final int NAME_U_ID = Menu.FIRST+56;
	public static final int NAME_V_ID = Menu.FIRST+57;
	public static final int NAME_W_ID = Menu.FIRST+58;
	public static final int NAME_X_ID = Menu.FIRST+59;
	public static final int NAME_Y_ID = Menu.FIRST+60;
	public static final int NAME_Z_ID = Menu.FIRST+61;
	
	public static final int SORT_MENU_GROUP = Menu.FIRST+62;
	public static final int SORT_AZ_ID = Menu.FIRST+63;
	public static final int SORT_ZA_ID = Menu.FIRST+64;
	public static final int SORT_NEWEST_ID = Menu.FIRST+65;
	public static final int SORT_OLDEST_ID = Menu.FIRST+66;
	
	
	@Override
    public void onCreate(Bundle icicle) {
         super.onCreate(icicle);
         setContentView(R.layout.games_list);
         mHeaderView = (TextView)findViewById(R.id.txtHeader);
         mEmptyView = (TextView)findViewById(R.id.txtEmpty);
         if(icicle != null){
        	 mPlatformFilter = icicle.getString("platformfilter");
        	 mGenreFilter = icicle.getString("genrefilter");
        	 mNameFilter = icicle.getString("namefilter");
        	 mSortFilter = icicle.getString("sortfilter");
        	 mPageNum = icicle.getInt("pagenum");
         }         
         
         if(!mLoading){
        	 new GetGamesListTask().execute();
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
			        			 new GetGamesListTask().execute();
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
	
	 private class GetGamesListTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... unused) {
	    	 mLoading = true;
	    	 if(!mAppendItems){
	    		 mPageNum = 1;
	    	 }
	    	
	 		constructAPIUrl();
			
			 try {
				 URL apiURL = new URL(mCurrentURL);
				 
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
	             ArrayList<HashMap<String,String>> list = myGamesHandler.getList();
	             int listSize = list.size();
	             if(listSize == 0){
	            	 mIsEmpty = true;
	             } else {
	            	 mIsEmpty = false;
	             }
	             
	             if(mAppendItems){
	            	 int numNewItems = list.size();
	            	 for(int i = 0; i < numNewItems; i++){
	            		 mGamesAdapter.addItem(list.get(i));
	            	 }
	             } else {
	            	 mGamesAdapter = new GamesListAdapter(GameBrowser.this, list);
	             }
	            
	             
	             // Set the total number of rows and pages
	             mTotalItems = myGamesHandler.getTotalNumRows();
	             mTotalPages = (int) Math.ceil((double) mTotalItems / (double) ROWS_PER_PAGE);
				 
			 } catch(Exception e){
				 Log.e(MY_DEBUG_TAG, "GamesQueryError", e);
				 return false;
			 }			    	 
	    	 
	         return true;
	     }

	     protected void onPostExecute(Boolean result) {
	    	 if(result){
	    		 String headerText = "";
	    		 boolean firstItemSet = false;
	    		 boolean platformAll = false;
	    		 boolean genreAll = false;
	    		 boolean nameAll = false;
	    		 
	    		 if(mHeaderPlatform.equals("All")){
	    			 platformAll = true;
	    		 }
	    		 
	    		 if(mHeaderGenre.equals("All")){
	    			 genreAll = true;
	    		 }
	    		 
	    		 if(mHeaderName.equals("All")){
	    			 nameAll = true;
	    		 }
	    		 
	    		 if(platformAll && genreAll && nameAll){
	    			 headerText = "Press 'Menu' to Filter Games";
	    		 } else {
	    			 headerText = "Filter: ";
	    			 if(!platformAll){
	    				 headerText = headerText + mHeaderPlatform;
	    				 firstItemSet = true;
	    			 }
	    			 
	    			 if(!genreAll){
	    				 if(firstItemSet){
	    					 headerText = headerText + ", ";
	    				 }
	    				 headerText = headerText + mHeaderGenre;
	    				 firstItemSet = true;
	    			 }
	    			 
	    			 if(!nameAll){
	    				 if(firstItemSet){
	    					 headerText = headerText + ", ";
	    				 }
	    				 headerText = headerText + mHeaderName;
	    				 firstItemSet = true;
	    			 }
	    		 }
	    		 mHeaderView.setText(headerText);
	    		 if(mAppendItems){
	    			 mAppendItems = false;
	    			 mGamesAdapter.notifyDataSetChanged();
	    		 } else {
		    		 if(mIsEmpty){
		    			 mEmptyView.setVisibility(View.VISIBLE);
		    		 } else {
		    			 mEmptyView.setVisibility(View.GONE);
		    		 }
		    		 GameBrowser.this.setListAdapter(mGamesAdapter);
	    		 }
	    		 mProgressDialog.dismiss();
	    		 mLoading = false;	    		 
	    	 } else {
	    		 mProgressDialog.dismiss();
	    		 //Toast.makeText(GameBrowser.this, "Retrieval Failure", Toast.LENGTH_SHORT).show();
	    		 
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(GameBrowser.this);
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
	    			 new GetGamesListTask().execute();
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
             mProgressDialog = new ProgressDialog(GameBrowser.this);
             mProgressDialog.setMessage("Loading Games...");
             mProgressDialog.setIndeterminate(true);
             mProgressDialog.setCancelable(true);
             mProgressDialog.show();
	     }
	 }	
	
	public void constructAPIUrl(){
		mCurrentURL = mBaseURL;
		mCurrentURL = mCurrentURL + "&page_num=" + mPageNum;
		mCurrentURL = mCurrentURL + mSortFilter;
		
		if(mPlatformFilter != null){
			mCurrentURL = mCurrentURL + mPlatformFilter;
		}
		
		if(mGenreFilter != null){
			mCurrentURL = mCurrentURL + mGenreFilter;
		}
		
		if(mNameFilter != null){
			mCurrentURL = mCurrentURL + mNameFilter;
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mPlatformFilter != null){
			outState.putString("platformfilter", mPlatformFilter);
		}
		if(mGenreFilter != null){
			outState.putString("genrefilter", mGenreFilter);
		}
		if(mNameFilter != null){
			outState.putString("namefilter", mNameFilter);
		}
		
		outState.putString("sortfilter", mSortFilter);
		outState.putInt("pagenum", mPageNum);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return(super.onCreateOptionsMenu(menu));
	}	
	
	private void populateMenu(Menu menu) {
		mPlatformSubmenu = menu.addSubMenu("Platform");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_ALL_ID, 0, "All");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_PC_ID, 0, "PC");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_PS2_ID, 0, "PS2");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_PS3_ID, 0, "PS3");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_PSP_ID, 0, "PSP");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_XBOX_ID, 0, "XBox");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_XBOX360_ID, 0, "XBox 360");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_N64_ID, 0, "Nintendo 64");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_GAMECUBE_ID, 0, "GameCube");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_WII_ID, 0, "Wii");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_DS_ID, 0, "Nintendo DS");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_GAMEBOY_ID, 0, "Game Boy");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_GBA_ID, 0, "Game Boy Advance");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_DREAMCAST_ID, 0, "Dreamcast");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_NES_ID, 0, "Nintendo");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_SNES_ID, 0, "Super Nintendo");
		mPlatformSubmenu.add(PLATFORM_MENU_GROUP, PLATFORM_GENESIS_ID, 0, "Genesis");
		mPlatformSubmenu.setGroupCheckable(PLATFORM_MENU_GROUP, true, true);
		mPlatformSubmenu.getItem(0).setChecked(true);
		
		mGenreSubmenu = menu.addSubMenu("Genre");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_ALL_ID, 0, "All");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_ACTION_ID, 0, "Action");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_ADVENTURE_ID, 0, "Adventure");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_ARCADE_ID, 0, "Arcade");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_CHILDRENS_ID, 0, "Children's");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_FAMILY_ID, 0, "Family");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_FIGHTING_ID, 0, "Fighting");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_FLIGHT_ID, 0, "Flight");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_OTHER_ID, 0, "Other/Compilations");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_PUZZLE_ID, 0, "Puzzle");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_RACING_ID, 0, "Racing");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_ROLEPLAYING_ID, 0, "Role Playing");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_SHOOTER_ID, 0, "Shooter");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_SIMULATION_ID, 0, "Simulation");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_SPORTS_ID, 0, "Sports");
		mGenreSubmenu.add(GENRE_MENU_GROUP, GENRE_STRATEGY_ID, 0, "Strategy");
		mGenreSubmenu.setGroupCheckable(GENRE_MENU_GROUP, true, true);
		mGenreSubmenu.getItem(0).setChecked(true);
		
		mNameSubmenu = menu.addSubMenu("Name");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_ALL_ID, 0, "All");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_09_ID, 0, "0-9");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_A_ID, 0, "A");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_B_ID, 0, "B");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_C_ID, 0, "C");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_D_ID, 0, "D");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_E_ID, 0, "E");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_F_ID, 0, "F");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_G_ID, 0, "G");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_H_ID, 0, "H");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_I_ID, 0, "I");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_J_ID, 0, "J");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_K_ID, 0, "K");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_L_ID, 0, "L");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_M_ID, 0, "M");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_N_ID, 0, "N");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_O_ID, 0, "O");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_P_ID, 0, "P");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_Q_ID, 0, "Q");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_R_ID, 0, "R");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_S_ID, 0, "S");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_T_ID, 0, "T");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_U_ID, 0, "U");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_V_ID, 0, "V");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_W_ID, 0, "W");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_X_ID, 0, "X");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_Y_ID, 0, "Y");
		mNameSubmenu.add(NAME_MENU_GROUP, NAME_Z_ID, 0, "Z");
		mNameSubmenu.setGroupCheckable(NAME_MENU_GROUP, true, true);
		mNameSubmenu.getItem(0).setChecked(true);
		
		mSortSubmenu = menu.addSubMenu("Sort");
		mSortSubmenu.add(SORT_MENU_GROUP, SORT_AZ_ID, 0, "A to Z");
		mSortSubmenu.add(SORT_MENU_GROUP, SORT_ZA_ID, 0, "Z to A");
		mSortSubmenu.add(SORT_MENU_GROUP, SORT_NEWEST_ID, 0, "Recently Added");
		//mSortSubmenu.add(SORT_MENU_GROUP, SORT_OLDEST_ID, 0, "Oldest");
		mSortSubmenu.setGroupCheckable(SORT_MENU_GROUP, true, true);
		mSortSubmenu.getItem(0).setChecked(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return(applyMenuChoice(item) ||
		super.onOptionsItemSelected(item));
	}
	
	private boolean applyMenuChoice(MenuItem item){
		switch (item.getItemId()) {
			case PLATFORM_ALL_ID:
				mPlatformFilter = null;
				mHeaderPlatform = "All";
				mPlatformSubmenu.getItem(0).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_PC_ID:
				mPlatformFilter = "&platform=pc";
				mHeaderPlatform = "PC";
				mPlatformSubmenu.getItem(1).setChecked(true);
				new GetGamesListTask().execute();
				return true;
			
			case PLATFORM_PS2_ID:
				mPlatformFilter = "&platform=ps2";
				mHeaderPlatform = "PS2";
				mPlatformSubmenu.getItem(2).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_PS3_ID:
				mPlatformFilter = "&platform=ps3";
				mHeaderPlatform = "PS3";
				mPlatformSubmenu.getItem(3).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_PSP_ID:
				mPlatformFilter = "&platform=psp";
				mHeaderPlatform = "PSP";
				mPlatformSubmenu.getItem(4).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_XBOX_ID:
				mPlatformFilter = "&platform=xbox";
				mHeaderPlatform = "Xbox";
				mPlatformSubmenu.getItem(5).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_XBOX360_ID:
				mPlatformFilter = "&platform=xbox360";
				mHeaderPlatform = "Xbox 360";
				mPlatformSubmenu.getItem(6).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_N64_ID:
				mPlatformFilter = "&platform=n64";
				mHeaderPlatform = "N64";
				mPlatformSubmenu.getItem(7).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_GAMECUBE_ID:
				mPlatformFilter = "&platform=gc";
				mHeaderPlatform = "GameCube";
				mPlatformSubmenu.getItem(8).setChecked(true);
				new GetGamesListTask().execute();
				return true;				

			case PLATFORM_WII_ID:
				mPlatformFilter = "&platform=wii";
				mHeaderPlatform = "Wii";
				mPlatformSubmenu.getItem(9).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_DS_ID:
				mPlatformFilter = "&platform=ds";
				mHeaderPlatform = "DS";
				mPlatformSubmenu.getItem(10).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_GAMEBOY_ID:
				mPlatformFilter = "&platform=gameboy";
				mHeaderPlatform = "Game Boy";
				mPlatformSubmenu.getItem(11).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_GBA_ID:
				mPlatformFilter = "&platform=gba";
				mHeaderPlatform = "GBA";
				mPlatformSubmenu.getItem(12).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_DREAMCAST_ID:
				mPlatformFilter = "&platform=dc";
				mHeaderPlatform = "Dreamcast";
				mPlatformSubmenu.getItem(13).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_NES_ID:
				mPlatformFilter = "&platform=nes";
				mHeaderPlatform = "Nintendo";
				mPlatformSubmenu.getItem(14).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_SNES_ID:
				mPlatformFilter = "&platform=snes";
				mHeaderPlatform = "Super Nintendo";
				mPlatformSubmenu.getItem(15).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case PLATFORM_GENESIS_ID:
				mPlatformFilter = "&platform=genesis";
				mHeaderPlatform = "Genesis";
				mPlatformSubmenu.getItem(16).setChecked(true);
				new GetGamesListTask().execute();
				return true;				
			
			case GENRE_ALL_ID:
				mGenreFilter = null;
				mHeaderGenre = "All";
				mGenreSubmenu.getItem(0).setChecked(true);
				new GetGamesListTask().execute();
				return true;	
				
			case GENRE_ACTION_ID:
				mGenreFilter = "&genre=action";
				mHeaderGenre = "Action";
				mGenreSubmenu.getItem(1).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_ADVENTURE_ID:
				mGenreFilter = "&genre=adventure";
				mHeaderGenre = "Adventure";
				mGenreSubmenu.getItem(2).setChecked(true);
				new GetGamesListTask().execute();
				return true;
			
			case GENRE_ARCADE_ID:
				mGenreFilter = "&genre=arcade";
				mHeaderGenre = "Arcade";
				mGenreSubmenu.getItem(3).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_CHILDRENS_ID:
				mGenreFilter = "&genre=children's";
				mHeaderGenre = "Children";
				mGenreSubmenu.getItem(4).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_FAMILY_ID:
				mGenreFilter = "&genre=family";
				mHeaderGenre = "Family";
				mGenreSubmenu.getItem(5).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_FIGHTING_ID:
				mGenreFilter = "&genre=fighting";
				mHeaderGenre = "Fighting";
				mGenreSubmenu.getItem(6).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_FLIGHT_ID:
				mGenreFilter = "&genre=flight";
				mHeaderGenre = "Flight";
				mGenreSubmenu.getItem(7).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_OTHER_ID:
				mGenreFilter = "&genre=other%20games/compilations";
				mHeaderGenre = "Other";
				mGenreSubmenu.getItem(8).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_PUZZLE_ID:
				mGenreFilter = "&genre=puzzle";
				mHeaderGenre = "Puzzle";
				mGenreSubmenu.getItem(9).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_RACING_ID:
				mGenreFilter = "&genre=racing";
				mHeaderGenre = "Racing";
				mGenreSubmenu.getItem(10).setChecked(true);
				new GetGamesListTask().execute();
				return true;	
				
			case GENRE_ROLEPLAYING_ID:
				mGenreFilter = "&genre=role%20playing";
				mHeaderGenre = "RP";
				mGenreSubmenu.getItem(11).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_SHOOTER_ID:
				mGenreFilter = "&genre=shooter";
				mHeaderGenre = "Shooter";
				mGenreSubmenu.getItem(12).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_SIMULATION_ID:
				mGenreFilter = "&genre=simulation";
				mHeaderGenre = "Sim";
				mGenreSubmenu.getItem(13).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_SPORTS_ID:
				mGenreFilter = "&genre=sport%20games";
				mHeaderGenre = "Sports";
				mGenreSubmenu.getItem(14).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case GENRE_STRATEGY_ID:
				mGenreFilter = "&genre=strategy";
				mHeaderGenre = "Strategy";
				mGenreSubmenu.getItem(15).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_ALL_ID:
				mNameFilter = null;
				mHeaderName = "All";
				mNameSubmenu.getItem(0).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_09_ID:
				mNameFilter = "&game_name=0-9";
				mHeaderName = "0-9";
				mNameSubmenu.getItem(1).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_A_ID:
				mNameFilter = "&game_name=a";
				mHeaderName = "A";
				mNameSubmenu.getItem(2).setChecked(true);
				new GetGamesListTask().execute();
				return true;
			
			case NAME_B_ID:
				mNameFilter = "&game_name=b";
				mHeaderName = "B";
				mNameSubmenu.getItem(3).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_C_ID:
				mNameFilter = "&game_name=c";
				mHeaderName = "C";
				mNameSubmenu.getItem(4).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_D_ID:
				mNameFilter = "&game_name=d";
				mHeaderName = "D";
				mNameSubmenu.getItem(5).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_E_ID:
				mNameFilter = "&game_name=e";
				mHeaderName = "E";
				mNameSubmenu.getItem(6).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_F_ID:
				mNameFilter = "&game_name=f";
				mHeaderName = "F";
				mNameSubmenu.getItem(7).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_G_ID:
				mNameFilter = "&game_name=g";
				mHeaderName = "G";
				mNameSubmenu.getItem(8).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_H_ID:
				mNameFilter = "&game_name=h";
				mHeaderName = "H";
				mNameSubmenu.getItem(9).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_I_ID:
				mNameFilter = "&game_name=i";
				mHeaderName = "I";
				mNameSubmenu.getItem(10).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_J_ID:
				mNameFilter = "&game_name=j";
				mHeaderName = "J";
				mNameSubmenu.getItem(11).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_K_ID:
				mNameFilter = "&game_name=k";
				mHeaderName = "K";
				mNameSubmenu.getItem(12).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_L_ID:
				mNameFilter = "&game_name=l";
				mHeaderName = "L";
				mNameSubmenu.getItem(13).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_M_ID:
				mNameFilter = "&game_name=m";
				mHeaderName = "M";
				mNameSubmenu.getItem(14).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_N_ID:
				mNameFilter = "&game_name=n";
				mHeaderName = "N";
				mNameSubmenu.getItem(15).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_O_ID:
				mNameFilter = "&game_name=o";
				mHeaderName = "O";
				mNameSubmenu.getItem(16).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_P_ID:
				mNameFilter = "&game_name=p";
				mHeaderName = "P";
				mNameSubmenu.getItem(17).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_Q_ID:
				mNameFilter = "&game_name=q";
				mHeaderName = "Q";
				mNameSubmenu.getItem(18).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_R_ID:
				mNameFilter = "&game_name=r";
				mHeaderName = "R";
				mNameSubmenu.getItem(19).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_S_ID:
				mNameFilter = "&game_name=s";
				mHeaderName = "S";
				mNameSubmenu.getItem(20).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_T_ID:
				mNameFilter = "&game_name=t";
				mHeaderName = "T";
				mNameSubmenu.getItem(21).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_U_ID:
				mNameFilter = "&game_name=u";
				mHeaderName = "U";
				mNameSubmenu.getItem(22).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_V_ID:
				mNameFilter = "&game_name=v";
				mHeaderName = "V";
				mNameSubmenu.getItem(23).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_W_ID:
				mNameFilter = "&game_name=w";
				mHeaderName = "W";
				mNameSubmenu.getItem(24).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_X_ID:
				mNameFilter = "&game_name=x";
				mHeaderName = "X";
				mNameSubmenu.getItem(25).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_Y_ID:
				mNameFilter = "&game_name=y";
				mHeaderName = "Y";
				mNameSubmenu.getItem(26).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case NAME_Z_ID:
				mNameFilter = "&game_name=z";
				mHeaderName = "Z";
				mNameSubmenu.getItem(27).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case SORT_AZ_ID:
				mSortFilter = "&sort=a-z";
				mSortSubmenu.getItem(0).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case SORT_ZA_ID:
				mSortFilter = "&sort=z-a";
				mSortSubmenu.getItem(1).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case SORT_NEWEST_ID:
				mSortFilter = "&sort=newest";
				mSortSubmenu.getItem(2).setChecked(true);
				new GetGamesListTask().execute();
				return true;
				
			case SORT_OLDEST_ID:
				mSortFilter = "&sort=oldest";
				mSortSubmenu.getItem(3).setChecked(true);
				new GetGamesListTask().execute();
				return true;					
		}
		
		return false;
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, GameLoad.class);
        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        //i.putExtra("headline", mList.get(position).get("headline"));
        HashMap<String,String> selectedItem = (HashMap<String,String>)mGamesAdapter.getItem(position);
        //i.putExtra("url", mList.get(position).get("url"));
        i.putExtra("gameid", selectedItem.get("gameid"));
        i.putExtra("gamename", selectedItem.get("gamename"));
        startActivity(i);
    }	
}
