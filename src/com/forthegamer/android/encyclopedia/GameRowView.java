package com.forthegamer.android.encyclopedia;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class GameRowView extends LinearLayout {
	private TextView mNameView;
	private TextView mSubstringView;
	
	public GameRowView (Context context, String gameName, String gameSubstring){
		super(context);
		/*View row = findViewById(R.layout.game_row);
		mNameView = (TextView) row.findViewById(R.id.gameName);
		mSubstringView = (TextView) row.findViewById(R.id.gameID);
		
		mNameView.setText(gameName);
		mSubstringView.setText(gameID);
		
		this.addView(row);*/
		
		this.setOrientation(VERTICAL);
		LayoutParams fillFillParams = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		mNameView = new TextView(context);
		mNameView.setText(gameName);
		mNameView.setTextColor(android.R.color.primary_text_light);
		mNameView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		mNameView.setPadding(7, 3, 0, 0);
		
		mSubstringView = new TextView(context);
		mSubstringView.setText(gameSubstring);
		mSubstringView.setPadding(7, 1, 0, 2);
		
		this.addView(mNameView, fillFillParams);
		this.addView(mSubstringView, fillFillParams);
		
	}
	
	public void setName(String name){
		mNameView.setText(name);
	}
	
	public void setSubstring(String id){
		mSubstringView.setText(id);
	}
}
