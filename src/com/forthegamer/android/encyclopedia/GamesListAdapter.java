package com.forthegamer.android.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GamesListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String,String>> mGamesList;
	private Context mContext;
	
	public GamesListAdapter(Context context, ArrayList<HashMap<String,String>> gamesList){
		mContext = context;
		mGamesList = gamesList;
	}
	
	public int getCount() {
		return mGamesList.size();
	}

	public Object getItem(int position) {
		return mGamesList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void addItem(HashMap<String,String> newItem){
		mGamesList.add(newItem);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		GameRowView gv;
		HashMap<String,String> gameItem = mGamesList.get(position);
		String gameName = gameItem.get("gamename");
		//String gameID = gameItem.get("gameid");
		String substring = gameItem.get("substring");
		
        if (convertView == null) {
           gv = new GameRowView(mContext, gameName, substring);
        } else {
            gv = (GameRowView) convertView;
            gv.setName(gameName);
            gv.setSubstring(substring);
        }

        return gv;
	}

}
