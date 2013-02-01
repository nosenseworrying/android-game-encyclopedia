package com.forthegamer.android.encyclopedia;

import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GameReviewHandler extends DefaultHandler {
	private final String MY_DEBUG_TAG = "GameNewsRetriever";
	private boolean mInArticle = false;
	private boolean mInURL = false;
	String myCurrentValue = "";
	String mURL = null;
	private boolean mKeepData = false;

	@Override
	public void startDocument() throws SAXException {
		//Log.v(MY_DEBUG_TAG, "offset: " + offset);
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName.equals("article")){
			mInArticle = true;
		} else if(localName.equals("url")){
			mInURL = true;
			mKeepData = true;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(mKeepData){
			myCurrentValue += unescape(new String(ch, start, length));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName.equals("article")){
			mInArticle = false;
		} else if(localName.equals("url")){
			mInURL = false;
			mURL = myCurrentValue;
			myCurrentValue = "";
			mKeepData = false;
		}
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
	
	public String getURL(){
		return mURL;
	}

}

