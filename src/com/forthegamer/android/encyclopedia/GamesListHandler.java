package com.forthegamer.android.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GamesListHandler extends DefaultHandler {
	private final String MY_DEBUG_TAG = "GamesListHandler";
	private boolean mInGame = false;
	private boolean mInGameName = false;
	private boolean mInGameNameUS = false;
	private boolean mInRelease = false;
	private boolean mInReleaseUS = false;
	private boolean mInGameID = false;
	private boolean mInPlatform = false;
	private boolean mInPlatformName = false;
	private boolean mInScore = false;
	private boolean mInTotalRows = false;
	String mCurrentValue = "";
	private Game mGame;
	private ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();
	private HashMap<String,String> mItem;
	private int mTotalNumRows = 0;
	
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
		} else if(localName.equals("name") && !mInPlatform && !mInScore){
			mInGameName = true;
		} else if(localName.equals("id") && !mInPlatform && !mInScore){
			mInGameID = true;
		} else if(localName.equals("platform")){
			mInPlatform = true;
		} else if(localName.equals("name") && mInPlatform){
			mInPlatformName = true;
		} else if(localName.equals("score")){
			mInScore = true;
		} else if(localName.equals("us") && mInGameName){
			mInGameNameUS = true;
		} else if(localName.equals("total_rows")){
			mInTotalRows = true;
		} else if(localName.equals("release_date") || localName.equals("expected_release_date")){
			mInRelease = true;
		} else if(localName.equals("us") && mInRelease){
			mInReleaseUS = true;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(mInGameNameUS || mInGameID || mInPlatformName || mInTotalRows || mInReleaseUS){
			mCurrentValue += unescape(new String(ch, start, length));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName){
		if(localName.equals("game")){
			mInGame = false;
			mItem.put("gamename", mGame.getName());
			mItem.put("gameid", mGame.getID());
			mItem.put("substring", mGame.getSubstring());
			mItem.put("release", mGame.getReleaseDate());
			mItem.put("platform", mGame.getPlatform());
			mList.add(mItem);
		} else if(localName.equals("name") && !mInPlatform && !mInScore){
			mInGameName = false;
		} else if(localName.equals("id") && !mInPlatform && !mInScore){
			mInGameID = false;
			mGame.setID(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("platform")){
			mInPlatform = false;
		} else if(localName.equals("name") && mInPlatform){			
			mInPlatformName = false;
			mGame.setPlatform(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("score")){
			mInScore = false;
		} else if(localName.equals("us") && mInGameName){
			mInGameNameUS = false;
			mGame.setName(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("total_rows")){
			mInTotalRows = false;
			mTotalNumRows = Integer.parseInt(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("release_date") || localName.equals("expected_release_date")){
			mInRelease = false;
		} else if(localName.equals("us") && mInRelease){
			mInReleaseUS = false;
			
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
		}
	}

	public ArrayList<HashMap<String,String>> getList(){
		return mList;
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
	
	@Override
	public void endDocument() throws SAXException {
	}
	
	public int getTotalNumRows(){
		return mTotalNumRows;
	}
}
