import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class XmlToJsonApp {

	static String sampleXml = "";
	public XmlToJsonApp() {
		// TODO Auto-generated constructor stub
		//sampleXml = readFile("sample-xml.xml", Charset.defaultCharset());
		
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
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
	
	 

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		URL url = null;
		try {
			url = new URL("http://services.tvrage.com/feeds/last_updates.php?hours=1");
			InputStream stream = url.openStream();
			sampleXml = getStringFromInputStream(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("Xml:\n"+sampleXml);
		JSONObject jsonObj = null;
		try {
		    jsonObj = XML.toJSONObject(sampleXml);
		} catch (JSONException e) {
		    e.printStackTrace();
		} 
		System.out.print("JSON:\n"+jsonObj.toString());
	}

}
