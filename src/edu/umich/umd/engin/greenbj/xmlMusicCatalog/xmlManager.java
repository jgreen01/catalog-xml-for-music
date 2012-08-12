package edu.umich.umd.engin.greenbj.xmlMusicCatalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class xmlManager {
	
	private ArrayList<String> titList;
	private ArrayList<String> artList;
	private ArrayList<String> couList;
	private ArrayList<String> comList;
	private ArrayList<Double> priList;
	private ArrayList<Integer> yeaList;
	private int count; // set inside readData()
	
	private static String TAG = "xmlManager";
	private File xmlDB;
	private String pathDB;
	
	public xmlManager(String xmlFile){
		
		pathDB = xmlFile;
		
		try { // to create xmlDB
			xmlDB = createXml(pathDB);
		} catch (IOException e) {
			Log.e(TAG,e.getMessage());
		}
		
		titList = new ArrayList<String>();
		artList = new ArrayList<String>();
		couList = new ArrayList<String>();
		comList = new ArrayList<String>();
		priList = new ArrayList<Double>();
		yeaList = new ArrayList<Integer>();
		
		try { // to read xml data
			readData();
		} catch (XmlPullParserException e) {
			Log.e(TAG,e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,e.getMessage());
		}
		
	}
	
	private void readData() 
			throws XmlPullParserException, IOException{

		int i = 0;
		FileInputStream fileStreamIn = new FileInputStream(xmlDB);
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser XmlPP = factory.newPullParser();
        
        XmlPP.setInput(fileStreamIn, "UTF-8");
        
        int eventType = XmlPP.getEventType();
        
        while (eventType != XmlPullParser.END_DOCUMENT) {
        	if(eventType == XmlPullParser.START_TAG) {
        		
        		if (XmlPP.getName().equals("CD")) 
        			i++;
        		
        		else if (XmlPP.getName().equals("TITLE"))
        			titList.add(XmlPP.nextText());
        			
        		else if (XmlPP.getName().equals("ARTIST"))
        			artList.add(XmlPP.nextText());
        			
        		else if (XmlPP.getName().equals("COUNTRY"))
        			couList.add(XmlPP.nextText());
        			
        		else if (XmlPP.getName().equals("COMPANY"))
        			comList.add(XmlPP.nextText());
        			
        		else if (XmlPP.getName().equals("PRICE"))
        			priList.add(new Double(XmlPP.nextText()).doubleValue());
        			
        		else if (XmlPP.getName().equals("YEAR"))
        			yeaList.add(Integer.parseInt(XmlPP.nextText()));

        	}
        	eventType = XmlPP.next();
        }
        
        count = i;
	}
	
	public void add(String titVal, String artVal, String couVal, 
					String comVal, Double priVal, Integer yeaVal){
		
		if(!titVal.equals(""))
			titList.add(0,titVal);
		else
			titList.add(0,"unknown");
			
		
		if(!artVal.equals(""))
			artList.add(0,artVal);
		else
			artList.add(0, "unknown");
		
		if(!couVal.equals(""))
			couList.add(0, couVal);
		else
			couList.add(0, "unknown");
		
		if(!comVal.equals(""))
			comList.add(0,comVal);
		else
			comList.add(0, "unknown");

		priList.add(0, priVal);
		yeaList.add(0, yeaVal);

		count++;
		
		try {
			writeData();
		} catch (IOException e) {
			Log.e(TAG,e.getMessage());
		}
	}
	
	public void remove(int delIndex){
		
		if(count != 0){
			titList.remove(delIndex);
			artList.remove(delIndex);
			couList.remove(delIndex);
			comList.remove(delIndex);
			priList.remove(delIndex);
			yeaList.remove(delIndex);
			count--;
			
			try {
				writeData();
			} catch (IOException e) {
				Log.e(TAG,e.getMessage());
			}
		}
	}
	
	private void writeData() 
			throws IOException{
		XmlSerializer serializer = Xml.newSerializer();
	    
	    FileOutputStream fileStreamOut = new FileOutputStream(xmlDB);
	    
	    serializer.setOutput(fileStreamOut, "UTF-8");
	    serializer.startDocument(null, Boolean.valueOf(true));
	    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	    
	   	serializer.startTag(null, "CATALOG");
	   		
   		for(int i = 0; i < count; i++){
   			serializer.startTag(null,"CD");
	   			serializer.startTag(null,"TITLE");
	   				serializer.text(titList.get(i));
	   			serializer.endTag(null, "TITLE");
	   			serializer.startTag(null, "ARTIST");
	   				serializer.text(artList.get(i));
	   			serializer.endTag(null, "ARTIST");
	   			serializer.startTag(null, "COUNTRY");
	   				serializer.text(couList.get(i));
	   			serializer.endTag(null, "COUNTRY");
	   			serializer.startTag(null, "COMPANY");
	   				serializer.text(comList.get(i));
	   			serializer.endTag(null, "COMPANY");
	   			serializer.startTag(null, "PRICE");
	   				serializer.text(priList.get(i).toString());
	   			serializer.endTag(null, "PRICE");
	   			serializer.startTag(null, "YEAR");
	   				serializer.text(yeaList.get(i).toString());
	   			serializer.endTag(null, "YEAR");
   			serializer.endTag(null, "CD");
   		}
	   		
	   	serializer.endTag(null, "CATALOG");
	    
	   	serializer.endDocument();
        serializer.flush();
        fileStreamOut.close();
	}
	
	private File createXml(String xFile)  // only creates if it doesn't exist
			throws IOException{
		File xmlFile = new File(xFile);
		
		if (!xmlFile.exists()){
			
			xmlFile.createNewFile();
			FileOutputStream fileStreamOut = new FileOutputStream(xmlFile);
			XmlSerializer serializer = Xml.newSerializer();
			
			serializer.setOutput(fileStreamOut, "UTF-8");
			serializer.startDocument(null, Boolean.valueOf(true));
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			serializer.endDocument();
			serializer.flush();
			fileStreamOut.close();
		}
		
		return xmlFile;
	}
	
	public ArrayList<String> getTitList() {
		return titList;
	}
	
	public ArrayList<String> getArtList() {
		return artList;
	}
	
	public ArrayList<String> getComList() {
		return comList;
	}

	public ArrayList<String> getCouList() {
		return couList;
	}

	public ArrayList<Double> getPriList() {
		return priList;
	}
	
	public ArrayList<Integer> getYeaList() {
		return yeaList;
	}
	
	public int getCount() {
		return count;
	}
	
}
