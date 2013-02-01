package com.forthegamer.android.encyclopedia;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GamesSearchHandler extends DefaultHandler {
	private ArrayList<HashMap<String,String>> mList  = new ArrayList<HashMap<String,String>>();
	private HashMap<String,String> mItem;
	private Game mGame;
	private boolean mInGameID = false;
	private boolean mInGame = false;
	private boolean mInGameName = false;
	private String mPlatform = null;
	private String mCurrentValue = "";
	
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
		} else if(localName.equals("id")){
			mInGameID = true;
		} else if(localName.equals("name")){
			mInGameName = true;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(mInGameName || mInGameID){
			mCurrentValue += unescape(new String(ch, start, length));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName){
		if(localName.equals("game")){
			mInGame = false;
			mItem.put("gamename", mGame.getName());
			mItem.put("gameid", mGame.getID());
			mItem.put("substring", mPlatform);
			mList.add(mItem);
		} else if(localName.equals("id")){
			mInGameID = false;
			mGame.setID(mCurrentValue);
			mCurrentValue = "";
		} else if(localName.equals("name")){
			mInGameName = false;
			mGame.setName(mCurrentValue);
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
	
	public void setPlatform(String platform){
		this.mPlatform = platform;
	}
}
