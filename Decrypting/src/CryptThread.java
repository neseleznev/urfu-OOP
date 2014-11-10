import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Vector;


public class CryptThread  implements Runnable 
{
	String url;
	Map<String, String> cache;
	int pos;
	StringBuilder builder;
	int iBuilder;
	
	public CryptThread(String url, Map<String, String> cache, int pos, StringBuilder builder, int iBuilder) 
	{
		this.url = url;
		this.cache = cache;
		this.pos = pos;
		this.builder = builder;
		this.iBuilder = iBuilder;
	}
	
	public void run() 
	{
		char symbol;
		synchronized (cache) 
		{
			if (cache.get(url) == null) 
				cache.put(url, getPage(url));
			
			symbol = cache.get(url).charAt(pos);
		}
		
		synchronized (builder) 
		{
			builder.setCharAt(iBuilder, symbol);
		}
	}
	
	private static String getPage(String url) 
	{
		String ret = "";
		URL site = null;
		try 
		{
			site = new URL(url);
			ret = readFile(new BufferedReader(new InputStreamReader(site.openStream(), "UTF-8")));
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	private static String readFile(BufferedReader reader) throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		String s; 
        while((s = reader.readLine()) != null) 
        {
            sb.append(s).append('\n');
        }
        return sb.toString();
	}

	public static void encrypt(String in, String out, Vector<String> urls) throws Exception{
		String text = readFile(new BufferedReader (new InputStreamReader(new FileInputStream(in), "UTF-8")));
		
		Vector<String> urls_content = new Vector<String> ();
		for (String url: urls) {
			urls_content.add(getPage(url));
		}
		
		DataOutputStream writer = null;
		try {
			writer = new DataOutputStream(new FileOutputStream(out));		
			for (int i = 0; i < text.length(); ++i) {
				int jUrl = 0, pos = -1;
				for (jUrl = 0; jUrl < urls_content.size(); ++jUrl) {
					pos = urls_content.get(jUrl).indexOf(text.charAt(i)); 
					if (pos != -1) break; 	
				}
				if (pos == -1) throw new CharNotFoundException();
				int element = (pos << 8) + (jUrl & 0xff);
				
				ByteBuffer b = ByteBuffer.allocate(4);
				//b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
				b.putInt(element);
				writer.write(b.array());
			}
		} finally {
			writer.close();
		}		
	}
	
	
}
