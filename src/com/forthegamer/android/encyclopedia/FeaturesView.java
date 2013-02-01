package com.forthegamer.android.encyclopedia;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class FeaturesView extends LinearLayout {
	//private LinearLayout mNewsLayout;
	//private LinearLayout mColorLayout;
	//private LinearLayout mSubstringLayout;
	//private TextView mColorView;
	private TextView mHeadlineView;
	private TextView mSubstringView;
	//private TextView mDateView;
	
	public FeaturesView (Context context, String headline, String substring){
		super(context);
		
		//this.setOrientation(HORIZONTAL);
		this.setOrientation(VERTICAL);
		LayoutParams wrapWrapParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LayoutParams fillFillParams = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		/*
		mColorLayout = new LinearLayout(context);
		mColorLayout.setOrientation(HORIZONTAL);
		mColorView = new TextView(context);
		mColorView.setWidth(6);
		*//*
		int tabColor = R.color.white;
		
		if(color.equalsIgnoreCase("red")){
			tabColor = R.color.red;
		} else if(color.equalsIgnoreCase("blue")){
			tabColor = R.color.blue;
		} else if(color.equalsIgnoreCase("green")){
			tabColor = R.color.green;
		} else if(color.equalsIgnoreCase("lightblue")){
			tabColor = R.color.lightblue;
		}*/
		/*
		mColorView.setBackgroundResource(tabColor);
		mColorLayout.addView(mColorView, fillFillParams);
		*/
		/*mNewsLayout = new LinearLayout(context);
		mNewsLayout.setOrientation(VERTICAL);
		*/
		mHeadlineView = new TextView(context);
		mHeadlineView.setText(headline);
		mHeadlineView.setTextColor(android.R.color.primary_text_light);
		mHeadlineView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		mHeadlineView.setPadding(7, 3, 0, 0);
		//mNewsLayout.addView(mHeadlineView, wrapWrapParams);
		
		mSubstringView = new TextView(context);
		mSubstringView.setText(substring);
		mSubstringView.setPadding(7, 1, 0, 2);
		
		//mNewsLayout.addView(mSubstringView, wrapWrapParams);
		
		this.addView(mHeadlineView, fillFillParams);
		this.addView(mSubstringView, fillFillParams);
		
		//this.addView(mColorLayout, fillFillParams);
		//this.addView(mNewsLayout, fillFillParams);
	}
	
	public void setHeadline(String headline){
		this.mHeadlineView.setText(headline);
	}
	
	public void setSubstring(String substring){
		this.mSubstringView.setText(substring);
	}
	
	/*public void setColor(String color){
		int tabColor = R.color.white;
		
		if(color.equalsIgnoreCase("red")){
			tabColor = R.color.red;
		} else if(color.equalsIgnoreCase("blue")){
			tabColor = R.color.blue;
		} else if(color.equalsIgnoreCase("green")){
			tabColor = R.color.green;
		} else if(color.equalsIgnoreCase("lightblue")){
			tabColor = R.color.lightblue;
		}
		
		this.mColorView.setBackgroundResource(tabColor);
	}*/
}
