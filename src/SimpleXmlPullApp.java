/**
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/**
 * @author xzgao
 *
 */
public class SimpleXmlPullApp {

	final static boolean EXTERNAL_STRING = true;
	
	/**
	 * 
	 */
	public SimpleXmlPullApp() {
		// TODO Auto-generated constructor stub
		
	}
	
	private static String getXmlText(){
		if(EXTERNAL_STRING){
			return getStringFromUrl("http://services.tvrage.com/feeds/last_updates.php?hours=1");
		}else{
			return "<foo id='1.0' >1000</foo>";
		}
	}
	
	private static String getStringFromUrl(String urlString){
		URL url = null;
		InputStream stream = null;
		String xmlText = "";
		try {
			url = new URL(urlString);
			stream = url.openStream();
			xmlText = getStringFromInputStream(stream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlText;
	}
	
	private static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	
	public static boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	} 
	
	public static Object convertXmlItem(String item){
		if(item == null){
			return null;
		}
		
		if(item.equalsIgnoreCase("true")){
			return true;
		}
		
		if(item.equalsIgnoreCase("false")){
			return false;
		}
		
		if(isNumeric(item)){
			try{
				 return Long.parseLong(item);
			 }catch(NumberFormatException e){
				 return Double.parseDouble(item);
			 } 
		}
		
		return item;
	}
	
	public static Object getObjectFromXml(String xmlText) throws XmlPullParserException, IOException{
		List<Object> resultList = new ArrayList<Object>();
		Stack<Entry> stack = new Stack<Entry>();
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput( new StringReader(xmlText) );
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
         if(eventType == XmlPullParser.START_DOCUMENT) {
             System.out.println("Start document");
         } else if(eventType == XmlPullParser.START_TAG) {
             System.out.println("Start tag "+xpp.getName());
             
             Entry tmpEntry = new Entry(xpp.getName());
             stack.push(tmpEntry);	//pushing tag entry into stack
             System.out.println(stack.peek());
             
             if(xpp.getAttributeCount() > 0){
            	 for(int i=0,n=xpp.getAttributeCount();i<n;i++){
            		 System.out.println("Attribute "+xpp.getAttributeName(i)+":"+xpp.getAttributeValue(i));
            		 
            		 List<Object> tmpList = new ArrayList<Object>();
            		 tmpList.add(xpp.getAttributeName(i));
            		 tmpList.add(convertXmlItem(xpp.getAttributeValue(i)));
            		 stack.peek().value.add(tmpList);
            	 }
             }
             System.out.println(stack.peek());
             
         } else if(eventType == XmlPullParser.END_TAG) {
             System.out.println("End tag "+xpp.getName());
             System.out.println(stack.peek());
             
             Entry tmpEntry = stack.pop();
             if(stack.isEmpty()){
            	 resultList.add(tmpEntry.toPair());
             }else{
            	 stack.peek().value.add(tmpEntry.toPair());
             }
         } else if(eventType == XmlPullParser.TEXT) {
             System.out.println("Text: "+xpp.getText());
             List<Object> tmpList = new ArrayList<Object>();
    		 tmpList.add("content");
    		 tmpList.add(convertXmlItem(xpp.getText()));
    		 stack.peek().value.add(tmpList);
    		 System.out.println(stack.peek());
         }
         eventType = xpp.next();
        }
        System.out.println("End document");
        if(!stack.isEmpty()){
        	throw new XmlPullParserException("Invalid XML.");
        }
		return resultList;		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		getObjectFromXml(getXmlText());
	}
	
	// Tag entry
	private static class Entry{
		public String key;
		public List<Object> value;
		
		public Entry(String k){
			key = k;
			value = new ArrayList<Object>();
		}
		
		public List<Object> toPair(){
			List<Object> tmp = new ArrayList<Object>();
			tmp.add(key);
			tmp.add(value);
			return tmp;
		}
		
		public String toString(){
			return "Tag: " + key + "{"+value.size()+"}";
		}
		
	}
	
}
