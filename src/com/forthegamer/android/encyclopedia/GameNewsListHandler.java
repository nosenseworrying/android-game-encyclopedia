package com.forthegamer.android.encyclopedia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GameNewsListHandler extends DefaultHandler {
	private final String MY_DEBUG_TAG = "GameNewsRetriever";
	private boolean mInArticle = false;
	private boolean mInPublishedDate = false;
	private boolean mInHeadline = false;
	private boolean mInStrapline = false;
	private boolean mInURL = false;
	String myCurrentValue = "";
	private boolean mKeepData = false;
	private DateFormat currentMonthFormat = new SimpleDateFormat("MM");
	private DateFormat currentDayFormat = new SimpleDateFormat("dd");
	private DateFormat currentYearFormat = new SimpleDateFormat("yyyy");
	private Date currentDate = new Date();
    private String currentMonth = currentMonthFormat.format(currentDate);
    private String currentDay = currentDayFormat.format(currentDate);
    private String currentYear = currentYearFormat.format(currentDate);
	private int iCurrentMonth = Integer.parseInt(currentMonth);
	private int iCurrentDay = Integer.parseInt(currentDay);
	private int iCurrentYear = Integer.parseInt(currentYear);
	private Calendar cal = Calendar.getInstance();
	private TimeZone tz = cal.getTimeZone();
	private int offset = tz.getOffset(currentDate.getTime()) / 3600000;
	
	private static final int MAX_STRAPLINE_SIZE = 115;
	
	private GameNews mGameNews;
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private HashMap<String,String> item;
	
	@Override
	public void startDocument() throws SAXException {
		//Log.v(MY_DEBUG_TAG, "offset: " + offset);
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName.equals("article")){
			mInArticle = true;
			item = new HashMap<String,String>();
			mGameNews = new GameNews();
		} else if(localName.equals("headline")){
			mInHeadline = true;
			mKeepData = true;
		} else if(localName.equals("published_date")){
			mInPublishedDate = true;
			mKeepData = true;
		} else if(localName.equals("strapline")){
			mInStrapline = true;
			mKeepData = true;
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
			item.put("headline", mGameNews.getHeadline());
			item.put("substring", mGameNews.getSubstring());
			item.put("url", mGameNews.getURL());
			list.add(item);
		} else if(localName.equals("headline")){
			mInHeadline = false;
			mGameNews.setHeadline(myCurrentValue);
			myCurrentValue = "";
			mKeepData = false;
		} else if(localName.equals("published_date")){
			mInPublishedDate = false;
			String dateString = "";
			
			//Manipulate myCurrentValue to get desired date
			// Example date value: 2009-12-21T17:19:47.00+0000
			if(myCurrentValue.length() != 27){
				mGameNews.setPublishedDate(myCurrentValue);
			} else {
				String dateAndTime[] = myCurrentValue.split("T");
				String articleDate = dateAndTime[0];
				String articleTime = dateAndTime[1];
				String yearMonthDay[] = articleDate.split("-");
				String articleYear = yearMonthDay[0];
				String articleMonth = yearMonthDay[1];
				String articleDay = yearMonthDay[2];
				String hourMinuteSecond[] = articleTime.split(":");
				String articleHour = hourMinuteSecond[0];
				String articleMinute = hourMinuteSecond[1];
				boolean isYesterday = false;
				
				int iArticleYear = Integer.parseInt(articleYear);
				int iArticleMonth = Integer.parseInt(articleMonth);
				int iArticleDay = Integer.parseInt(articleDay);
				int iArticleHour = Integer.parseInt(articleHour);
				
				// Calculate local time zone difference
				iArticleHour = iArticleHour + offset;
				
				String amPm = "am";
				if(iArticleHour > 12){
					iArticleHour = iArticleHour - 12;
					amPm = "pm";
				} else if(iArticleHour == 0){
					iArticleHour = 12;
				} else if(iArticleHour < 0){
					iArticleDay = iArticleDay - 1;
					if(iArticleDay < iCurrentDay){
						isYesterday = true;
						
						// Check for month boundaries
						if(iArticleDay == 0){
							iArticleMonth = iArticleMonth - 1;
							
							// Check for year boundaries
							if(iArticleMonth == 0){
								iArticleMonth = 12;
								iArticleYear = iArticleYear - 1;
							}
							
							// Get the number of days in the article month
							Calendar cal = new GregorianCalendar(iArticleYear, iArticleMonth, 1);  
							iArticleDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
						}
					}
				} else if(iArticleHour == 12){
					amPm = "pm";
				}
				
				articleHour = Integer.toString(iArticleHour);
				articleMonth = Integer.toString(iArticleMonth);
				if(isYesterday){
					articleDay = Integer.toString(iArticleDay);
					if(iArticleDay < 10){
						articleDay = "0" + articleDay;
					}
				}
				
				if(iArticleMonth >= iCurrentMonth && iArticleDay >= iCurrentDay && iArticleYear >= iCurrentYear && !isYesterday){
					dateString = articleHour + ":" + articleMinute + " " + amPm;
				} else {
					dateString = articleMonth + "/" + articleDay + "/" + articleYear;
				}				
			}

			mGameNews.setPublishedDate(dateString);
			myCurrentValue = "";
			mKeepData = false;
		} else if(localName.equals("strapline")){
			mInStrapline = false;
			if(myCurrentValue.length() > MAX_STRAPLINE_SIZE){
				myCurrentValue = myCurrentValue.substring(0, MAX_STRAPLINE_SIZE) + "...";
			}
			mGameNews.setStrapline(myCurrentValue);
			myCurrentValue = "";
			mKeepData = false;
		} else if(localName.equals("url")){
			mInURL = false;
			mGameNews.setURL(myCurrentValue);
			myCurrentValue = "";
			mKeepData = false;
		}
	}
	
	public ArrayList<HashMap<String,String>> getList(){
		return this.list;
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

}
