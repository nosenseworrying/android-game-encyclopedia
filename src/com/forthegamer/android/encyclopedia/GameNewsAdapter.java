package com.forthegamer.android.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GameNewsAdapter extends BaseAdapter {
	private ArrayList<HashMap<String,String>> mGameNewsList;
	private Context mContext;
	
	public GameNewsAdapter(Context context, ArrayList<HashMap<String,String>> gameNewsList){
		mContext = context;
		mGameNewsList = gameNewsList;
	}	
	
	public int getCount() {
		return mGameNewsList.size();
	}

	public Object getItem(int index) {
		return mGameNewsList.get(index);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		FeaturesView fv;
		HashMap<String,String> newsItem = mGameNewsList.get(position);
		String newsHeadline = newsItem.get("headline");
		String newsSubstring = newsItem.get("substring");
		//String newsColor = newsItem.get("color");
		
        if (convertView == null) {
            fv = new FeaturesView(mContext, newsHeadline, newsSubstring);
        } else {
            fv = (FeaturesView) convertView;
            fv.setHeadline(newsHeadline);
            fv.setSubstring(newsSubstring);
            //fv.setColor(newsColor);
            //nv.setDate(newsDate);
        }

        return fv;
	}

}
