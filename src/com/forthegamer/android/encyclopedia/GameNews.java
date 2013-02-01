package com.forthegamer.android.encyclopedia;

public class GameNews {
	private String mHeadline;
	private String mStrapline;
	private String mPublishedDate;
	private String mURL;
	
	public void setHeadline(String headline){
		this.mHeadline = headline;
	}
	
	public String getHeadline(){
		return this.mHeadline;
	}
	
	public void setStrapline(String strapline){
		this.mStrapline = strapline;
	}
	
	public String getStrapline(){
		return this.mStrapline;
	}
	
	public void setPublishedDate(String date){
		this.mPublishedDate = date;
	}
	
	public String getPublishedDate(){
		return this.mPublishedDate;
	}
	
	public void setURL(String url){
		this.mURL = url;
	}
	
	public String getURL(){
		return this.mURL;
	}
	
	public String getSubstring(){
		return mStrapline + " - " + mPublishedDate;
	}
}
