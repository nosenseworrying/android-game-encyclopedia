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

public class FeaturesListHandler extends DefaultHandler {
	private final String MY_DEBUG_TAG = "GameNewsRetriever";
	private boolean in_articles = false;
	private boolean in_article = false;
	private boolean in_game = false;
	private boolean in_game_name = false;
	private boolean in_game_platform = false;
	private boolean in_game_platform_name = false;
	private boolean in_published_date = false;
	private boolean in_headline = false;
	private boolean in_strapline = false;
	private boolean in_url = false;
	private boolean mInTotalRows = false;
	String myCurrentValue = "";
	private int mTotalNumRows = 0;
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
    
	private Feature mFeature;
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private HashMap<String,String> item;
	
	@Override
	public void startDocument() throws SAXException {
		//Log.v(MY_DEBUG_TAG, "offset: " + offset);
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName.equals("articles")){
			this.in_articles = true;
		} else if(localName.equals("article")){
			this.in_article = true;
			item = new HashMap<String,String>();
			mFeature = new Feature();
		} else if(localName.equals("game")){
			this.in_game = true;
		} else if(localName.equals("platform")){
			this.in_game_platform = true;
		} else if(localName.equals("name") && this.in_game_platform){
			this.in_game_platform_name = true;
		} else if(localName.equals("name") && this.in_game  && !this.in_game_platform){
			this.in_game_name = true;
		} else if(localName.equals("headline")){
			this.in_headline = true;
			//Log.v(MY_DEBUG_TAG, "in the headline!");
		} else if(localName.equals("published_date")){
			this.in_published_date = true;
		} else if(localName.equals("strapline")){
			this.in_strapline = true;
		} else if(localName.equals("url")){
			this.in_url = true;
		} else if(localName.equals("total_rows")){
			mInTotalRows = true;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(this.in_game_name || this.in_headline || this.in_game_platform_name || this.in_published_date || this.in_strapline || this.in_url || this.mInTotalRows){
			myCurrentValue += unescape(new String(ch, start, length));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName.equals("articles")){
			this.in_articles = false;
		} else if(localName.equals("article")){
			this.in_article = false;
			item.put("headline", mFeature.getExtractedHeadline());
			item.put("substring", mFeature.getSubstring());
			item.put("color", mFeature.getTabColor());
			item.put("date", mFeature.getExtractedDate());
			item.put("url", mFeature.getExtractedURL());
			list.add(item);
		} else if(localName.equals("game")){
			this.in_game = false;
		} else if(localName.equals("platform")){
			this.in_game_platform = false;
		} else if(localName.equals("name") && this.in_game_platform){
			this.in_game_platform_name = false;
			mFeature.setExtractedPlatform(myCurrentValue);
			
			//Log.v(MY_DEBUG_TAG, "value: " + myCurrentValue);
			
			// Set the tab color based on platform
			String color = "white";
			if(myCurrentValue.equalsIgnoreCase("PC")){
				color = "red";
			} else if(myCurrentValue.toLowerCase().startsWith("ps")){
				color = "blue";
			} else if(myCurrentValue.toLowerCase().startsWith("xbox")){
				color = "green";
			} else if(myCurrentValue.equalsIgnoreCase("wii") || myCurrentValue.equalsIgnoreCase("ds")){
				color = "lightblue";
			}
			
			//Log.v(MY_DEBUG_TAG, "color: " + color);
			mFeature.setTabColor(color);
			
			myCurrentValue = "";
		} else if(localName.equals("name") && this.in_game && !this.in_game_platform){
			this.in_game_name = false;
			mFeature.setExtractedName(myCurrentValue);
			myCurrentValue = "";
		} else if(localName.equals("headline")){
			this.in_headline = false;
			mFeature.setExtractedHeadline(myCurrentValue);
			myCurrentValue = "";
		} else if(localName.equals("published_date")){
			this.in_published_date = false;
			String dateString = "";
			
			//Manipulate myCurrentValue to get desired date
			// Example date value: 2009-12-21T17:19:47.00+0000
			if(myCurrentValue.length() != 27){
				mFeature.setExtractedDate(myCurrentValue);
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
					dateString = articleMonth + "/" + articleDay;
				}				
			}

			mFeature.setExtractedDate(dateString);
			myCurrentValue = "";
		} else if(localName.equals("strapline")){
			this.in_strapline = false;
			mFeature.setExtractedStrapline(myCurrentValue);
			myCurrentValue = "";
			
		} else if(localName.equals("url")){
			this.in_url = false;
			mFeature.setExtractedURL(myCurrentValue);
			myCurrentValue = "";
		} else if(localName.equals("total_rows")){
			mInTotalRows = false;
			mTotalNumRows = Integer.parseInt(myCurrentValue);
			myCurrentValue = "";
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
	
	public int getTotalNumRows(){
		return mTotalNumRows;
	}
}
