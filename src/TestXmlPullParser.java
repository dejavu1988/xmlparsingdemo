/**
 * 
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/**
 * @author xzgao
 *
 */
public class TestXmlPullParser {

    static Reader reader = null;
	final boolean EXTERNAL_STRING = true;
	/**
	 * 
	 */
	public TestXmlPullParser() {
		// TODO Auto-generated constructor stub
		if(!EXTERNAL_STRING){
			reader = new StringReader ( "<foo id='1' value='1000'/>");
		}else{
			URL url = null;
			InputStream stream = null;
			try {
				url = new URL("http://weather.yahooapis.com/forecastrss?w=12828021&u=c");
				stream = url.openStream();
				reader = new InputStreamReader(stream, "utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        new TestXmlPullParser();
		xpp.setInput( TestXmlPullParser.reader );
        int eventType = xpp.getEventType();
        //boolean valueOn = false; // flag of 'value' in current tag attributes
        while (eventType != XmlPullParser.END_DOCUMENT) {
         if(eventType == XmlPullParser.START_DOCUMENT) {
             System.out.println("Start document");
         } else if(eventType == XmlPullParser.START_TAG) {
             System.out.println("Start tag "+xpp.getName());
             if(xpp.getAttributeCount() > 0){
            	 for(int i=0,n=xpp.getAttributeCount();i<n;i++){
            		 System.out.println(xpp.getAttributeName(i)+":"+xpp.getAttributeValue(i));
            		 /*if(xpp.getAttributeName(i).equals("value")){
            			 valueOn = true;	// set flag when 'value' found in attributes
            		 }*/
            	 }
             }
         } else if(eventType == XmlPullParser.END_TAG) {
             System.out.println("End tag "+xpp.getName());
             //valueOn = false; // reset flag
         } else if(eventType == XmlPullParser.TEXT) {
             System.out.println("value:"+xpp.getText());
             /*if(valueOn){
            	 // Collision occurs            	 
             }*/
         }
         eventType = xpp.next();
        }
        System.out.println("End document");

	}

}
