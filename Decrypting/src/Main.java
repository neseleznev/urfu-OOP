import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Main 
{
	public static void main(String[] args) throws Exception
	{
		Vector<String> urls = new Vector<String>();
		read_urls(urls);
		
		CryptThread.encrypt("text.txt", "encrypted.txt", urls);
		
		long size = new File("encrypted.txt").length() / 4;
		StringBuilder decrypted = new StringBuilder((int) size);
		decrypted.setLength((int) size);
		int iDecrypt = 0;
		
		DataInputStream text = null;
		try
		{
			Map<String, String> cache = new HashMap<String, String>();
			Vector<Thread> threads = new Vector<Thread>();
			text = new DataInputStream(new FileInputStream("encrypted.txt"));
			while (text.available() > 0)
			{
				int number = text.readInt();
				
				int iUrl = number & 0xff;
				int pos = number >>> 8;
				//System.out.println("num: " + number + " url: " + iUrl + " pos: " + pos);
				
				threads.add(new Thread(new CryptThread(
						urls.get(iUrl), cache, pos, decrypted, iDecrypt)));
				threads.lastElement().start();
				iDecrypt++;
			}
			for (Thread thread : threads)
			{
				thread.join();
			}
			System.out.print("Decrypted: " + decrypted);
		}
		finally
		{
			if (text != null) {
				text.close();
			}
		}
	}
	
	private static void read_urls(Vector<String> urls)
	{
		BufferedReader urls_file = null;
		try
		{
			try
			{
				urls_file = new BufferedReader(new InputStreamReader(new FileInputStream("urls.txt"), "UTF-8"));
				String line = null;
				while ((line = urls_file.readLine()) != null) {
					urls.add(line);
				}
			}
			finally
			{
				if (urls_file != null) {
					urls_file.close();
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Error: ");
			e.printStackTrace();
		}
	}
}