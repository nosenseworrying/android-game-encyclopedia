package com.forthegamer.android.encyclopedia;

public class Feature {
	private String extractedName = null;
	private String extractedHeadline = null;
	private String extractedPlatform = null;
	private String tabColor = null;
	private String extractedDate = null;
	private String extractedStrapline = null;
	private String extractedURL = null;
	
	public String getExtractedName() {
		return extractedName;
	}
	
	public void setExtractedName(String name){
		this.extractedName = name;
	}
	
	public String getExtractedHeadline(){
		return extractedHeadline;
	}
	
	public void setExtractedHeadline(String headline){
		this.extractedHeadline = headline;
	}
	
	public String getExtractedPlatform(){
		return this.extractedPlatform;
	}
	
	public void setExtractedPlatform(String platform){
		this.extractedPlatform = platform;
	}
	
	public String getOutputString(){
		return extractedHeadline + " : " + extractedName + " : " + extractedPlatform;
		//return "fake output!";
	}
	
	public String getSubstring(){
		return extractedStrapline + " - " + extractedDate;
	}
	
	public void setTabColor(String color){
		this.tabColor = color;
	}
	
	public String getTabColor(){
		return tabColor;
	}
	
	public void setExtractedDate(String date){
		this.extractedDate = date;
	}
	
	public String getExtractedDate(){
		return extractedDate;
	}
	
	public void setExtractedStrapline(String strapline){
		this.extractedStrapline = strapline;
	}
	
	public String getExtractedStrapline(){
		return extractedStrapline;
	}
	
	public void setExtractedURL(String url){
		this.extractedURL = url;
	}
	
	public String getExtractedURL(){
		return extractedURL;
	}
}
