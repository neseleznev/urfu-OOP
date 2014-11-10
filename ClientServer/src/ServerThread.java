import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class ServerThread implements Runnable 
{
	private Socket socket;
	private static Object createFile = new Object();
	
	public ServerThread(Socket socket) 
	{
		this.socket = socket;
	}
	
	public void run() 
	{
		String ip = socket.getInetAddress().getHostAddress();
		try 
		{			
			try 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));				
	
				String filename = in.readLine();
				System.out.println(ip + " send file " + filename);

				if (filename == null || filename.equals("")) 
				{
					return;
				}
				String newName = "";
				synchronized (createFile) 
				{
					String[] partsFileName = filename.split("\\.(?=[^\\.]+$)");
					newName = filename;
					Integer i = new Integer(1);
					while (fileExists(newName)) 
					{
						newName = partsFileName[0] + 
									 "(" + i.toString() + ")." + 
									 partsFileName[1];
						i++;
					}
					(new File(newName)).createNewFile();
				}
				System.out.println(ip + " start recieving into " + newName );
				
				out.write(newName + "\r\n"); out.flush();
				
				
				downloadFile(socket, newName);
				System.out.println(ip + " recieved " + newName);
			} 
			finally 
			{
				System.out.println("Disconnect...");
				socket.close();
			}
		} 
		catch (IOException e) 
		{
			System.out.println(ip + " client broke connection!");
		}
	}
	
	
	private boolean fileExists(String filename) 
	{
		File f = new File(filename);
		return f.exists() && !f.isDirectory();
	}
	
	private void downloadFile(Socket sock, String filename) throws IOException 
	{
		DataInputStream reader = new DataInputStream(socket.getInputStream());
		DataOutputStream writer = new DataOutputStream(new FileOutputStream(filename));
		try 
		{
			byte [] buf = new byte[1024];
			int size = 0;
			while ((size = reader.read(buf)) != -1) 
			{
				writer.write(buf, 0, size);
			}
		}
		finally 
		{
			reader.close();
			writer.close();
		}
	}
}
