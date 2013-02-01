package com.forthegamer.android.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GameHandler extends DefaultHandler {
	private final String MY_DEBUG_TAG = "GameHandler";
	private boolean mInGame = false;
	private boolean mInDescription = false;
	private boolean mInGenre = false;
	private boolean mInGenreName = false;
	private boolean mInCensorship = false;
	private boolean mInESRB = false;
	private boolean mInRating = false;
	private boolean mInScore = false;
	private boolean mInScoreID = false;
	private boolean mInScoreName = false;
	private boolean mInImages = false;
	private boolean mInThumbnail = false;
	private boolean mInPlatform = false;
	private boolean mInPlatformName = false;
	private boolean mInRelease = false;
	private boolean mInReleaseUS = false;
	private boolean mInOfficialSite = false;
	private boolean mInOfficialSiteUS = false;
	private boolean mInLinks = false;
	private boolean mInLinksNews = false;
	private boolean mInLinksReview = false;
	private boolean mInLinksScreen = false;
	private boolean mInLinksCheats = false;
	private boolean mKeepData = false;
	String mCurrentValue = "";
	private Game mGame;
	private HashMap<String,String> mItem;

	@Override
	public void startDocument() throws SAXException {
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName.equals("game")){
			mInGame = true;
			mItem = new HashMap<String,String>();
			mGame = new Game();	
		} else if(localName.equals("description")){
			mInDescription = true;
			mKeepData = true;
		} else if(localName.equals("genre")){
			mInGenre = true;
		} else if(localName.equals("name") && mInGenre){
			mInGenreName = true;
			mKeepData = true;
		} else if(localName.equals("censorship")){
			mInCensorship = true;
		} else if(localName.equals("esrb") && mInCensorship){
			mInESRB = true;
		} else if(localName.equals("rating") && mInCensorship && mInESRB){
			mInRating = true;
			mKeepData = true;
		} else if(localName.equals("score")){
			mInScore = true;
		} else if(localName.equals("id") && mInScore){
			mInScoreID = true;
			mKeepData = true;
		} else if(localName.equals("name") && mInScore){
			mInScoreName = true;
			mKeepData = true;
		} else if(localName.equals("images")){
			mInImages = true;
		} else if(localName.equals("thumbnail") && mInImages){
			mInThumbnail = true;
			mKeepData = true;
		} else if(localName.equals("platform")){
			mInPlatform = true;
		} else if(localName.equals("name") && mInPlatform){
			mInPlatformName = true;
			mKeepData = true;
		} else if(localName.equals("release_date") || localName.equals("expected_release_date")){
			mInRelease = true;
		} else if(localName.equals("us") && mInRelease){
			mInReleaseUS = true;
			mKeepData = true;
		} else if(localName.equals("official_site")){
			mInOfficialSite = true;
		} else if(localName.equals("us") && mInOfficialSite){
			mInOfficialSiteUS = true;
			mKeepData = true;
		} else if(localName.equals("links")){
			mInLinks = true;
		} else if(localName.equals("news") && mInLinks){
			mInLinksNews = true;
			mKeepData = true;
		} else if(localName.equals("review") && mInLinks){
			mInLinksReview = true;
			mKeepData = true;
		} else if(localName.equals("screenshots") && mInLinks){
			mInLinksScreen = true;
			mKeepData = true;
		} else if(localName.equals("cheats") && mInLinks){
			mInLinksCheats = true;
			mKeepData = true;
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(mKeepData){
			mCurrentValue += unescape(new String(ch, start, length));
			mCurrentValue = mCurrentValue.replaceAll("\\<.*?>","");
		}
	}	

	@Override
	public void endElement(String uri, String localName, String qName){
		if(localName.equals("game")){
			mInGame = false;
			mItem.put("description", mGame.getDescription());
			mItem.put("genre", mGame.getGenre());
			mItem.put("rating", mGame.getRating());
			mItem.put("scoreid", mGame.getScoreID());
			mItem.put("scorename", mGame.getScoreName());
			mItem.put("thumburl", mGame.getThumbURL());
			mItem.put("platform", mGame.getPlatform());
			mItem.put("release", mGame.getReleaseDate());
			mItem.put("officialsite", mGame.getOfficialSite());
			mItem.put("news", mGame.getNewsLink());
			mItem.put("review", mGame.getReviewLink());
			mItem.put("screenshots", mGame.getScreenshotsLink());
			mItem.put("cheats", mGame.getCheatsLink());
		} else if(localName.equals("description")){
			mInDescription = false;
			mKeepData = false;
			mGame.setDescription(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("genre")){
			mInGenre = false;
		} else if(localName.equals("name") && mInGenre){
			mInGenreName = false;
			mKeepData = false;
			mGame.setGenre(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("censorship")){
			mInCensorship = false;
		} else if(localName.equals("esrb") && mInCensorship){
			mInESRB = false;
		} else if(localName.equals("rating") && mInCensorship && mInESRB){
			mInRating = false;
			mKeepData = false;
			mGame.setRating(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("score")){
			mInScore = false;
		} else if(localName.equals("id") && mInScore){
			mInScoreID = false;
			mKeepData = false;
			mGame.setScoreID(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("name") && mInScore){
			mInScoreName = false;
			mKeepData = false;
			mGame.setScoreName(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("images")){
			mInImages = false;
		} else if(localName.equals("thumbnail") && mInImages){
			mInThumbnail = false;
			mKeepData = false;
			mGame.setThumURL(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("platform")){
			mInPlatform = false;
		} else if(localName.equals("name") && mInPlatform){
			mInPlatformName = false;
			mKeepData = false;
			mGame.setPlatform(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("release_date") || localName.equals("expected_release_date")){
			mInRelease = false;
		} else if(localName.equals("us") && mInRelease){
			mInReleaseUS = false;
			mKeepData = false;
			//Manipulate mCurrentValue to get desired date
			// Example date value: 2009-12-21T17:19:47.00+0000
			String releaseDate = "";
			if(mCurrentValue.length() != 27){
				releaseDate = mCurrentValue;
			} else {
				String dateAndTime[] = mCurrentValue.split("T");
				String articleDate = dateAndTime[0];
				String yearMonthDay[] = articleDate.split("-");
				String articleYear = yearMonthDay[0];
				String articleMonth = yearMonthDay[1];
				String articleDay = yearMonthDay[2];
				releaseDate = articleMonth + "/" + articleDay + "/" + articleYear;
			}

			mGame.setReleaseDate(releaseDate);
			mCurrentValue = "";
		} else if(localName.equals("official_site")){
			mInOfficialSite = false;
		} else if(localName.equals("us") && mInOfficialSite){
			mInOfficialSiteUS = false;
			mKeepData = false;
			mGame.setOfficialSite(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("links")){
			mInLinks = false;
		} else if(localName.equals("news") && mInLinks){
			mInLinksNews = false;
			mKeepData = false;
			mGame.setNewsLink(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("review") && mInLinks){
			mInLinksReview = false;
			mKeepData = false;
			mGame.setReviewLink(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("screenshots") && mInLinks){
			mInLinksScreen = false;
			mKeepData = false;
			mGame.setScreenshotsLink(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("cheats") && mInLinks){
			mInLinksCheats = false;
			mKeepData = false;
			mGame.setCheatsLink(mCurrentValue);
			mCurrentValue = "";
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
	}
	
	public HashMap<String,String> getItem(){
		return this.mItem;
	}
	
   private String unescape(String str)
    {
        str = str.replace("&amp;", "&");
        str = str.replace("&lt;", "<");
        str = str.replace("&gt;", ">");
        str = str.replace("&#039;", "\\");
        str = str.replace("&#39;", "'");
        str = str.replace("&quot;", "\"");
        str = str.replace("&lt;", "<");
        str = str.replace("&ndash;", "-");
        str = str.replace("&#8211;", "--");
        str = str.replace("&#8217;", "'");
        str = str.replace("&#8230;", "...");
        str = str.replace("&#160;", " ");
        str = str.replace("&#8220;", "\"");
        str = str.replace("&#8221;", "\"");      
        return str;
    }	
}
