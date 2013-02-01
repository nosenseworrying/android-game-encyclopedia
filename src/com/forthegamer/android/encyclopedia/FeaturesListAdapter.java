package com.forthegamer.android.encyclopedia;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class FeaturesListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String,String>> mFeaturesList;
	private Context mContext;
	
	public FeaturesListAdapter(Context context, ArrayList<HashMap<String,String>> featuresList){
		mContext = context;
		mFeaturesList = featuresList;
	}	
	
	public int getCount() {
		return mFeaturesList.size();
	}

	public Object getItem(int index) {
		return mFeaturesList.get(index);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void addItem(HashMap<String,String> newItem){
		mFeaturesList.add(newItem);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		FeaturesView fv;
		HashMap<String,String> newsItem = mFeaturesList.get(position);
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
