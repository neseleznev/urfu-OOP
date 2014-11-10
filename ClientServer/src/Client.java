import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client 
{

	public static void main(String[] args) 
	{
		System.out.println("Welcome to File Recieving client application\r\n");
		Scanner input = new Scanner(System.in);
		try {
			while (true) 
			{
				Socket socket = new Socket("127.0.0.1", 1235);
				try 
				{
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
					System.out.print("Enter file path>");
					String path = input.nextLine();
					if (path.equals("")) 
						break;
					
					if (!fileExists(path)) 
					{
						System.out.println("File not found");
						continue;
					}
					String filename = new File(path).getName();
					out.write(filename + "\r\n"); out.flush();
					String newFileName = in.readLine();				
					System.out.println("Server will recieve this file with name: \"" + newFileName + "\"");
					uploadFile(path, socket);
					System.out.println("File has been succesfully uploaded :)");
				} 
				finally
				{
					socket.close();
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("connection is broken");
		}
	}
	
	private static void uploadFile(String filename, Socket socket) throws IOException 
	{
		DataInputStream reader;
		DataOutputStream writer;
		try 
		{
			reader = new DataInputStream(new FileInputStream(filename));
			writer = new DataOutputStream(socket.getOutputStream());
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
		catch (FileNotFoundException e)
		{
			System.out.println("File " + filename + " not found");
		}
	}
	
	private static boolean fileExists(String filename) 
	{
		File f = new File(filename);
		return f.exists() && !f.isDirectory();
	}
}
