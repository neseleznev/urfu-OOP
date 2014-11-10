import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server 
{
	public static void main(String[] args) 
	{
		System.out.println("Welcome to File Recieving server application\r\n");
		try 
		{
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(1235);
			System.out.println("Listening...");
			while (true) 
			{				
				Socket socket = serverSocket.accept();
				System.out.println(socket.getInetAddress().getHostAddress());
	
				ServerThread myServer = new ServerThread(socket);
				new Thread(myServer).start();
			}
		} 
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
