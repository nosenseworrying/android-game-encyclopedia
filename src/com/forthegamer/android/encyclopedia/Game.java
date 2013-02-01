package com.forthegamer.android.encyclopedia;

public class Game {
	private String mName = null;
	private String mID = null;
	private String mPlatform = null;
	private String mReleaseDate = null;
	private String mDescription = null;
	private String mGenre = null;
	private String mRating = null;
	private String mScoreID = null;
	private String mScoreName = null;
	private String mThumbURL = null;
	private String mOfficialSite = null;
	private String mNewsLink = null;
	private String mFeaturesLink = null;
	private String mReviewLink = null;
	private String mPreviewsLink = null;
	private String mCheatsLink = null;
	private String mScreenLink = null;
	
	public String getName(){
		return this.mName;
	}
	
	public void setName(String name){
		this.mName = name;
	}
	
	public String getID(){
		return this.mID;
	}
	
	public void setID(String id){
		this.mID = id;
	}
	
	public String getPlatform(){
		return this.mPlatform;
	}
	
	public void setPlatform(String platform){
		this.mPlatform = platform;
	}
	
	public String getSubstring(){
		if(mReleaseDate == null){
			return this.mPlatform;
		} else {
			return this.mPlatform + " - Release: " + mReleaseDate;
		}
		
	}
	
	public void setReleaseDate(String release){
		this.mReleaseDate = release;
	}
	
	public String getReleaseDate(){
		return this.mReleaseDate;
	}
	
	public void setDescription(String description){
		this.mDescription = description;
	}
	
	public String getDescription(){
		return this.mDescription;
	}
	
	public void setGenre(String genre){
		this.mGenre = genre;
	}
	
	public String getGenre(){
		return this.mGenre;
	}
	
	public void setRating(String rating){
		this.mRating = rating;
	}
	
	public String getRating(){
		return this.mRating;
	}
	
	public void setScoreID(String id){
		this.mScoreID = id;
	}
	
	public String getScoreID(){
		return this.mScoreID;
	}
	
	public void setScoreName(String name){
		this.mScoreName = name;
	}
	
	public String getScoreName(){
		return this.mScoreName;
	}
	
	public void setThumURL(String url){
		mThumbURL = url;
	}
	
	public String getThumbURL(){
		return this.mThumbURL;
	}
	
	public void setOfficialSite(String site){
		mOfficialSite = site;
	}
	
	public String getOfficialSite(){
		return this.mOfficialSite;
	}
	
	public void setNewsLink(String site){
		mNewsLink = site;
	}
	
	public String getNewsLink(){
		return this.mNewsLink;
	}
	
	public void setReviewLink(String site){
		mReviewLink = site;
	}
	
	public String getReviewLink(){
		return this.mReviewLink;
	}
	
	public void setScreenshotsLink(String site){
		mScreenLink = site;
	}
	
	public String getScreenshotsLink(){
		return this.mScreenLink;
	}
	
	public void setCheatsLink(String site){
		mCheatsLink = site;
	}
	
	public String getCheatsLink(){
		return this.mCheatsLink;
	}		
}
